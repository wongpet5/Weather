package weather.app.HelperMethods;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import weather.app.Classes.FutureWeather;
import weather.app.Classes.FutureWeatherList;

public class FutureWeather_XMLParse {

    private String xml = null;
    private FutureWeatherList futureWeatherList ;

    public FutureWeather_XMLParse(String xml)
    {
        this.xml = xml;
        this.futureWeatherList = new FutureWeatherList();
    }

    public FutureWeatherList getFutureWeather()
    {
        return futureWeatherList;
    }

    public void ParseWeatherXML() throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput( new StringReader( xml ) );
        int eventType = xpp.getEventType();

        FutureWeather futureWeather = new FutureWeather();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if(eventType == XmlPullParser.START_DOCUMENT) {}

            else if(eventType == XmlPullParser.START_TAG) {

                if (xpp.getName().equalsIgnoreCase("time")) {

                    futureWeather = new FutureWeather();

                    for (int i = 0; i < xpp.getAttributeCount(); i++)
                    {
                        if (xpp.getAttributeName(i).equalsIgnoreCase("day")) {
                            futureWeather.dayString = xpp.getAttributeValue(i);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                futureWeather.day = dateFormat.parse(xpp.getAttributeValue(i));
                            }
                            catch (ParseException e1) {
                                throw new  RuntimeException(e1);
                            }
                        }
                    }
                }

                if (xpp.getName().equalsIgnoreCase("symbol")) {

                    for (int i = 0; i < xpp.getAttributeCount(); i++)
                    {
                        if (xpp.getAttributeName(i).equalsIgnoreCase("number")) {

                            String temperature = xpp.getAttributeValue(i);
                            try {
                                futureWeather.IconId = NumberFormat.getInstance().parse((temperature)).intValue();
                            } catch (ParseException e1) {
                                throw new  RuntimeException(e1);
                            }
                        }
                    }
                }

                if (xpp.getName().equalsIgnoreCase("temperature")) {

                    for (int i = 0; i < xpp.getAttributeCount(); i++)
                    {
                        if (xpp.getAttributeName(i).equalsIgnoreCase("day")) {

                            String temperature = xpp.getAttributeValue(i);
                            try {
                                futureWeather.temperature = NumberFormat.getInstance().parse((temperature)).doubleValue();
                            } catch (ParseException e1) {
                                throw new  RuntimeException(e1);
                            }
                        }

                        if (xpp.getAttributeName(i).equalsIgnoreCase("min")) {

                            String temperature = xpp.getAttributeValue(i);
                            try {
                                futureWeather.lowTemp = NumberFormat.getInstance().parse((temperature)).doubleValue();
                            } catch (ParseException e1) {
                                throw new  RuntimeException(e1);
                            }
                        }

                        if (xpp.getAttributeName(i).equalsIgnoreCase("max")) {

                            String temperature = xpp.getAttributeValue(i);
                            try {
                                futureWeather.highTemp = NumberFormat.getInstance().parse((temperature)).doubleValue();
                            } catch (ParseException e1) {
                                throw new  RuntimeException(e1);
                            }
                        }
                    }
                }

                if (xpp.getName().equalsIgnoreCase("clouds")) {
                    for (int i = 0; i < xpp.getAttributeCount(); i++)
                    {
                        if (xpp.getAttributeName(i).equalsIgnoreCase("value")) {
                            futureWeather.clouds = xpp.getAttributeValue(i);
                        }
                    }
                }
            }

            else if(eventType == XmlPullParser.END_TAG) {

                if (xpp.getName().equalsIgnoreCase("time")) {
                    this.futureWeatherList.AddToWeatherList(futureWeather);
                }

            }

            eventType = xpp.next();
        }
    }
}
