package weather.app.HelperMethods;

import android.graphics.Bitmap;
import android.graphics.Color;

import weather.app.R;

public class WeatherIdIcons
{
    public WeatherIdIcons() { }

    // 0 = large size
    // 1 = small size
    public static int SetWeatherCondition(int WeatherId, int size)
    {
        int iconId = 0;

        String monthString;
        switch (WeatherId) {

            // Thunder Storm
            case 200:
            case 201:
            case 202:
            case 210:
            case 211:
            case 212:
            case 221:
            case 230:
            case 231:
                if (size == 0)
                    iconId = R.raw.storm32;
                else if (size == 1)
                    iconId = R.raw.storm75;
                break;

            // Light Drizzle
            case 300:
            case 301:
            case 302:
            case 310:
            case 311:
            case 312:
            case 313:
            case 314:
            case 321:
                if (size == 0)
                    iconId = R.raw.littlerain32;
                else if (size == 1)
                    iconId = R.raw.littlerain75;
                break;

            // Rain
            case 500:
            case 501:
                if (size == 0)
                    iconId = R.raw.littlerain32;
                else if (size == 1)
                    iconId = R.raw.littlerain75;
                break;

            // Heavy Rain
            case 502:
            case 503:
            case 504:
                if (size == 0)
                    iconId = R.raw.downpour32;
                else if (size == 1)
                    iconId = R.raw.downpour75;
                break;

            // Freezing Rain
            case 511:
                if (size == 0)
                    iconId = R.raw.sleet32;
                else if (size == 1)
                    iconId = R.raw.sleet75;
                break;

            // Rain
            case 520:
            case 521:
            case 522:
            case 531:
                if (size == 0)
                    iconId = R.raw.rain32;
                else if (size == 1)
                    iconId = R.raw.rain75;
                break;


            // Light Snow
            case 600:
                if (size == 0)
                    iconId = R.raw.littlesnow32;
                else if (size == 1)
                    iconId = R.raw.littlesnow75;
                break;

            // Snow
            case 601:
            case 602:
                if (size == 0)
                    iconId = R.raw.snow32;
                else if (size == 1)
                    iconId = R.raw.snow75;
                break;

            // Sleet
            case 611:
            case 612:
            case 615:
            case 616:
            case 620:
            case 621:
            case 622:
                if (size == 0)
                    iconId = R.raw.sleet32;
                else if (size == 1)
                    iconId = R.raw.sleet75;
                break;

            // Snow Storm

            // Atmosphere
            case 701:
            case 711:
            case 721:
            case 731:
            case 741:
            case 751:
            case 761:
            case 762:
            case 771:
            case 781:
                if (size == 0)
                    iconId = R.raw.airelement32;
                else if (size == 1)
                    iconId = R.raw.airelement75;
                break;

            // Clear Sky
            // To do: Fix if it is Night or Day Time
            case 800:
                if (size == 0)
                    iconId = R.raw.sun32;
                    //iconId = R.raw.moon32;
                else if (size == 1)
                    iconId = R.raw.sun75;
                    //iconId = R.raw.moon75;
                break;

            // Scattered Clouds
            // To do: Fix if it is Night or Day Time
            case 801:
                if (size == 0)
                    iconId = R.raw.partlycloudyday32;
                    //iconId = R.raw.partlycloudynight32;
                else if (size == 1)
                    iconId = R.raw.partlycloudyday75;
                //iconId = R.raw.partlycloudynight75;
                break;

            // Clouds
            case 802:
                if (size == 0)
                    iconId = R.raw.clouds32;
                else if (size == 1)
                    iconId = R.raw.clouds75;
                break;

            // Broken Clouds
            // OverCast Clouds
            case 803:
            case 804:
                if (size == 0)
                    iconId = R.raw.skydrivecopyrighted32;
                else if (size == 1)
                    iconId = R.raw.skydrivecopyrighted75;
                break;

            default:
                break;
        }

        return iconId;
    }

    public static Bitmap invertImage(Bitmap src) {
        // create new bitmap with the same attributes(width,height)
        //as source bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // color info
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();

        // scan through every pixel
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final bitmap
        return bmOut;
    }
}
