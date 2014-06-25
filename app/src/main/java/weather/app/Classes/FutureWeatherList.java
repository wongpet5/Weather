package weather.app.Classes;

import java.util.ArrayList;
import java.util.List;

import weather.app.Classes.Weather.FutureWeather;

public class FutureWeatherList {

    private List<FutureWeather> weatherList;

    public FutureWeatherList() {
        this.weatherList = new ArrayList<FutureWeather>();
    }

    public void AddToWeatherList(FutureWeather item){
        weatherList.add(item);
    }

    public List<FutureWeather> GetWeatherList() { return weatherList; }

    public FutureWeather GetItem(int i)
    {
        if (i < (weatherList.size()))
            return  weatherList.get(i);
        else
            return null;
    }

}
