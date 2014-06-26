package weather.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.*;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;


import weather.app.Classes.Weather.CurrentWeather;
import weather.app.HelperMethods.FutureWeather_XMLParse;
import weather.app.HelperMethods.Location_GetBackGroundImage;
import weather.app.HelperMethods.WeatherDates;
import weather.app.HelperMethods.WeatherIdIcons;
import weather.app.HelperMethods.Weather_Location;
import weather.app.HelperMethods.Weather_Network;
import weather.app.HelperMethods.Weather_XMLParse;

public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private Handler handler;
    private Context context = null;
    private View v = null;

    ImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog;

    DemoCollectionPagerAdapter pageAdapter;

    @Override
    public View findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Handler
        handler = new Handler();
    }

    @Override
    public void onStart()
    {
        // Call Parent
        super.onStart();

        // Thread to initiate the call to get current location weather information
        startThread();

        pageAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.myViewPager);

        pager.setAdapter(pageAdapter);
    }


    // Web Calls cannot be done on the main thread, therefore they will be done on a secondary thread
    protected void startThread() {
        Thread t = new Thread() {
            public void run() {

                // Get current location
                Weather_Location weatherLocation = new Weather_Location(getApplicationContext());

                String jString = "";
                // Get Background Image for current location
                Location_GetBackGroundImage background = new Location_GetBackGroundImage();

                try {
                   background.DownloadText(weatherLocation.GetLatitude(), weatherLocation.GetLongitude(), 1);
                }
                catch (IOException e1) {}
                catch (ParseException e2) {}

                final String locBackGround = background.panoramas.panomarasPhotos.get(0).photo_file_url ;

                // Get Current Weather Conditions
                Weather_Network weatherCall = new Weather_Network();

                String weatherXML = weatherCall.DownloadText(weatherLocation.GetLatitude(), weatherLocation.GetLongitude(), 1);

                // Parse Current Weather Conditions XML
                final Weather_XMLParse weatherXMLParse = new Weather_XMLParse(weatherXML);
                try {
                    weatherXMLParse.ParseCurrentWeatherXML();
                }
                catch (XmlPullParserException e1) {
                    throw new RuntimeException(e1);
                }
                catch (IOException e2) {
                    throw new RuntimeException(e2);
                }

                // GET THE FUTURE WEATHER
                String weatherXMLFuture = weatherCall.DownloadText(weatherLocation.GetLatitude(), weatherLocation.GetLongitude(), 2);

                // Parse Future Weather Conditions XML
                final FutureWeather_XMLParse futureXMLParse = new FutureWeather_XMLParse(weatherXMLFuture);
                try {
                    futureXMLParse.ParseWeatherXML();
                } catch (XmlPullParserException e1) {
                    throw new RuntimeException(e1);
                }
                catch (IOException e2) {
                    throw new RuntimeException(e2);
                }

                //Set the values for the Widget Controls
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SetUIControls(weatherXMLParse, futureXMLParse, locBackGround);
                    }
                });

            }
        };
        t.start();
    }

    public static Bitmap loadBitmap(String URL, BitmapFactory.Options options) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e1) {
        }
        return bitmap;
    }

    private static InputStream OpenHttpConnection(String strURL)
            throws IOException {
        InputStream inputStream = null;
        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
        }
        return inputStream;
    }

    private void SetUIControls(Weather_XMLParse weatherXMLParse, FutureWeather_XMLParse futureXMLParse, String JSON) {

        // Set Background
        img = (ImageView)findViewById(R.id.background);
        new LoadImage().execute(JSON);

        // Set Current Weather Conditions
        SetCurrentWeatherControls(weatherXMLParse.getCurrentWeather());

        // Set Future
        pageAdapter.SetFutureForecaseFragment(futureXMLParse);

        // Set Clothing Recommendations
        pageAdapter.SetClothingForecast(weatherXMLParse.getCurrentWeather());
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {
            if(image != null){
                Log.d("Background image: ", "processing completed.");
                img.setImageBitmap(image);
            }else{
                Log.d("Background image: ", "processing still in progress.");
            }
        }
    }

    private void SetCurrentWeatherControls(CurrentWeather currentWeather)
    {
        // Set the Day of the Week
        TextView dateTextView = (TextView) findViewById(R.id.Day);
        String weekDay = WeatherDates.GetWeatherDays();
        dateTextView.setText(weekDay);

        // Set the Month + Day
        String dd = WeatherDates.GetWeatherMonths();
        TextView monthTextView = (TextView) findViewById(R.id.Month);
        monthTextView.setText(dd);

        // Set the City
        TextView cityTextView = (TextView) findViewById(R.id.CityText);
        cityTextView.setText(currentWeather.cityName);

        // Set Current Temperature
        TextView currentTemperatureText = (TextView) findViewById(R.id.CurrentTemperatureText);
        currentTemperatureText.setText((int)Math.round(currentWeather.temperature) + " °C");

        // Set Minimum Temperature
        TextView minTemperatureText = (TextView) findViewById(R.id.MinimumTemperatureText);
        minTemperatureText.setText("Low: " + (int)Math.round(currentWeather.lowTemp) + " °C");

        // Set Maximum Temperature
        TextView maxTemperatureText = (TextView) findViewById(R.id.MaximumTemperatureText);
        maxTemperatureText.setText("High: " + (int)Math.round(currentWeather.highTemp) + " °C");

        // Set PNG Weather Image
        ImageView weatherImage = (ImageView) findViewById(R.id.WeatherImage);
        int iconId = WeatherIdIcons.SetWeatherCondition(currentWeather.weatherIconId, 1);
        if (iconId != 0) {
            weatherImage.setImageBitmap(WeatherIdIcons.invertImage(BitmapFactory.decodeResource(getResources(), iconId )));
        }
    }

}
