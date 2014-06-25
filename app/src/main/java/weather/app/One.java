package weather.app;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import weather.app.Classes.CurrentWeather;
import weather.app.Classes.FutureWeather;
import weather.app.HelperMethods.FutureWeather_XMLParse;
import weather.app.HelperMethods.WeatherIdIcons;

public class One extends Fragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.one, container, false);

    }

    public void SetClothing(CurrentWeather item)
    {

        ImageView weatherImage = (ImageView) getView().findViewById(R.id.clothing);
        int iconId = R.raw.umbrella128;

        if (iconId != 0)
        {
            weatherImage.setImageBitmap(WeatherIdIcons.invertImage(BitmapFactory.decodeResource(getResources(), iconId)));
        }

        ImageView weatherImage2 = (ImageView) getView().findViewById(R.id.clothing2);
        int iconId2 = R.raw.wet128;

        if (iconId2 != 0)
        {
            weatherImage2.setImageBitmap(WeatherIdIcons.invertImage(BitmapFactory.decodeResource(getResources(), iconId2)));
        }
    }


}
