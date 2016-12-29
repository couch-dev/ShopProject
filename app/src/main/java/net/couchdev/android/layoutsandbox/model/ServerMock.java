package net.couchdev.android.layoutsandbox.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tim on 28.12.2016.
 */

public class ServerMock extends SQLiteOpenHelper {

    public static ServerMock instance;
    private static final String DB_NAME = "server_db";
    private static SQLiteDatabase database;

    public static void init(Context context){
        instance = new ServerMock(context, DB_NAME, null, 1);
        database = instance.getWritableDatabase();
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

    public boolean checkUsername(String username) {
        database = getWritableDatabase();
        String sql = "SELECT * FROM users WHERE username='" + username + "';";
        Cursor cur = database.rawQuery(sql, null);
        if(cur.getCount() == 0){
            return true;
        }
        return false;
    }

    public boolean createUser(String username, String email, String password) {
        database = getWritableDatabase();
        String sql = "SELECT * FROM users WHERE email='" + email + "';";
        Cursor cur = database.rawQuery(sql, null);
        if(cur.getCount() > 0){
            return false;
        }
        String insert = "INSERT INTO users (username, email, password) " +
                "VALUES('" + username + "', '" + email + "', '" + password.hashCode() + "');";
        database.execSQL(insert);
        return true;
    }

    public String login(String usernameOrEmail, String password) {
        database = getWritableDatabase();
        String sql = "SELECT * FROM users WHERE username='" + usernameOrEmail + "' OR " +
                "email='" + usernameOrEmail + "' AND password='" + password.hashCode() + "';";
        Cursor cur = database.rawQuery(sql, null);
        if(cur.getCount() == 1){
            cur.moveToFirst();
            return cur.getString(1);
        }
        return null;
    }
}
