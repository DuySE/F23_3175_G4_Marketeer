package com.example.f23_3175_g4_marketeer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.mindrot.jbcrypt.BCrypt;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Database information
    private static final String DB_NAME = "MARKETEER.DB";
    // Table name
    private static final String TABLE_USERS = "Users";
    static final String TABLE_PRODUCTS = "PRODUCTS";
    // Database version
    private static final int DB_VERSION = 1;
    // Column name
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_ADDRESS = "Address";
    private static final String COLUMN_PHONE = "Phone";
    static final String COLUMN_PRODUCT_ID = "product_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_PRICE = "price";
    static final String COLUMN_SELLER = "seller";
    static final String COLUMN_STATUS = "status";
    static final String COLUMN_IMG_NAME = "image";
    // Create table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" +
            COLUMN_USERNAME + " TEXT PRIMARY KEY NOT NULL," +
            COLUMN_PASSWORD + " TEXT NOT NULL," +
            COLUMN_ADDRESS + " TEXT," +
            COLUMN_PHONE + " TEXT" +
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
        // Hash password before store it in database
        contentValues.put(COLUMN_PASSWORD, BCrypt.hashpw(password, BCrypt.gensalt(12)));
        db.insert(TABLE_USERS, null, contentValues);
        db.close();
    }

    public boolean login(String username, String password) {
        User user = getUser(username);
        String userPwd = "";
        if (user != null) userPwd = user.getPassword();
        // Check if password inputted by user matches with password stored in database
        return BCrypt.checkpw(password, userPwd);
    }

    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{COLUMN_USERNAME, COLUMN_PASSWORD,
                COLUMN_ADDRESS, COLUMN_PHONE};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = new String[]{username};
        Cursor cursor = db.query(TABLE_USERS, columns, selection,
                selectionArgs, null, null, null);
        int colUsername = cursor.getColumnIndex(COLUMN_USERNAME);
        int colPassword = cursor.getColumnIndex(COLUMN_PASSWORD);
        int colAddress = cursor.getColumnIndex(COLUMN_ADDRESS);
        int colPhone = cursor.getColumnIndex(COLUMN_PHONE);
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User(cursor.getString(colUsername));
            user.setPassword(cursor.getString(colPassword));
            user.setAddress(cursor.getString(colAddress));
            user.setPhone(cursor.getString(colPhone));
        }
        cursor.close();
        db.close();
        return user;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, user.getUsername());
        contentValues.put(COLUMN_PASSWORD, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
        contentValues.put(COLUMN_ADDRESS, user.getAddress());
        contentValues.put(COLUMN_PHONE, user.getPhone());
        String where = COLUMN_USERNAME + " = ?";
        String[] whereArgs = new String[]{user.getUsername()};
        db.update(TABLE_USERS, contentValues, where, whereArgs);
        db.close();
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