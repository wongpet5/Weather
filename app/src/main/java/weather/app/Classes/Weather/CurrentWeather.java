package weather.app.Classes.Weather;

import java.util.Date;

public class CurrentWeather extends Weather {

    public Date sunrise;
    public Date sunset;

    public int cityId;

    public double longitude;
    public double latitude;
    public double humidity;
    public double pressure;

    public String cityName;
    public String country;
    public String humidityUnit;
    public String pressureUnit;
    public String temperatureUnit;
    public String weatherIcon;

    public CurrentWeather()
    {
        super();

        sunrise = new Date();
        sunset = new Date();
        cityId = 0;
        longitude = 0;
        latitude = 0;
        humidity = 0;
        pressure = 0;
        cityName = "";
        country = "";
        humidityUnit = "";
        pressureUnit = "";
        temperatureUnit = "";
        weatherIcon = "";
    }

}
