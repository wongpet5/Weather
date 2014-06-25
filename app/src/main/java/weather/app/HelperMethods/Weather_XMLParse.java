package weather.app.HelperMethods;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.NumberFormat;
import java.text.ParseException;

import weather.app.Classes.Weather.CurrentWeather;


public class Weather_XMLParse
{
    private String xml = null;
    private CurrentWeather currentWeather;

    public Weather_XMLParse(String xml)
    {
        this.xml = xml;
        this.currentWeather = new CurrentWeather();
    }

    public CurrentWeather getCurrentWeather()
    {
        return currentWeather;
    }

    public void ParseCurrentWeatherXML() throws XmlPullParserException, IOException
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput( new StringReader( xml ) );
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            if(eventType == XmlPullParser.START_DOCUMENT) {

            } else if(eventType == XmlPullParser.START_TAG) {

                if (xpp.getName().equalsIgnoreCase("city"))
                {
                    for (int i = 0; i < xpp.getAttributeCount(); i++)
                    {
                        if (xpp.getAttributeName(i).equalsIgnoreCase("id")) {
                            String cityId = xpp.getAttributeValue(i);
                            try {
                                currentWeather.cityId = ((Number) NumberFormat.getInstance().parse(cityId)).intValue() ;
                            } catch (ParseException e1) {
                                throw new RuntimeException(e1);
                            }
                        }

                        if (xpp.getAttributeName(i).equalsIgnoreCase("name")) {
                            currentWeather.cityName = xpp.getAttributeValue(i);
                        }
                    }
                }

                if (xpp.getName().equalsIgnoreCase("coord"))
                {
                    for (int i = 0; i < xpp.getAttributeCount(); i++)
                    {
                        if (xpp.getAttributeName(i).equalsIgnoreCase("lon")) {
                            String cityId = xpp.getAttributeValue(i);
                            try {
                                currentWeather.longitude = ((Number) NumberFormat.getInstance().parse(cityId)).doubleValue() ;
                            } catch (ParseException e1) {
                                throw new RuntimeException(e1);
                            }
                        }

                        if (xpp.getAttributeName(i).equalsIgnoreCase("lat")) {
                            String cityId = xpp.getAttributeValue(i);
                            try {
                                currentWeather.latitude = ((Number) NumberFormat.getInstance().parse(cityId)).doubleValue() ;
                            } catch (ParseException e2) {
                                throw new RuntimeException(e2);
                            }
                        }
                    }
                }

                if (xpp.getName().equalsIgnoreCase("country"))
                {
                    //eventType = xpp.next();
                    //currentWeather.country = xpp.getText();
                }

                if (xpp.getName().equalsIgnoreCase("sun rise"))
                {
                    //SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");
                    //String dateInString = "Friday, Jun 7, 2013 12:10:56 PM";
                    //currentWeather.sunrise = xpp.getText();
                }

                if (xpp.getName().equalsIgnoreCase("temperature"))
                {
                    for (int i = 0; i < xpp.getAttributeCount(); i++)
                    {
                        if (xpp.getAttributeName(i).equalsIgnoreCase("value")) {
                            String temperature = xpp.getAttributeValue(i);
                            try {
                                currentWeather.temperature = ((Number) NumberFormat.getInstance().parse(temperature)).doubleValue() ;
                            } catch (ParseException e1) {
                                throw new RuntimeException(e1);
                            }
                        }

                        if (xpp.getAttributeName(i).equalsIgnoreCase("min")) {
                            String minTemp = xpp.getAttributeValue(i);
                            try {
                                currentWeather.lowTemp = ((Number) NumberFormat.getInstance().parse(minTemp)).doubleValue() ;
                            } catch (ParseException e2) {
                                throw new RuntimeException(e2);
                            }
                        }

                        if (xpp.getAttributeName(i).equalsIgnoreCase("max")) {
                            String maxTemp = xpp.getAttributeValue(i);
                            try {
                                currentWeather.highTemp = ((Number) NumberFormat.getInstance().parse(maxTemp)).doubleValue() ;
                            } catch (ParseException e2) {
                                throw new RuntimeException(e2);
                            }
                        }
                    }
                }

                if (xpp.getName().equalsIgnoreCase("humidity"))
                {
                    for (int i = 0; i < xpp.getAttributeCount(); i++)
                    {
                        if (xpp.getAttributeName(i).equalsIgnoreCase("value")) {
                            String humidity = xpp.getAttributeValue(i);
                            try {
                                currentWeather.humidity = ((Number) NumberFormat.getInstance().parse(humidity)).doubleValue();
                            } catch (ParseException e1) {
                                throw new RuntimeException(e1);
                            }
                        }
                    }
                }

                if (xpp.getName().equalsIgnoreCase("pressure"))
                {
                    for (int i = 0; i < xpp.getAttributeCount(); i++)
                    {
                        if (xpp.getAttributeName(i).equalsIgnoreCase("value")) {
                            String pressure = xpp.getAttributeValue(i);
                            try {
                                currentWeather.pressure = ((Number) NumberFormat.getInstance().parse(pressure)).doubleValue();
                            } catch (ParseException e1) {
                                throw new RuntimeException(e1);
                            }
                        }
                    }
                }

                if (xpp.getName().equalsIgnoreCase("weather"))
                {
                    for (int i = 0; i < xpp.getAttributeCount(); i++)
                    {
                        if (xpp.getAttributeName(i).equalsIgnoreCase("number")) {
                            String weatherId = xpp.getAttributeValue(i);

                            try {
                                currentWeather.weatherIconId = ((Number) NumberFormat.getInstance().parse(weatherId)).intValue() ;
                            } catch (ParseException e1) {
                                throw new RuntimeException(e1);
                            }

                        }

                        if (xpp.getAttributeName(i).equalsIgnoreCase("value")) {
                            currentWeather.weatherDescription = xpp.getAttributeValue(i);
                        }

                        if (xpp.getAttributeName(i).equalsIgnoreCase("icon")) {
                            currentWeather.weatherIcon = xpp.getAttributeValue(i);
                        }
                    }
                }

            } else if(eventType == XmlPullParser.END_TAG) {
                //System.out.println("End tag "+xpp.getName());
            } else if(eventType == XmlPullParser.TEXT) {
                //System.out.println("Text "+xpp.getText());
            }
            eventType = xpp.next();
        }

    }

}
