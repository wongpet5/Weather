package weather.app.HelperMethods;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.lang.String;

public class Weather_Location
{
    Context currentContext = null;

    public Weather_Location(Context ctx)
    {
        this.currentContext = ctx;
    }

    public String GetWOEID()
    {
        Location currentLocation = GetLocation();

        double longitude = 0;
        double latitude = 0;

        if (currentLocation != null) {
            longitude = currentLocation.getLongitude();
            latitude = currentLocation.getLatitude();
        } else {
            // Note if you are using the Android Simulator it does not support "GetGPSLocation"
            // As a result the longitude and latitude had to be hardcoded. (This is the location of Toronto)
            longitude = 43.7;
            latitude = -79.4;
        }

        Log.d("Weather_Location_Longitude", Double.toString(longitude));
        Log.d("Weather_Location_Latitude", Double.toString(latitude));

        return null;
    }

    public Location GetLocation()
    {
        String provider = "";
        LocationManager locationManager = (LocationManager) currentContext.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            return null;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return location;
    }
}
