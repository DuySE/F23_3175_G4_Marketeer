package com.example.f23_3175_g4_marketeer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Database information
    static final String DB_NAME = "MARKETEER.DB";
    // Table name
    static final String TABLE_USERS = "USERS";
    // Database version
    static final int DB_VERSION = 1;
    static final String COLUMN_USERNAME = "username";
    static final String COLUMN_PASSWORD = "password";
    // Create table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_USERNAME + " TEXT PRIMARY KEY, " + COLUMN_PASSWORD + " TEXT NOT NULL)";

    // This method is called the first time a database is accessed.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
    }

    /* This method is called if the database version number changes.
    It prevents previous users apps from breaking when change the database design.
    */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(sqLiteDatabase);
    }

    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.COLUMN_USERNAME, username);
        contentValue.put(DatabaseHelper.COLUMN_PASSWORD, password);
        db.insert(DatabaseHelper.TABLE_USERS, null, contentValue);
        db.close();
    }

    public User getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{DatabaseHelper.COLUMN_USERNAME, DatabaseHelper.COLUMN_PASSWORD};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = new String[]{username, password};
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection,
                selectionArgs, null, null, null);
        int colUsername = cursor.getColumnIndex(COLUMN_USERNAME);
        int colPassword = cursor.getColumnIndex(COLUMN_PASSWORD);
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(cursor.getString(colUsername),
                    cursor.getString(colPassword));
        }
        return user;
    }
}