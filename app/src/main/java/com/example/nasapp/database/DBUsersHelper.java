package com.example.nasapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.nasapp.models.UserModel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DBUsersHelper extends SQLiteOpenHelper {
    public static final String USERS_TABLE = "USERS_TABLE";
    public static final String COLUMN_FIRST_NAME = "FIRST_NAME";
    public static final String COLUMN_LAST_NAME = "LAST_NAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_ID = "ID";
    public static final String REGISTRATION_DATE = "REGISTRATION_DATE";

    public DBUsersHelper(@Nullable Context context) {
        super(context, "user.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + USERS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FIRST_NAME + " TEXT, "  + COLUMN_LAST_NAME + " TEXT, " + COLUMN_EMAIL + " TEXT, " + COLUMN_PASSWORD + " TEXT, " + REGISTRATION_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + USERS_TABLE);
        onCreate(db);

    }
    public boolean addUser(UserModel userModel) throws NoSuchAlgorithmException {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FIRST_NAME, userModel.getName());
        cv.put(COLUMN_LAST_NAME, userModel.getLastName());
        cv.put(COLUMN_EMAIL, userModel.getEmail());
        cv.put(COLUMN_PASSWORD, HashPassword.hashPassword(userModel.getPassword()));

        long insert = db.insert(USERS_TABLE, null, cv);

        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public class HashPassword {

        public static String hashPassword(String password) throws NoSuchAlgorithmException {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashPassword = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashPassword);

        }

        public static String bytesToHex(byte[] bytes) {
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : bytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        }

    }
    public boolean checkUser(String email, String password) throws NoSuchAlgorithmException {
        String[] columns = {
          COLUMN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String query = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ? ";

        String hashedPassword = HashPassword.hashPassword(password);

        String[] selectionArgs = {email, hashedPassword};

        Cursor cursor = db.query(USERS_TABLE, columns, query, selectionArgs, null, null, null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }
    public int getUserById(String email) {
        String[] columns = { COLUMN_ID };
        SQLiteDatabase db = this.getReadableDatabase();
        String query = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(USERS_TABLE, columns, query, selectionArgs, null, null, null);

        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }
        cursor.close();
        db.close();
        return id;
    }
    public String getFirstName(String email) {
        String[] columns = {COLUMN_FIRST_NAME};
        SQLiteDatabase db = this.getReadableDatabase();
        String query = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(USERS_TABLE, columns, query, selectionArgs, null, null, null);

        String fullName = "";
        if (cursor.moveToFirst()) {
            fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME));
        }
        cursor.close();
        db.close();
        return fullName;
    }

    public String getLastName(String email) {
        String[] columns = {COLUMN_LAST_NAME};
        SQLiteDatabase db = this.getReadableDatabase();
        String query = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(USERS_TABLE, columns, query, selectionArgs, null, null, null);

        String lastName = "";
        if (cursor.moveToFirst()) {
            lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME));
        }
        cursor.close();
        db.close();
        return lastName;
    }

    public boolean checkEmail(String email) {
        String[] columns = {
                COLUMN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        String query = COLUMN_EMAIL + " = ?";

        String[] selectionArgs = {email};

        Cursor cursor = db.query(USERS_TABLE, columns, query, selectionArgs, null, null, null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

}
