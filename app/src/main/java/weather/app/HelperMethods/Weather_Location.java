package weather.app.HelperMethods;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.lang.String;

public class Weather_Location
{
    private Context currentContext = null;
    private Location currentLocation = null;

    public Weather_Location(Context ctx)
    {
        this.currentContext = ctx;
        SetLocation();
    }

    private void SetLocation()
    {
        String provider = "";
        LocationManager locationManager = (LocationManager) currentContext.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            currentLocation = null;
        }

        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public double GetLatitude()
    {
        if (currentLocation != null) {
            return currentLocation.getLatitude();
        } else {
            return 43.7;
        }
    }

    public double GetLongitude()
    {
        if (currentLocation != null) {
            return currentLocation.getLongitude();
        } else {
            return -79.4;
        }
    }

    public Location GetLocation()
    {
        return currentLocation;
    }
}
