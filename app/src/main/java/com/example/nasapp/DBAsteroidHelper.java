package com.example.nasapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBAsteroidHelper extends SQLiteOpenHelper {


    public static final String COLUMN_ID = "ID";
    public static final String ASTEROIDS_TABLE = "ASTEROIDS_TABLE";
    public static final String COLUMN_ASTEROID_NAME = "ASTEROID_NAME";
    public static final String COLUMN_HAZARDOUS_ASTEROID = "HAZARDOUS_ASTEROID";
    public static final String COLUMN_NEO_REFERENCE_ID = "NEO_REFERENCE_ID";
    public static final String COLUMN_NASA_JPL_URL = "NASA_JPL_URL";
    public static final String COLUMN_IS_SENTRY_OBJECT = "IS_SENTRY_OBJECT";
    public static final String COLUMN_ABSOLUTE_MAGNITUDE = "ABSOLUTE_MAGNITUDE";
    public static final String COLUMN_USER_ID = "USER_ID";
    private int userId;



    public DBAsteroidHelper(@Nullable Context context, int userId) {
        super(context, "asteroid.db", null, 1);
        this.userId = userId;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + ASTEROIDS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ASTEROID_NAME + " TEXT, " + COLUMN_HAZARDOUS_ASTEROID + " INTEGER, " + COLUMN_NEO_REFERENCE_ID + " REAL, " + COLUMN_NASA_JPL_URL + " TEXT, " + COLUMN_IS_SENTRY_OBJECT + " INTEGER, " + COLUMN_ABSOLUTE_MAGNITUDE + " TEXT, " + COLUMN_USER_ID + " INTEGER,FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + DBHelper.USERS_TABLE + "(" + DBHelper.COLUMN_ID + "));";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertAsteroids(String name, boolean hazardousAsteroid, String neoReferenceId, String nasaJplUrl, boolean isSentryObject, String absoluteMagnitude, long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ASTEROID_NAME, name);
        values.put(COLUMN_HAZARDOUS_ASTEROID, hazardousAsteroid);
        values.put(COLUMN_NEO_REFERENCE_ID, neoReferenceId);
        values.put(COLUMN_NASA_JPL_URL, nasaJplUrl);
        values.put(COLUMN_IS_SENTRY_OBJECT, isSentryObject);
        values.put(COLUMN_ABSOLUTE_MAGNITUDE, absoluteMagnitude);
        values.put(COLUMN_USER_ID, userId);

        db.insert(ASTEROIDS_TABLE, null, values);
        db.close();

    }
}
