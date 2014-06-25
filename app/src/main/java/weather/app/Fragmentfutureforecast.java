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
import weather.app.HelperMethods.WeatherIdIcons;

public class Fragmentfutureforecast extends Fragment {

    public static final String ARG_OBJECT = "object";

    Hashtable<Integer, Integer> numbers = new Hashtable<Integer, Integer>();
    Hashtable<Integer, Integer> lowerTempHash = new Hashtable<Integer, Integer>();
    Hashtable<Integer, Integer> weatherHash = new Hashtable<Integer, Integer>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        numbers.put(1, R.id.lblDay1);
        numbers.put(2, R.id.lblDay2);
        numbers.put(3, R.id.lblDay3);
        numbers.put(4, R.id.lblDay4);
        numbers.put(5, R.id.lblDay5);
        numbers.put(6, R.id.lblDay6);

        lowerTempHash.put(1, R.id.lblTemp1a);
        lowerTempHash.put(2, R.id.lblTemp2a);
        lowerTempHash.put(3, R.id.lblTemp3a);
        lowerTempHash.put(4, R.id.lblTemp4a);
        lowerTempHash.put(5, R.id.lblTemp5a);
        lowerTempHash.put(6, R.id.lblTemp6a);

        weatherHash.put(1, R.id.WeatherImage1);
        weatherHash.put(2, R.id.WeatherImage2);
        weatherHash.put(3, R.id.WeatherImage3);
        weatherHash.put(4, R.id.WeatherImage4);
        weatherHash.put(5, R.id.WeatherImage5);
        weatherHash.put(6, R.id.WeatherImage6);

        return inflater.inflate(R.layout.fragmentfutureforecast, container, false);
    }

    public void setFragmentControlData(FutureWeather_XMLParse futureXMLParse)
    {
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
        String weekDay = ReturnDayString(dayOfWeek);

        TextView dayOneTextView = (TextView) getView().findViewById(numbers.get(num));
        dayOneTextView.setText(weekDay);
    }

    public void SetAverageTemperature(FutureWeather item, int num) {
        String temp = Long.toString((Math.round(item.lowTemp) + Math.round(item.highTemp))/2);

        TextView dayOneTextView = (TextView) getView().findViewById(lowerTempHash.get(num));
        dayOneTextView.setText(temp + " °C");
    }
    public void SetWeatherImage(FutureWeather item, int num) {
        ImageView weatherImage = (ImageView) getView().findViewById(weatherHash.get(num));
        int iconId = WeatherIdIcons.SetWeatherCondition(item.weatherIconId, 1);

        if (iconId != 0)
        {
            weatherImage.setImageBitmap(WeatherIdIcons.invertImage(BitmapFactory.decodeResource(getResources(), iconId)));
        }
    }

    private String ReturnDayString(int dayOfWeek)
    {
        String weekDay = "";
        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "Monday";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "Tuesday";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "Wednesday";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "Thursday";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "Friday";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "Saturday";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "Sunday";
        }

        return weekDay;
    }

    private String ReturnMonth(int month)
    {
        String dd = "";

        Calendar c = Calendar.getInstance();

        if (month == Calendar.JANUARY) {
            dd = "January " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.FEBRUARY) {
            dd = "February " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.MARCH) {
            dd = "March " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.APRIL) {
            dd = "April " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.MAY) {
            dd = "May " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.JUNE) {
            dd = "June " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.JULY) {
            dd = "July " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.AUGUST) {
            dd = "August " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.SEPTEMBER) {
            dd = "September " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.OCTOBER) {
            dd = "October " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.NOVEMBER) {
            dd = "November " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.DECEMBER) {
            dd = "December " + c.get(Calendar.DAY_OF_MONTH);
        }

        return dd;
    }

}
