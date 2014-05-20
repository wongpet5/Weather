package weather.app;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import weather.app.HelperMethods.Weather_Location;
import weather.app.HelperMethods.Weather_Network;
import weather.app.HelperMethods.Weather_XMLParse;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class TodayFragment extends Fragment {

    private Context context = null;
    private View v = null;
    public TodayFragment(Context ctx)
    {
        this.context = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
                Weather_XMLParse weatherXMLParse = new Weather_XMLParse(weatherXML);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Set the Date
                        TextView dateTextView = (TextView) getActivity().findViewById(R.id.DateTimeText);
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());

                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                        String formattedDate = df.format(c.getTime());
                        dateTextView.setText("Date: " + formattedDate);

                        // Set the City
                        TextView cityTextView = (TextView) getActivity().findViewById(R.id.CityText);
                        cityTextView.setText("City: " + "Toronto");

                        // Set Current Temperature

                        // Set Minimum Temperature

                        // Set Maximum Temperature 
                    }
                });
            }
        };
        t.start();
    }

}