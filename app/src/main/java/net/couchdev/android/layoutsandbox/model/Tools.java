package net.couchdev.android.layoutsandbox.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tim on 26.12.2016.
 */

public class Tools {

    public static Context context;

    public static void init(Context context){
        Tools.context = context;
        File path = new File(context.getExternalFilesDir(null), "tmp");
        path.mkdirs();
        path = new File(context.getExternalFilesDir(null), "profile");
        path.mkdirs();
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

    public static Bitmap getProfilePic(){
        File dir = new File(context.getExternalFilesDir(null), "profile");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            int picIndex = -1;
            long max = 0;
            for (int i = 0; i < children.length; i++)
            {
                long l = Long.parseLong(children[i].replace("profile", "").replace(".png", ""));
                if(l > max){
                    max = l;
                    picIndex = i;
                }
            }
            if(picIndex >= 0){
                File file = new File(dir, children[picIndex]);
                return BitmapFactory.decodeFile(file.getPath());
            }
        }
        return null;
    }

    public static String getUniqueProfilePicName(){
        return "profile" + (System.currentTimeMillis()/1000) + ".png";
    }

    public static String getUniqueImageName(){
        return "image" + (System.currentTimeMillis()/1000) + ".jpg";
    }

    private static String getUniqueBitmapName(){
        return "bitmap" + (System.currentTimeMillis()/1000) + ".png";
    }

    public static File createTmpFile(Bitmap bitmap){
        try {
            final File tmpPath = new File(context.getFilesDir(), "tmp");
            final String filename = getUniqueBitmapName();
            final File tmpImage = new File(tmpPath, filename);
            FileOutputStream out = new FileOutputStream(tmpImage);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return tmpImage;
        } catch(Exception e){
            Log.e("Tools", "createTmpFile: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void copyFile(File srcFile, File dstFile){
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try
        {
            inChannel = new FileInputStream(srcFile).getChannel();
            outChannel = new FileOutputStream(dstFile).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (Exception e){
            Log.e("Tools", "moveFile: " + e.getMessage());
        }
        finally
        {
            try {
                if (inChannel != null)
                    inChannel.close();
                if (outChannel != null)
                    outChannel.close();
            } catch (Exception e){
                Log.e("Tools", "moveFile: " + e.getMessage());
            }
        }
    }

    public static void deleteTmpFiles(){
        File dir = new File(context.getExternalFilesDir(null), "tmp");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                Log.d("Tools", "deleteTmpFiles: " + children[i] + " deleted: " + new File(dir, children[i]).delete());
            }
        }
    }

}
