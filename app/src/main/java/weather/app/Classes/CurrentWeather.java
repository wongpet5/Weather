package weather.app.Classes;

import java.util.Date;

public class CurrentWeather {
    public int cityId;
    public String cityName;
    public double longitude;
    public double latitude;
    public String country;
    private Date sunrise;
    private Date sunset;
    public double temperature;
    public double minTemp;
    public double maxTemp;
    private String temperatureUnit;
    public double humidity;
    private String humidityUnit;
    public double pressure;
    private String pressureUnit;
    public String weatherDescription;
    public String weatherIcon;
}
