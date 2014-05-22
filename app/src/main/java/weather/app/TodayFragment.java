package weather.app;

import android.app.Fragment;
import android.content.Context;
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

                String serverURL = "api.openweathermap.org/data/2.5/weather?lat=" + weatherLocation.GetLatitude() + "&lon=" + weatherLocation.GetLongitude() + "&mode=xml&APPID=cbf2998a2c82e81ab7df43b533bf019c";
                Weather_Network weatherCall = new Weather_Network();

                String weatherXML = weatherCall.DownloadText(serverURL);
                Log.d("Today Fragment", weatherXML);

                // Parse XML
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
        TextView dateTextView = (TextView) getActivity().findViewById(R.id.DateTimeText);
        Calendar c = Calendar.getInstance();
        dateTextView.setText(c.getTime().toString());

        // Set the City
        TextView cityTextView = (TextView) getActivity().findViewById(R.id.CityText);
        cityTextView.setText(weatherXMLParse.getCurrentWeather().cityName);

        // Set Current Temperature
        TextView currentTemperatureText = (TextView) getActivity().findViewById(R.id.CurrentTemperatureText);
        currentTemperatureText.setText("Current: " + weatherXMLParse.getCurrentWeather().temperature + " °C");

        // Set Minimum Temperature
        TextView minTemperatureText = (TextView) getActivity().findViewById(R.id.MinimumTemperatureText);
        minTemperatureText.setText("Min: " + weatherXMLParse.getCurrentWeather().minTemp + " °C");

        // Set Maximum Temperature
        TextView maxTemperatureText = (TextView) getActivity().findViewById(R.id.MaximumTemperatureText);
        maxTemperatureText.setText("Max: " + weatherXMLParse.getCurrentWeather().maxTemp + " °C");

        // Weather Image
        ImageView weatherImage = (ImageView) getActivity().findViewById(R.id.WeatherImage);

        //SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.filename);
        SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.cloud);
        Picture picture = svg.getPicture();
        Drawable drawable = svg.createPictureDrawable();
        weatherImage.setImageDrawable(drawable);
    }

}