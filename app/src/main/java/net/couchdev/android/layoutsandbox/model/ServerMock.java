package net.couchdev.android.layoutsandbox.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import net.couchdev.android.layoutsandbox.controller.ChatActivity;

import java.util.ArrayList;

/**
 * Created by Tim on 28.12.2016.
 */

public class ServerMock extends SQLiteOpenHelper {

    public static final String INVALID_LOGIN = "1";
    public static final String INVALID_PASS = "2";

    public static ServerMock instance;
    private static final String DB_NAME = "server_db";
    private static SQLiteDatabase database;
    private static ArrayList<ShopItem> shopItems;

    public static void init(Context context){
        instance = new ServerMock(context, DB_NAME, null, 1);
        database = instance.getWritableDatabase();
        shopItems = new ArrayList<>();

        // TODO: remove database update here
//        String[] statements = new String[]{
//                "DROP TABLE IF EXISTS users;",
//                "CREATE TABLE IF NOT EXISTS users(id INTEGER primary key autoincrement, username TEXT," +
//                        " email TEXT, password TEXT);"
//        };
//        for(String sql: statements){
//            database.execSQL(sql);
//        }
    }

    public static void destroy(){
        instance.database.close();
    }

    public static ServerMock getInstance() {
        return instance;
    }

    public ServerMock(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] statements = new String[]{
                "CREATE TABLE IF NOT EXISTS users(id INTEGER primary key autoincrement, username TEXT," +
                        " email TEXT, password TEXT);"
        };
        for(String sql: statements){
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addShopItem(ShopItem item){
        shopItems.add(item);
    }

    public ArrayList<ShopItem> getShopItems() {
        return shopItems;
    }

    public boolean checkUsername(String username) {
        database = getWritableDatabase();
        String sql = "SELECT * FROM users WHERE username='" + username + "';";
        Cursor cur = database.rawQuery(sql, null);
        if(cur.getCount() == 0){
            cur.close();
            return true;
        }
        cur.close();
        return false;
    }

    public boolean checkEmail(String email) {
        database = getWritableDatabase();
        String sql = "SELECT * FROM users WHERE email='" + email + "';";
        Cursor cur = database.rawQuery(sql, null);
        if(cur.getCount() == 0){
            cur.close();
            return true;
        }
        cur.close();
        return false;
    }

    public boolean createUser(String username, String email, String password) {
        database = getWritableDatabase();
        String sql = "SELECT * FROM users WHERE email='" + email + "';";
        Cursor cur = database.rawQuery(sql, null);
        if(cur.getCount() > 0){
            cur.close();
            return false;
        }
        String insert = "INSERT INTO users (username, email, password) " +
                "VALUES('" + username + "', '" + email + "', '" + password.hashCode() + "');";
        database.execSQL(insert);
        cur.close();
        return true;
    }

    public String[] login(String usernameOrEmail, String password) {
        database = getWritableDatabase();
        String sql = "SELECT * FROM users WHERE username='" + usernameOrEmail + "' OR " +
                "email='" + usernameOrEmail + "';";
        Cursor cur = database.rawQuery(sql, null);
        if(cur.getCount() == 0){
            cur.close();
            return new String[]{INVALID_LOGIN};
        }
        sql = "SELECT * FROM users WHERE username='" + usernameOrEmail + "' OR " +
                "email='" + usernameOrEmail + "' AND password='" + password + "';";
        cur = database.rawQuery(sql, null);
        if(cur.getCount() == 0){
            cur.close();
            return new String[]{INVALID_PASS};
        } else if(cur.getCount() == 1){
            cur.moveToFirst();
            String username = cur.getString(1);
            String email = cur.getString(2);
            cur.close();
            return new String[]{username, email};
        }
        cur.close();
        return new String[]{};
    }

    public void sendMessage(final Context context, String username, final String message, final int vId){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                    //Log.i("Sender", "message successfully sent");
                    ((ChatActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ChatActivity) context).notifyMessageSent(vId);
                        }
                    });
                    Thread.sleep(1000);
                    ((ChatActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((ChatActivity) context).receive(message, "~Server~", false);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
