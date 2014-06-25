package weather.app;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView;
import android.view.*;
import android.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.app.FragmentManager;
import android.widget.TextView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.lang.Object;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Calendar;
import android.graphics.Color;
import weather.app.Classes.FutureWeather;
import weather.app.HelperMethods.FutureWeather_XMLParse;
import weather.app.HelperMethods.Location_GetBackGroundImage;
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
        //img.setScaleType(ImageView.ScaleType.FIT_XY);
        // Change Opacity of Image
        int opacity = 200;
        img.setBackgroundColor(opacity * 0x1000000);
        new LoadImage().execute(JSON);


        // Set the Day of the Week
        TextView dateTextView = (TextView) findViewById(R.id.Day);
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        String weekDay = "";
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "Monday";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "Tuesday";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "Wednesday";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "Thursday";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "Friday";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "Saturday";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "Sunday";
        }
        dateTextView.setText(weekDay);

        // Set the Month + Day
        int month = c.get(Calendar.MONTH);
        String dd = "";
        if (month == Calendar.JANUARY) {
            dd = "January " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.FEBRUARY) {
            dd = "February " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.MARCH) {
            dd = "March " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.APRIL) {
            dd = "April " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.MAY) {
            dd = "May " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.JUNE) {
            dd = "June " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.JULY) {
            dd = "July " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.AUGUST) {
            dd = "August " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.SEPTEMBER) {
            dd = "September " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.OCTOBER) {
            dd = "October " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.NOVEMBER) {
            dd = "November " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.DECEMBER) {
            dd = "December " + c.get(Calendar.DAY_OF_MONTH);
        }
        TextView monthTextView = (TextView) findViewById(R.id.Month);
        monthTextView.setText(dd);

        // Set the City
        TextView cityTextView = (TextView) findViewById(R.id.CityText);
        cityTextView.setText(weatherXMLParse.getCurrentWeather().cityName);

        // Set Current Temperature
        TextView currentTemperatureText = (TextView) findViewById(R.id.CurrentTemperatureText);
        currentTemperatureText.setText((int)Math.round(weatherXMLParse.getCurrentWeather().temperature) + " °C");

        // Set Minimum Temperature
        TextView minTemperatureText = (TextView) findViewById(R.id.MinimumTemperatureText);
        minTemperatureText.setText("Low: " + (int)Math.round(weatherXMLParse.getCurrentWeather().minTemp) + " °C");

        // Set Maximum Temperature
        TextView maxTemperatureText = (TextView) findViewById(R.id.MaximumTemperatureText);
        maxTemperatureText.setText("High: " + (int)Math.round(weatherXMLParse.getCurrentWeather().maxTemp) + " °C");

        // Weather Image uses PNG
        ImageView weatherImage = (ImageView) findViewById(R.id.WeatherImage);
        int iconId = WeatherIdIcons.SetWeatherCondition(weatherXMLParse.getCurrentWeather().weatherId, 0);

        // Set Clothing
        pageAdapter.SetClothingForecast(weatherXMLParse.getCurrentWeather());

        if (iconId != 0)
        {
            weatherImage.setImageBitmap(WeatherIdIcons.invertImage(BitmapFactory.decodeResource(getResources(), iconId )));
        }

        // Set Future
        pageAdapter.SetFutureForecaseFragment(futureXMLParse);

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

}
