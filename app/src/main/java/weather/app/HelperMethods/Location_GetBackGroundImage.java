package weather.app.HelperMethods;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import weather.app.Classes.Panoramas;
import weather.app.Classes.PanoramasMapLocation;
import weather.app.Classes.PanoramasPhoto;

public class Location_GetBackGroundImage
{
    public Panoramas panoramas = new Panoramas();
    public List<PanoramasPhoto> panoramasPhotos = new ArrayList<PanoramasPhoto>();

    public Location_GetBackGroundImage()
    { }

    public void DownloadText(double latitude, double longitude, int mode) throws UnsupportedEncodingException, IOException, ParseException
    {
        int BUFFER_SIZE = 4000;
        InputStream in = null;
        try {
            in = OpenHTTPConnection(latitude, longitude, mode);
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
        }

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        // Check against null

        reader.beginObject();
        while (reader.hasNext()) {

            String name = reader.nextName();

            if (name.equals("count")) {
                panoramas.count = reader.nextInt();
            }

            if (name.equals("has_more")) {
                panoramas.hasMore = reader.nextBoolean();
            }

            if (name.equals("map_location")) {
                panoramas.panoromasMapLocation = GetMapLocation(reader);
            }

            if (name.equals("photos")) {
                panoramas.panomarasPhotos = GetPanoramasPhotos(reader);
            }
        }
        reader.endObject();
    }

    public PanoramasMapLocation GetMapLocation(JsonReader reader) throws IOException
    {
        PanoramasMapLocation panoramasMapLocation = new PanoramasMapLocation();

        reader.beginObject();
        while (reader.hasNext()) {

            String name = reader.nextName();

            if (name.equals("lat")) {
                panoramasMapLocation.latitude = reader.nextDouble();
            }
            if (name.equals("lon")) {
                panoramasMapLocation.longitude = reader.nextDouble();
            }
            if (name.equals("panoramio_zoom")) {
                panoramasMapLocation.panoromaZoom = reader.nextDouble();
            }
        }

        reader.endObject();
        return panoramasMapLocation;
    }

    public List<PanoramasPhoto> GetPanoramasPhotos(JsonReader reader) throws IOException//, ParseException
    {
        List<PanoramasPhoto> photos = new ArrayList<PanoramasPhoto>();

        reader.beginArray();
        while (reader.hasNext()) {


            //String name = reader.nextName();
            photos.add(GetPanoramasPhoto(reader));

        }

        reader.endArray();
        return photos;
    }

    public PanoramasPhoto GetPanoramasPhoto(JsonReader reader) throws IOException//, ParseException
    {
        PanoramasPhoto photo = new PanoramasPhoto();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if (name.equals("height")) {
                photo.height = reader.nextDouble();
            }
            else if (name.equals("latitude")) {
                photo.latitude = reader.nextDouble();
            }
            else if (name.equals("longitude")) {
                photo.longitude = reader.nextDouble();
            }
            else if (name.equals("owner_id")) {
                photo.owner_id = reader.nextInt();
            }
            else if (name.equals("owner_name")) {
                photo.owner_name = reader.nextString();
            }
            else if (name.equals("owner_url")) {
                photo.owner_url = reader.nextString();
            }
            else if (name.equals("photo_file_url")) {
                photo.photo_file_url = reader.nextString();
            }
            else if (name.equals("photo_id")){
                photo.photo_id = reader.nextString();
            }
            else if (name.equals("photo_title")) {
                photo.photo_title = reader.nextString();
            }
            else if (name.equals("photo_url")) {
                photo.photo_url = reader.nextString();
            }
            else if (name.equals("place_id")){
                reader.nextString();
            }
            else if (name.equals("upload_date")) {
                reader.nextString();
            }
            else if (name.equals("width")) {
                photo.width = reader.nextDouble();
                break;
            }
        }

        reader.endObject();
        return photo;
    }
                                          // Y            // X
    private InputStream OpenHTTPConnection(double latitude, double longitude, int mode) throws IOException
    {
        InputStream in = null;
        int response = -1;

        try {

            //
            String query = "";
            String host = "";
            //set=public&from=0&to=20&minx=-180&miny=-90&maxx=180&maxy=90&size=medium&mapfilter=true
            query = "set=public" + "&from=0&to=5" + "&minx=" + (longitude-3) + "&miny=" + (latitude-3) + "&maxx=" + (longitude+3) + "&maxy=" + (latitude+3) + "&size=medium&mapfilter=false";
            host = "/map/get_panoramas.php?";

            URI uri = new URI("http", "www.panoramio.com", host, query, null);
            URL url = new URL(uri.toASCIIString().replace("%3F", ""));

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

    public void GetURL() {

    }

}
