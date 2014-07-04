package weather.app;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weather.app.Classes.City;
import weather.app.Classes.Weather.CurrentWeather;
import weather.app.HelperMethods.FutureWeather_XMLParse;
import weather.app.HelperMethods.WeatherDates;
import weather.app.HelperMethods.WeatherIdIcons;
import weather.app.HelperMethods.Weather_Location;
import weather.app.HelperMethods.Weather_Network;
import weather.app.HelperMethods.Weather_XMLParse;

public class CityListItemAdapter extends BaseAdapter
{
    private List<City> cities;
    private Context context;
    private LayoutInflater layoutInflater;
    private Handler handler;


    public CityListItemAdapter(List<City> _cities, Context context) {
        super();

        this.cities  = _cities;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

        handler = new Handler();
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Citylistitem clt;
        if (convertView == null)
        {
            String city = cities.get(position).city;
            clt = new Citylistitem(context, city);
        }
        else
        {
            clt = (Citylistitem) convertView;
            clt.setNameText(cities.get(position).city);
            clt.setImage(R.raw.airelement32);
        }

        // Set the Temperature
        SetTemperature(clt, cities.get(position));

        return clt;
    }

    private void SetTemperature(Citylistitem clt, City city)
    {
        final Citylistitem clt_final = clt;
        final City city_final = city;

        Thread t = new Thread() {
            public void run() {
                Weather_Location weatherLocation = new Weather_Location(context);
                Weather_Network weatherCall = new Weather_Network();
                String weatherXML = weatherCall.DownloadText(city_final.longitude, city_final.latitude, 1);

                // Parse Current Weather Conditions XML
                final Weather_XMLParse weatherXMLParse = new Weather_XMLParse(weatherXML);
                try {
                    weatherXMLParse.ParseCurrentWeatherXML();
                }
                catch (XmlPullParserException e1) {
                    throw new RuntimeException(e1);
                }
                catch (IOException e2) {
                    throw new RuntimeException(e2);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SetCurrentWeatherControls(weatherXMLParse, clt_final);
                    }
                });
            }
        };
        t.start();
    }

    private void SetCurrentWeatherControls(Weather_XMLParse weatherXMLParse, Citylistitem clt)
    {
        SetCurrentWeatherControls(weatherXMLParse.getCurrentWeather(), clt);
    }

    private void SetCurrentWeatherControls(CurrentWeather currentWeather, Citylistitem clt)
    {
        clt.setTempText(((int) Math.round(currentWeather.temperature) + " °C"));
        int iconId = WeatherIdIcons.SetWeatherCondition(currentWeather.weatherIconId, 0);
        clt.setImage(iconId);
    }
}
