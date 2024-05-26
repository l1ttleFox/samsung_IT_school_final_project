package com.samsung.finalproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "database";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "places";
    private static final String ID_COl = "id";
    private static final String NAME_COl = "name";
    private static final String LATITUDE_COL = "latitude";
    private static final String LONGITUDE_COL = "longitude";
    private Context context;
    private static DBHandler singleton;

    public static synchronized DBHandler getDB(Context context) {
        if (singleton == null) {
            singleton = new DBHandler(context);
        }
        return singleton;
    }

    private DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COl + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME_COl + " TEXT, " +
                LATITUDE_COL + " TEXT, " +
                LONGITUDE_COL + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addNewPlace(String name, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME_COl, name);
        values.put(LATITUDE_COL, latitude);
        values.put(LONGITUDE_COL, longitude);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();

        return result;
    }

    public ArrayList<PlaceItem> getAllPlaces() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {ID_COl, NAME_COl, LATITUDE_COL, LONGITUDE_COL}, null, null, null, null, null);
        ArrayList<PlaceItem> allPlaces = new ArrayList<>();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(NAME_COl));
            @SuppressLint("Range") String latitude = cursor.getString(cursor.getColumnIndex(LATITUDE_COL));
            @SuppressLint("Range") String longitude = cursor.getString(cursor.getColumnIndex(LONGITUDE_COL));
            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(ID_COl));
            String time = new SolarTime(Double.parseDouble(longitude)).toString();

            PlaceItem iPlace = new PlaceItem(name, time, Double.parseDouble(latitude), Double.parseDouble(longitude), id);
            allPlaces.add(iPlace);
        }
        cursor.close();

        return allPlaces;
    }

    public boolean deletePlace(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = db.delete(TABLE_NAME, ID_COl + "=?", new String[] {Long.toString(id)}) > 0;
        db.close();

        return result;
    }
}
