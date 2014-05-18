package weather.app.HelperMethods;

import java.util.Dictionary;
import java.util.Hashtable;

public class Weather_XMLParse
{
    private String xml = null;
    private Hashtable<String, String> parsedXML = new Hashtable<String, String>();

    public Weather_XMLParse(String xml)
    {
        this.xml = xml;
    }


}
