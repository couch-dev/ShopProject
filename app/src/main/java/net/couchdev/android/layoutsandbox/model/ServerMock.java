package net.couchdev.android.layoutsandbox.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tim on 28.12.2016.
 */

public class ServerMock extends SQLiteOpenHelper {

    public static final String INVALID_LOGIN = "1";
    public static final String INVALID_PASS = "2";

    public static ServerMock instance;
    private static final String DB_NAME = "server_db";
    private static SQLiteDatabase database;

    public static void init(Context context){
        instance = new ServerMock(context, DB_NAME, null, 1);
        database = instance.getWritableDatabase();

        // TODO: remove database update here
        String[] statements = new String[]{
                "DROP TABLE IF EXISTS users;",
                "CREATE TABLE IF NOT EXISTS users(id INTEGER primary key autoincrement, username TEXT," +
                        " email TEXT, password TEXT);"
        };
        for(String sql: statements){
            database.execSQL(sql);
        }
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

    public String login(String usernameOrEmail, String password) {
        database = getWritableDatabase();
        String sql = "SELECT * FROM users WHERE username='" + usernameOrEmail + "' OR " +
                "email='" + usernameOrEmail + "';";
        Cursor cur = database.rawQuery(sql, null);
        if(cur.getCount() == 0){
            cur.close();
            return INVALID_LOGIN;
        }
        sql = "SELECT * FROM users WHERE username='" + usernameOrEmail + "' OR " +
                "email='" + usernameOrEmail + "' AND password='" + password + "';";
        cur = database.rawQuery(sql, null);
        if(cur.getCount() == 0){
            cur.close();
            return INVALID_PASS;
        } else if(cur.getCount() == 1){
            cur.moveToFirst();
            String username = cur.getString(1);
            cur.close();
            return username;
        }
        cur.close();
        return null;
    }
}
