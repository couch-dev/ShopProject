package net.couchdev.android.layoutsandbox.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import net.couchdev.android.layoutsandbox.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;

/**
 * Created by Tim on 15.05.2017.
 */

public class FileTools {

    private static final String LOG_TAG = FileTools.class.getSimpleName();
    private static final String FILENAME = "settings";
    private static Context context;
    private static HashMap<String, Integer> unreadMessages;

    public static void init(Context context){
        FileTools.context = context;
        File path = new File(context.getExternalFilesDir(null), "profile");
        path.mkdirs();
        path = new File(context.getFilesDir(), "tmp");
        path.mkdirs();
        if(!loadSettings()){
            unreadMessages = new HashMap<>();
        }
        Log.d(LOG_TAG, "initialized");
    }

    public static HashMap<String, Integer> getUnreadMessages() {
        return unreadMessages;
    }

    public static void setUnreadMessages(HashMap<String, Integer> unreadMessages) {
        FileTools.unreadMessages = unreadMessages;
    }

    private static void saveSettings(){
        try {
            File file = new File(context.getFilesDir(), FILENAME);
            FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(unreadMessages);
        } catch (Exception e){
            Log.e(LOG_TAG, "saveSettings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean loadSettings(){
        try {
            File file = new File(context.getFilesDir(), FILENAME);
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(in);
            unreadMessages = (HashMap<String, Integer>) ois.readObject();
            return true;
        } catch (Exception e){
            Log.e(LOG_TAG, "saveSettings: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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
                try{
                    long l = Long.parseLong(children[i].replace("profile", "").replace(".jpg", ""));
                    if(l > max){
                        max = l;
                        picIndex = i;
                    }
                } catch (Exception e){
                    Log.e(LOG_TAG, "getProfilePic: " + e.getMessage() + "\n" + e.getStackTrace());
                    return null;
                }
            }
            if(picIndex >= 0){
                File file = new File(dir, children[picIndex]);
                Log.d(LOG_TAG, "loaded profile pic: " + file.getAbsolutePath());
                return BitmapFactory.decodeFile(file.getPath());
            } else{
                Log.e(LOG_TAG, "getProfilePic: no profile pics found!");
            }
        } else{
            Log.e(LOG_TAG, "getProfilePic: no such directory: " + dir.getAbsolutePath());
        }
        return null;
    }

    public static boolean saveAsProfilePic(Bitmap bitmap){
        File path = new File(context.getExternalFilesDir(null), "profile");
        File dstFile = new File(path, FileTools.getUniqueProfilePicName());
        try {
            FileOutputStream out = new FileOutputStream(dstFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Log.d(LOG_TAG, "profile pic saved: " + dstFile.getAbsolutePath());
            return true;
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "saveAsProfilePic: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static String getUniqueProfilePicName(){
        return "profile" + (System.currentTimeMillis()/100) + ".jpg";
    }

    public static String getImageName(){
        return "image.jpg";
    }

    private static String getBitmapName(){
        return "bitmap.jpg";
    }

    public static File createTmpFile(Bitmap bitmap){
        try {
            final File tmpPath = new File(context.getFilesDir(), "tmp");
            final String filename = getBitmapName();
            final File tmpImage = new File(tmpPath, filename);
            FileOutputStream out = new FileOutputStream(tmpImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Log.d(LOG_TAG, "created tmp file: " + tmpImage.getAbsolutePath());
            return tmpImage;
        } catch(Exception e){
            Log.e(LOG_TAG, "createTmpFile: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getTmpBitmap(){
        File dir = new File(context.getFilesDir(), "tmp");
        if (dir.isDirectory())
        {
            File file = new File(dir, getBitmapName());
            Bitmap result = BitmapFactory.decodeFile(file.getPath());
            deleteTmpFiles();
            return result;
        }
        return null;
    }

    public static void deleteTmpFiles(){
        File dir = new File(context.getExternalFilesDir(null), "tmp");
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                Log.d(LOG_TAG, "deleteTmpFiles: " + children[i] + " deleted: " + new File(dir, children[i]).delete());
            }
        }
    }

}
