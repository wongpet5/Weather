package weather.app;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;

import java.io.IOException;
import java.util.Calendar;

import weather.app.HelperMethods.Weather_Location;
import weather.app.HelperMethods.Weather_Network;
import weather.app.HelperMethods.Weather_XMLParse;

import org.xmlpull.v1.XmlPullParserException;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

public class TodayFragment extends Fragment {

    private Handler handler;
    private Context context = null;
    private View v = null;
    public TodayFragment(Context ctx)
    {
        this.context = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Initialize the Handler
        handler = new Handler();

        // Retrieving the currently selected item number
        int position = getArguments().getInt("position");

        String url = getArguments().getString("url");
        // List of rivers
        String[] menus = getResources().getStringArray(R.array.menu_array);

        // Creating view corresponding to the fragment
        v = inflater.inflate(R.layout.fragment_today, container, false);

        // Updating the action bar title
        getActivity().getActionBar().setTitle(menus[position]);

        return v;
    }

    @Override
    public void onStart()
    {
        // Call Parent
        super.onStart();

        // Thread to initiate the call to get current location weather information
        startThread();
    }

    // Web Calls cannot be done on the main thread, therefore they will be done on a secondary thread
    protected void startThread() {
        Thread t = new Thread() {
            public void run() {

                // Get current location
                Weather_Location weatherLocation = new Weather_Location(context);

                // Get Current Weather Conditions
                Weather_Network weatherCall = new Weather_Network();

                String weatherXML = weatherCall.DownloadText(weatherLocation.GetLatitude(), weatherLocation.GetLongitude(), 1);
                Log.d("Today Fragment", weatherXML);

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

                // Get Future Weather Conditions
                String weatherJSONFuture = weatherCall.DownloadText(weatherLocation.GetLatitude(), weatherLocation.GetLongitude(), 2);

                Log.d("Future Weather JSON", weatherJSONFuture);

                //Set the values for the Widget Controls
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SetUIControls(weatherXMLParse);
                    }
                });
            }
        };
        t.start();
    }

    private void SetUIControls(Weather_XMLParse weatherXMLParse) {

        // Set the Date
        //TextView dateTextView = (TextView) getActivity().findViewById(R.id.DateTimeText);
        //Calendar c = Calendar.getInstance();
        //dateTextView.setText(c.getTime().toString());

        // Set the Day of the Week
        TextView dateTextView = (TextView) getActivity().findViewById(R.id.Day);
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
        TextView monthTextView = (TextView) getActivity().findViewById(R.id.Month);
        monthTextView.setText(dd);

        // Set the City
        TextView cityTextView = (TextView) getActivity().findViewById(R.id.CityText);
        cityTextView.setText(weatherXMLParse.getCurrentWeather().cityName);

        // Set Current Temperature
        TextView currentTemperatureText = (TextView) getActivity().findViewById(R.id.CurrentTemperatureText);
        currentTemperatureText.setText((int)Math.round(weatherXMLParse.getCurrentWeather().temperature) + " °C");

        // Set Minimum Temperature
        TextView minTemperatureText = (TextView) getActivity().findViewById(R.id.MinimumTemperatureText);
        minTemperatureText.setText("Low: " + (int)Math.round(weatherXMLParse.getCurrentWeather().minTemp) + " °C");

        // Set Maximum Temperature
        TextView maxTemperatureText = (TextView) getActivity().findViewById(R.id.MaximumTemperatureText);
        maxTemperatureText.setText("High: " + (int)Math.round(weatherXMLParse.getCurrentWeather().maxTemp) + " °C");

        // Weather Image uses SVG
        //ImageView weatherImage = (ImageView) getActivity().findViewById(R.id.WeatherImage);
        //SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.cloud);
        //Drawable drawable = svg.createPictureDrawable();
        //weatherImage.setImageDrawable(drawable);

        // Weather Image uses PNG
        ImageView weatherImage = (ImageView) getActivity().findViewById(R.id.WeatherImage);
        weatherImage.setImageResource(R.raw.fair);
    }

}