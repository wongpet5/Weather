package weather.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import weather.app.Classes.City;
import weather.app.GoogleGeoLocAPI.GeoLocFromAddress;
import weather.app.GooglePlacesAPI.PlaceJSONParser;
import weather.app.SQL.CityReaderAdapter;

public class Citylist extends Activity implements AdapterView.OnItemClickListener {

    private AutoCompleteTextView atvPlaces;
    private PlacesTask placesTask;
    private ParserTask parserTask;

    private CityListItemAdapter cityListAdapter;

    private Handler handler;

    private List<City> cities;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        handler = new Handler();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.citylist);

        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);

        atvPlaces.setOnItemClickListener(this);

        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        SetCityListView();
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyBWsDNVFhhwRpF_x9sfALAJQXpxigEOd9Y";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.select_dialog_item, from, to);


            // Setting the adapter
            // This will set the data
            atvPlaces.setAdapter(adapter);


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Remove Keyboard after a selection is made
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        HashMap<String, String> hm = (HashMap<String, String>) adapterView.getItemAtPosition(position);

        // Use Description to get the Latitude and Longitude
        String description = hm.get("description");

        // Clear the text
        atvPlaces.setText("");

        // Get the Geo Loc - Latitude / Longitude of the city
        GetLocationFromAddress(description);
    }

    private void GetLocationFromAddress(String Description)
    {
        final String description = Description;

        Thread t = new Thread() {

            public void run() {

                final List<String> cityInfo = GeoLocFromAddress.getLatLongFromAddress(description);

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        AddCityToDatabase(cityInfo);
                    }
                });
            }
        };
        t.start();
    }

    public void AddCityToDatabase(List<String> city)
    {
        if (city != null && city.size() == 3 && cityListAdapter != null)
        {
            CityReaderAdapter db = new CityReaderAdapter(this);
            db.open();

            long id = db.insertContact(city.get(0), city.get(1), city.get(2));
            db.close();

            City cityItem = new City();
            cityItem.city = city.get(0);
            cityItem.latitude = Double.parseDouble(city.get(1));
            cityItem.longitude = Double.parseDouble(city.get(2));
            cityItem.id = id;

            cityListAdapter.Add(cityItem);
            cityListAdapter.notifyDataSetChanged();
        }
    }

    private void SetCityListView()
    {
        CityReaderAdapter db = new CityReaderAdapter(this);
        db.open();

        //citiesNames = new ArrayList<String>();
        cities = new ArrayList<City>();

        Cursor citiesCursor = db.GetAllCities();
        while (citiesCursor.moveToNext())
        {
            long id = Long.parseLong(citiesCursor.getString(citiesCursor.getColumnIndexOrThrow(CityReaderAdapter.KEY_ROWID)));
            double _lat = Double.parseDouble(citiesCursor.getString(citiesCursor.getColumnIndexOrThrow(CityReaderAdapter.KEY_LAT)));
            double _lng = Double.parseDouble(citiesCursor.getString(citiesCursor.getColumnIndexOrThrow(CityReaderAdapter.KEY_LONG)));
            String _city = citiesCursor.getString(citiesCursor.getColumnIndexOrThrow(CityReaderAdapter.KEY_NAME));

            City newCity = new City();
            newCity.id = id;
            newCity.city = _city;
            newCity.latitude = _lat;
            newCity.longitude = _lng;

            cities.add(newCity);
        }

        db.close();

        ListView CityList = (ListView) findViewById(R.id.city_list);

        CityList.setChoiceMode(ListView.CHOICE_MODE_NONE);
        CityList.setTextFilterEnabled(true);

        cityListAdapter = new CityListItemAdapter(cities, this);
        CityList.setAdapter(cityListAdapter);

        CityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent =  new Intent(getBaseContext(), CityWeatherConditionsActivity.class);

                Bundle extra = new Bundle();
                extra.putString("City", cities.get((int)id).city.toString());
                extra.putDouble("Latitude", cities.get((int)id).latitude);
                extra.putDouble("Longitude", cities.get((int)id).longitude);

                intent.putExtras(extra);

                startActivity(intent);
            }
        });

        // Delete A city from the Item List
        SwipeDismissListViewTouchListener touchListener =
            new SwipeDismissListViewTouchListener(
                CityList,
                new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        for (int position : reverseSortedPositions) {

                            // Get the city to delete
                            City cityItem = (City)cityListAdapter.getItem((int)position);

                            // Remove city from UI
                            cityListAdapter.Remove(cityListAdapter.getItemId(position));

                            // Remove City for Database
                            CityReaderAdapter db = new CityReaderAdapter(getApplicationContext());
                            db.open();

                            db.removeContact(cityItem.id);
                            db.close();

                        }
                        cityListAdapter.notifyDataSetChanged();
                    }
                });

        CityList.setOnTouchListener(touchListener);

    }

}
