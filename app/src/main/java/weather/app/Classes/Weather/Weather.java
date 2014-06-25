package weather.app.Classes.Weather;

public class Weather {

    public int weatherIconId;

    public double lowTemp;
    public double highTemp;
    public double temperature;

    public String weatherDescription;

    public Weather()
    {
        weatherIconId = 0;
        lowTemp = 0;
        highTemp = 0;
        temperature = 0;
        weatherDescription = "";
    }
}
