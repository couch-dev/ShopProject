package net.couchdev.android.layoutsandbox.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Tim on 26.12.2016.
 */

public class Database extends SQLiteOpenHelper {

    public static Database instance;
    private static final String DB_NAME = "cam_db";
    private static SQLiteDatabase database;
    private static String loggedInUser;
    private static String loggedInPass;

    public static void init(Context context){
        instance = new Database(context, DB_NAME, null, 1);
        database = instance.getWritableDatabase();

        // TODO: remove database update here
        String[] statements = new String[]{
                "DROP TABLE IF EXISTS userdata;",
                "DROP TABLE IF EXISTS last_login;",
                "CREATE TABLE IF NOT EXISTS userdata(username TEXT primary key not null," +
                        " first_name TEXT, last_name TEXT, date_of_birth DATE, address_line_1 TEXT," +
                        " address_line_2 TEXT, address_line_3 TEXT, country TEXT, state TEXT," +
                        " city TEXT, zip TEXT, complete INT);",
                "CREATE TABLE IF NOT EXISTS last_login(username TEXT primary key not null," +
                        " password TEXT);"
        };
        for(String sql: statements){
            database.execSQL(sql);
        }
    }

    public static void setLoggedInUser(String username, String password){
        loggedInUser = username;
        loggedInPass = password;
    }

    public static void destroy(){
        instance.database.close();
    }

    public static Database getInstance() {
        return instance;
    }

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] statements = new String[]{
                "CREATE TABLE IF NOT EXISTS userdata(username TEXT primary key not null," +
                        " first_name TEXT, last_name TEXT, date_of_birth DATE, address_line_1 TEXT," +
                        " address_line_2 TEXT, address_line_3 TEXT, country TEXT, state TEXT," +
                        " city TEXT, zip TEXT, complete INT);"
        };
        for(String sql: statements){
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void updateLastLoggedInUser(){
        clearLogin();
        String insert = "INSERT INTO last_login (username, password) VALUES('" + loggedInUser + "'," +
                " '" + loggedInPass + "');";
        database.execSQL(insert);
    }

    public void clearLogin(){
        String delete = "DELETE FROM last_login;";
        database.execSQL(delete);
    }

    public String[] getLastLogin(){
        String sql = "SELECT * FROM last_login;";
        String[] login = null;
        Cursor cur = database.rawQuery(sql, null);
        if(cur.getCount() == 1){
            login = new String[2];
            login[0] = cur.getString(0);
            login[1] = cur.getString(1);
        }
        cur.close();
        return login;
    }

    public void addUserData(){
        if(loggedInUser == null){
            return;
        }
        String insert = "INSERT INTO userdata (username, complete) VALUES('" + loggedInUser + "', 0);";
        try{
            database.execSQL(insert);
        } catch(SQLiteConstraintException ex){
        }
    }

    public void updateUserData(String firstName, String lastName, String dateOfBirth){
        if(loggedInUser == null){
            return;
        }
        String update = "UPDATE userdata SET first_name='" + firstName + "', last_name='" + lastName + "'," +
                " date_of_birth='" + dateOfBirth + "'" +
                " WHERE username='" + loggedInUser + "';";
        database.execSQL(update);
    }

    public void updateUserData(String addressLine1, String addressLine2, String addressLine3,
                               String country, String state, String city, String zip){
        if(loggedInUser == null){
            return;
        }
        String update = "UPDATE userdata SET address_line_1='" + addressLine1 + "', address_line_2='" + addressLine2 + "'," +
                " address_line_3='" + addressLine3 + "', country='" + country + "', state='" + state + "', city='" +
                city + "', zip='" + zip + "'" +
                " WHERE username='" + loggedInUser + "';";
        database.execSQL(update);
    }

    public void setComplete(boolean complete){
        if(loggedInUser == null){
            return;
        }
        String update = "UPDATE userdata SET complete=" + (complete ? "1" : "0") +
                " WHERE username='" + loggedInUser + "';";
        database.execSQL(update);
    }

    public boolean isComplete(){
        if(loggedInUser == null){
            return false;
        }
        String sql = "SELECT * FROM userdata WHERE username='" + loggedInUser + "' AND" +
                " complete=1;";
        Cursor cur = database.rawQuery(sql, null);
        if(cur.getCount() == 1){
            cur.close();
            return true;
        }
        cur.close();
        return false;
    }

}
