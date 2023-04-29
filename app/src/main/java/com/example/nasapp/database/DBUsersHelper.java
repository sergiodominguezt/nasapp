package com.example.nasapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.nasapp.UserModel;

public class DBUsersHelper extends SQLiteOpenHelper {
    public static final String USERS_TABLE = "USERS_TABLE";
    public static final String COLUMN_FULL_NAME = "FULL_NAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_ID = "ID";
    public static final String REGISTRATION_DATE = "REGISTRATION_DATE";

    public DBUsersHelper(@Nullable Context context) {
        super(context, "user.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + USERS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FULL_NAME + " TEXT, " + COLUMN_EMAIL + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + REGISTRATION_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

        db.execSQL(createTable);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addUser(UserModel userModel) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FULL_NAME, userModel.getName());
        cv.put(COLUMN_EMAIL, userModel.getEmail());
        cv.put(COLUMN_PASSWORD, userModel.getPassword());

        long insert = db.insert(USERS_TABLE, null, cv);

        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean checkUser(String email, String password) {
        String[] columns = {
          COLUMN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String query = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ? ";

        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(USERS_TABLE, columns, query, selectionArgs, null, null, null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public int getUserById(String email, String password) {
        String[] columns = { COLUMN_ID };
        SQLiteDatabase db = this.getReadableDatabase();
        String query = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        // Check if password argument is not null
        if (password != null) {
            query += " AND " + COLUMN_PASSWORD + " = ?";
            selectionArgs = new String[] { email, password };
        }

        Cursor cursor = db.query(USERS_TABLE, columns, query, selectionArgs, null, null, null);

        int id = 0;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }
        cursor.close();
        db.close();
        return id;
    }
}
