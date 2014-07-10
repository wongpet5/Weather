package weather.app;

import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Hashtable;


import weather.app.Classes.Weather.FutureWeather;
import weather.app.HelperMethods.FutureWeather_XMLParse;
import weather.app.HelperMethods.WeatherDates;
import weather.app.HelperMethods.WeatherIdIcons;

public class Fragmentfutureforecast extends Fragment {

    public static final String ARG_OBJECT = "object";

    public Hashtable<Integer, Integer> numbers;
    public Hashtable<Integer, Integer> lowerTempHash;
    public Hashtable<Integer, Integer> weatherHash;
    private View _view;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        InitializeHashTables();

        _view = inflater.inflate(R.layout.fragmentfutureforecast, container, false);
        return _view;
    }

    public void InitializeHashTables()
    {
        if (numbers == null)
        {
            numbers = new Hashtable<Integer, Integer>();
            numbers.put(1, R.id.lblDay1);
            numbers.put(2, R.id.lblDay2);
            numbers.put(3, R.id.lblDay3);
            numbers.put(4, R.id.lblDay4);
            numbers.put(5, R.id.lblDay5);
            numbers.put(6, R.id.lblDay6);
        }

        if (lowerTempHash == null)
        {
            lowerTempHash =  new Hashtable<Integer, Integer>();
            lowerTempHash.put(1, R.id.lblTemp1a);
            lowerTempHash.put(2, R.id.lblTemp2a);
            lowerTempHash.put(3, R.id.lblTemp3a);
            lowerTempHash.put(4, R.id.lblTemp4a);
            lowerTempHash.put(5, R.id.lblTemp5a);
            lowerTempHash.put(6, R.id.lblTemp6a);
        }

        if (weatherHash == null)
        {
            weatherHash = new Hashtable<Integer, Integer>();
            weatherHash.put(1, R.id.WeatherImage1);
            weatherHash.put(2, R.id.WeatherImage2);
            weatherHash.put(3, R.id.WeatherImage3);
            weatherHash.put(4, R.id.WeatherImage4);
            weatherHash.put(5, R.id.WeatherImage5);
            weatherHash.put(6, R.id.WeatherImage6);
        }
    }

    public void setFragmentControlData(FutureWeather_XMLParse futureXMLParse)
    {
        InitializeHashTables();

        for (int i = 1; i <= 6; i++)
        {
            SetDay(futureXMLParse.getFutureWeather().GetItem(i), i);
            SetAverageTemperature(futureXMLParse.getFutureWeather().GetItem(i), i);
            SetWeatherImage(futureXMLParse.getFutureWeather().GetItem(i), i);
        }
    }

    public void SetDay(FutureWeather item, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(item.day);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String weekDay = WeatherDates.GetWeatherDays(dayOfWeek);

        if (getView() != null) {
            TextView dayOneTextView = (TextView) getView().findViewById(numbers.get(num));
            dayOneTextView.setText(weekDay);
        }
        else {
            TextView dayOneTextView = (TextView) _view.findViewById(numbers.get(num));
            dayOneTextView.setText(weekDay);
        }
    }

    public void SetAverageTemperature(FutureWeather item, int num) {
        String temp = Long.toString((Math.round(item.lowTemp) + Math.round(item.highTemp))/2);

        TextView dayOneTextView = (TextView) _view.findViewById(lowerTempHash.get(num));
        dayOneTextView.setText(temp + " °C");
    }
    public void SetWeatherImage(FutureWeather item, int num) {
        ImageView weatherImage = (ImageView) _view.findViewById(weatherHash.get(num));
        int iconId = WeatherIdIcons.SetWeatherCondition(item.weatherIconId, 1);

        if (iconId != 0)
        {
            //weatherImage.setImageBitmap(WeatherIdIcons.invertImage(BitmapFactory.decodeResource(getResources(), iconId)));
            weatherImage.setImageResource(iconId);
        }
    }

}
