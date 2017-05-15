package net.couchdev.android.layoutsandbox.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import net.couchdev.android.layoutsandbox.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tim on 26.12.2016.
 */

public class Tools {

    private static Context context;

    public static void init(Context context){
        Tools.context = context;
        Log.d("Tools", "initialized");
    }

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

    public static String[] getSortedCountries(){
        String[] countryArray = context.getResources().getStringArray(R.array.countries);
        Arrays.sort(countryArray);
        return countryArray;
    }
}
