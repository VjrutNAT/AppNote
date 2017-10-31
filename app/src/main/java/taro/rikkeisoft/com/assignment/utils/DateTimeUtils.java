package taro.rikkeisoft.com.assignment.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

/**
 * Created by VjrutNAT on 10/29/2017.
 */

public class DateTimeUtils {

    public static String getCurrentDayOfWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
        Date d = new Date();
        return sdf.format(d);
    }


    public static String getDateStrFromMilliseconds(long dateMillis, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(new Date(dateMillis));
    }

    public static long parseStrDateTimeToMills(String srtDateTime, String dateTimeFormat){

        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.getDefault());
        long millis = 0;
        try {
            Date d = sdf.parse(srtDateTime);
            millis = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
     return millis;
    }

    public static String getCurrentDateTimeInStr(String strFormat){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(strFormat, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}
