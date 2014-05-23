package weather.app.HelperMethods;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class Weather_Network {

    public Weather_Network()
    {

    }

    // mode 1 = current
    // mode 2 = future
    public String DownloadText(double latitude, double longitude, int mode)
    {
        int BUFFER_SIZE = 4000;
        InputStream in = null;
        try {
            in = OpenHTTPConnection(latitude, longitude, mode);
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer))>0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }
        return str;
    }

    private InputStream OpenHTTPConnection(double latitude, double longitude, int mode) throws IOException
    {
        InputStream in = null;
        int response = -1;

        try {

            //
            String query = "";
            String host = "";

            if (mode == 1)
            {
                query = "lat=" + latitude + "&lon=" + longitude + "&units=metric&mode=xml&APPID=cbf2998a2c82e81ab7df43b533bf019c";
                host = "/data/2.5/weather";
            }
            else // if (mode == 2)
            {
                query = "lat=" + latitude + "&lon=" + longitude + "&cnt=10&mode=json&APPID=cbf2998a2c82e81ab7df43b533bf019c";
                host = "/data/2.5/forecast";
            }

            URI uri = new URI("http", "api.openweathermap.org", host, query, null);
            Log.d("WeatherURL", uri.toASCIIString());
            URL url = new URL(uri.toASCIIString());

            URLConnection conn = url.openConnection();

            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");
            try {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }
            }
            catch (Exception ex) {
                Log.d("Networking", ex.getLocalizedMessage());
                throw new IOException("Error connecting");
            }
        }
        catch (URISyntaxException e)
        {
            Log.d("URI EXCEPTION", "Invalid URI", e);
        }

        return in;
    }

}
