package net.couchdev.android.layoutsandbox.model;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tim on 26.12.2016.
 */

public class Tools {

    public static <T> String enumerate(List<T> list, String separator){
        String result = "";

        for(T item: list){
            result += item + separator;
        }
        result = result.substring(0, result.length()-separator.length());

        return result;
    }

    public static String dateToString(Calendar calendar){
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);

        return day + ". " + month + " " + year;
    }
}
