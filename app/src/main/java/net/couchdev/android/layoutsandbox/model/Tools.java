package net.couchdev.android.layoutsandbox.model;

import java.util.List;

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
}
