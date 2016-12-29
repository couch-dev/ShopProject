package net.couchdev.android.layoutsandbox.model;

import android.content.Context;
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

    public static void init(Context context, String username){
        loggedInUser = username;
        instance = new Database(context, DB_NAME, null, 1);
        database = instance.getWritableDatabase();

        String[] statements = new String[]{
                "DROP TABLE IF EXISTS userdata;",
                "CREATE TABLE IF NOT EXISTS userdata(username TEXT primary key not null," +
                        " first_name TEXT, last_name TEXT, date_of_birth DATE, address_line_1 TEXT," +
                        " address_line_2 TEXT, address_line_3 TEXT, country TEXT, state TEXT," +
                        " city TEXT, zip TEXT);"
        };
        for(String sql: statements){
            database.execSQL(sql);
        }
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
                        " city TEXT, zip TEXT);"
        };
        for(String sql: statements){
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addUserData(){
        String insert = "INSERT INTO userdata (username) VALUES('" + loggedInUser + "');";
        database.execSQL(insert);
    }

    public void updateUserData(String firstName, String lastName, String dateOfBirth){
        String update = "UPDATE userdata SET first_name='" + firstName + "', last_name='" + lastName + "'," +
                " date_of_birth='" + dateOfBirth + "'" +
                " WHERE username='" + loggedInUser + "';";
        database.execSQL(update);
    }

    public void updateUserData(String addressLine1, String addressLine2, String addressLine3,
                               String country, String state, String city, String zip){
        String update = "UPDATE userdata SET address_line_1='" + addressLine1 + "', address_line_2='" + addressLine2 + "'," +
                " address_line_3='" + addressLine3 + "', country='" + country + "', state='" + state + "', city='" +
                city + "', zip='" + zip + "'" +
                " WHERE username='" + loggedInUser + "';";
        database.execSQL(update);
    }

}
