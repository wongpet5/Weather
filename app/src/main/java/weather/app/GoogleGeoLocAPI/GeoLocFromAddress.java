package weather.app.GoogleGeoLocAPI;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import weather.app.SQL.CityReaderAdapter;

public class GeoLocFromAddress {

    public GeoLocFromAddress() {}

    public static List<String> getLatLongFromAddress(String youraddress)
    {
        String key = "key=AIzaSyBWsDNVFhhwRpF_x9sfALAJQXpxigEOd9Y";
        String uri = "https://maps.google.com/maps/api/geocode/json?address=" + youraddress.replace(", ", ",+").replace(" ", "%20") + "&sensor=false&" + key;
        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        double lng = 0;
        double lat = 0;
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

            lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("latitude", "" + lat);
            Log.d("longitude", "" + lng);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<String> returnValue = new ArrayList<String>();
        returnValue.add(youraddress);
        returnValue.add(Double.toString(lng));
        returnValue.add(Double.toString(lat));

        return returnValue;
    }

}
