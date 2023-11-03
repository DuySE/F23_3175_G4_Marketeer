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
    static final String TABLE_USERS = "Users";
    static final String TABLE_PRODUCTS = "PRODUCTS";
    // Database version
    static final int DB_VERSION = 1;
    // Column name
    static final String COLUMN_USERNAME = "Username";
    static final String COLUMN_PASSWORD = "Password";
    static final String COLUMN_ADDRESS = "Address";
    static final String COLUMN_PHONE = "Phone";
    static final String COLUMN_PROFILE_IMG = "Profile_Image";
    static final String COLUMN_PRODUCT_ID = "Product_Id";
    static final String COLUMN_NAME = "Name";
    static final String COLUMN_PRICE = "Price";
    static final String COLUMN_SELLER = "Seller";
    static final String COLUMN_STATUS = "Status";
    static final String COLUMN_IMG_NAME = "Image";
    // Create table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" +
            COLUMN_USERNAME + " TEXT PRIMARY KEY NOT NULL," +
            COLUMN_PASSWORD + " TEXT NOT NULL," +
            COLUMN_ADDRESS + " TEXT," +
            COLUMN_PHONE + " TEXT," +
            COLUMN_PROFILE_IMG + " TEXT" +
            ")";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
            COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " + COLUMN_PRICE + " REAL NOT NULL, " +
            COLUMN_SELLER + " TEXT NOT NULL, " + COLUMN_STATUS + " TEXT NOT NULL, " +
            COLUMN_IMG_NAME + " TEXT NOT NULL)";

    // This method is called the first time a database is accessed.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
        sqLiteDatabase.execSQL(CREATE_TABLE_PRODUCTS);
    }

    /* This method is called if the database version number changes.
    It prevents previous users apps from breaking when change the database design.
    */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(sqLiteDatabase);
    }

    public void addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_USERS, null, contentValues);
        db.close();
    }

    public User getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{COLUMN_USERNAME, COLUMN_PASSWORD};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = new String[]{username, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection,
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

    //This version is to query for ProfileActivity
    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_ADDRESS, COLUMN_PHONE, COLUMN_PROFILE_IMG};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = new String[]{username};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int colUsername = cursor.getColumnIndex(COLUMN_USERNAME);
        int colPassword = cursor.getColumnIndex(COLUMN_PASSWORD);
        int colAddress = cursor.getColumnIndex(COLUMN_ADDRESS);
        int colPhone = cursor.getColumnIndex(COLUMN_PHONE);
        int colProfileImg = cursor.getColumnIndex(COLUMN_PROFILE_IMG);

        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(cursor.getString(colUsername), cursor.getString(colPassword),
                    cursor.getString(colAddress), cursor.getString(colPhone),
                    cursor.getString(colProfileImg));
        }
        return user;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (user != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_USERNAME, user.getUsername());
            contentValues.put(COLUMN_PASSWORD, user.getPassword());
            contentValues.put(COLUMN_ADDRESS, user.getAddress());
            contentValues.put(COLUMN_PHONE, user.getPhone());
            contentValues.put(COLUMN_PROFILE_IMG, user.getProfileImg());
            String where = COLUMN_USERNAME + " = ?";
            String[] whereArgs = new String[]{user.getUsername()};
            db.update(TABLE_USERS, contentValues, where, whereArgs);
            db.close();
        }
    }

    public void addProduct(String name, double price, String seller, String imgName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_PRICE, price);
        contentValues.put(DatabaseHelper.COLUMN_SELLER, seller);
        contentValues.put(DatabaseHelper.COLUMN_STATUS, "Available");
        contentValues.put(DatabaseHelper.COLUMN_IMG_NAME, imgName);
        db.insert(DatabaseHelper.TABLE_PRODUCTS, null, contentValues);
    }

    public void updateProduct(String name, double price, String seller, String status, String imgName){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_PRICE, price);
        contentValues.put(DatabaseHelper.COLUMN_SELLER, seller);
        contentValues.put(DatabaseHelper.COLUMN_STATUS, status);
        contentValues.put(DatabaseHelper.COLUMN_IMG_NAME, imgName);
        String where = COLUMN_PRODUCT_ID + " = 1";
        String[] whereArgs = new String[]{};
        db.update(TABLE_PRODUCTS,contentValues,where,whereArgs);
    }


}