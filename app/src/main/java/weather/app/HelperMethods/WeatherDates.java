package weather.app.HelperMethods;

import java.util.Calendar;

public class WeatherDates {

    public WeatherDates() {}

    public static String GetWeatherDays() {
        String weekDay = "";

        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "Monday";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "Tuesday";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "Wednesday";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "Thursday";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "Friday";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "Saturday";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "Sunday";
        }
        return weekDay;
    }

    public static String GetWeatherMonths() {

        String dd = "";

        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);

        if (month == Calendar.JANUARY) {
            dd = "January " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.FEBRUARY) {
            dd = "February " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.MARCH) {
            dd = "March " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.APRIL) {
            dd = "April " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.MAY) {
            dd = "May " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.JUNE) {
            dd = "June " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.JULY) {
            dd = "July " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.AUGUST) {
            dd = "August " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.SEPTEMBER) {
            dd = "September " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.OCTOBER) {
            dd = "October " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.NOVEMBER) {
            dd = "November " + c.get(Calendar.DAY_OF_MONTH);
        } else if (month == Calendar.DECEMBER) {
            dd = "December " + c.get(Calendar.DAY_OF_MONTH);
        }

        return dd;
    }


}
