package weather.app;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

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

    private Fragmentfutureforecast future;
    private Handler handler;

    ImageView img;
    Bitmap bitmap;

    @Override
    public View findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        future = new Fragmentfutureforecast();
        getSupportFragmentManager().beginTransaction().add(R.id.linlayout, future, "tag").commit();

        handler = new Handler();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        BackGroundImageThread();
        CurrentWeatherThread();
        FutureWeatherThread();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:

                Intent intent =  new Intent(this, Citylist.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSearch()
    {
        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    protected void BackGroundImageThread(){
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

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SetBackground(locBackGround);
                    }
                });
            }
        };
        t.start();
    }

    protected void CurrentWeatherThread(){
        Thread t = new Thread() {
            public void run() {

                Weather_Location weatherLocation = new Weather_Location(getApplicationContext());
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

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SetCurrentWeatherUIControls(weatherXMLParse);
                    }
                });
            }
        };
        t.start();
    }

    protected void FutureWeatherThread() {
        Thread t = new Thread() {
            public void run() {

                Weather_Location weatherLocation = new Weather_Location(getApplicationContext());
                Weather_Network weatherCall = new Weather_Network();
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

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SetFutureWeather(futureXMLParse);
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

    private static InputStream OpenHttpConnection(String strURL) throws IOException
    {
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
        }
        catch (Exception ex) {}

        return inputStream;
    }

    private void SetBackground(String JSON)
    {
        img = (ImageView)findViewById(R.id.background);
        new LoadImage().execute(JSON);
    }

    private void SetCurrentWeatherUIControls(Weather_XMLParse weatherXMLParse)
    {
        SetCurrentWeatherControls(weatherXMLParse.getCurrentWeather());
    }

    private void SetFutureWeather(FutureWeather_XMLParse futureXMLParse)
    {
        future.setFragmentControlData(futureXMLParse);
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
            weatherImage.setImageResource(iconId);
        }
    }

}
