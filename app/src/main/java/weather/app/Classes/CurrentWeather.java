package weather.app.Classes;

import java.util.Date;

public class CurrentWeather {
    private int cityId;
    private String cityName;
    private double longitude;
    private double latitude;
    private String country;
    private Date sunrise;
    private Date sunset;
    private double temperature;
    private double minTemp;
    private double maxTemp;
    private String temperatureUnit;

}

/*
<current>
    <city id="6167863" name="Downtown Toronto">
    <coord lon="-79.4" lat="43.7"/>
    <country>CA</country>
    <sun rise="2014-05-18T09:48:27" set="2014-05-19T00:39:39"/>
    </city>
    <temperature value="8.65" min="7" max="10" unit="celsius"/>
    <humidity value="67" unit="%"/>
    <pressure value="1021" unit="hPa"/>
    <wind>
    <speed value="1.03" name="Calm"/>
    <direction value="221" code="SW" name="Southwest"/>
    </wind>
    <clouds value="0" name="sky is clear"/>
    <precipitation mode="no"/>
    <weather number="800" value="Sky is Clear" icon="01n"/>
    <lastupdate value="2014-05-18T02:19:05"/>
    </current>

 */