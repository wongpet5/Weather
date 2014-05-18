package weather.app;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

import weather.app.HelperMethods.Weather_Location;
import weather.app.HelperMethods.Weather_Network;
import weather.app.HelperMethods.Weather_XMLParse;


public class TodayFragment extends Fragment {

    private Context context = null;

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
        View v = inflater.inflate(R.layout.fragment_today, container, false);

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

            }
        };
        t.start();
    }

}