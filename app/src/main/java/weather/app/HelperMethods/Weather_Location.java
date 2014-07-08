package weather.app.HelperMethods;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;


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
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        LocationManager locationManager = (LocationManager) currentContext.getSystemService(Context.LOCATION_SERVICE);

        currentLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    }

    // If NULL Default Set to location to Toronto
    public double GetLatitude()
    {
        if (currentLocation != null) {
            return currentLocation.getLatitude();
        } else {
            return 43.7;
        }
    }

    // If NULL Default Set to location to Toronto
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
