package weather.app.Classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FutureWeatherList {

    private List<FutureWeather> weatherList;

    public FutureWeatherList() {
        this.weatherList = new ArrayList<FutureWeather>();
    }

    public void AddToWeatherList(FutureWeather item){
        weatherList.add(item);
    }

    public FutureWeather GetItem(int i)
    {
        if (i < (weatherList.size()))
            return  weatherList.get(i);
        else
            return null;
    }

}
