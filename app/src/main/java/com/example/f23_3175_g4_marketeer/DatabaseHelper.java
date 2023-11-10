package com.example.f23_3175_g4_marketeer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

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
            contentValues.put(COLUMN_PASSWORD, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
            contentValues.put(COLUMN_ADDRESS, user.getAddress());
            contentValues.put(COLUMN_PHONE, user.getPhone());
            contentValues.put(COLUMN_PROFILE_IMG, user.getProfileImg());
            String where = COLUMN_USERNAME + " = ?";
            String[] whereArgs = new String[]{user.getUsername()};
            db.update(TABLE_USERS, contentValues, where, whereArgs);
            db.close();
        }
    }

    public Product addProduct(String name, String price, String seller, String imgName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_PRICE, price);
        contentValues.put(DatabaseHelper.COLUMN_SELLER, seller);
        contentValues.put(DatabaseHelper.COLUMN_STATUS, "Available");
        contentValues.put(DatabaseHelper.COLUMN_IMG_NAME, imgName);
        db.insert(DatabaseHelper.TABLE_PRODUCTS, null, contentValues);
        return null;
    }

    public void updateProduct(int id, String name, String price, String seller, String status, String imgName) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_PRICE, price);
        contentValues.put(DatabaseHelper.COLUMN_SELLER, seller);
        contentValues.put(DatabaseHelper.COLUMN_STATUS, status);
        contentValues.put(DatabaseHelper.COLUMN_IMG_NAME, imgName);
        String where = COLUMN_PRODUCT_ID + " = ?";
        String[] whereArgs = new String[]{Integer.toString(id)};
        db.update(TABLE_PRODUCTS, contentValues, where, whereArgs);
    }

    public Product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{COLUMN_PRODUCT_ID, COLUMN_NAME, COLUMN_PRICE, COLUMN_SELLER, COLUMN_STATUS, COLUMN_IMG_NAME};
        String selection = COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = new String[]{Integer.toString(id)};
        Cursor cursor = db.query(TABLE_PRODUCTS, columns, selection, selectionArgs, null, null, null);
        int colId = cursor.getColumnIndex(COLUMN_PRODUCT_ID);
        int colName = cursor.getColumnIndex(COLUMN_NAME);
        int colPrice = cursor.getColumnIndex(COLUMN_PRICE);
        int colSeller = cursor.getColumnIndex(COLUMN_SELLER);
        int colStatus = cursor.getColumnIndex(COLUMN_STATUS);
        int colImg = cursor.getColumnIndex(COLUMN_IMG_NAME);

        Product product = null;
        if (cursor.moveToFirst()) {
            product = new Product(cursor.getString(colName), cursor.getString(colPrice), cursor.getString(colImg),
                    cursor.getString(colSeller), cursor.getString(colStatus), cursor.getInt(colId));
        }
        return product;
    }

    public List<Product> getProducts(String seller) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{COLUMN_PRODUCT_ID, COLUMN_NAME, COLUMN_PRICE, COLUMN_SELLER, COLUMN_STATUS, COLUMN_IMG_NAME};
        String selection = COLUMN_SELLER + " = ? AND " + COLUMN_STATUS + " = ? ";
        String[] selectionArgs = new String[]{seller, "Available"};
        Cursor cursor = db.query(TABLE_PRODUCTS, columns, selection, selectionArgs, null, null, null);
        int colId = cursor.getColumnIndex(COLUMN_PRODUCT_ID);
        int colName = cursor.getColumnIndex(COLUMN_NAME);
        int colPrice = cursor.getColumnIndex(COLUMN_PRICE);
        int colSeller = cursor.getColumnIndex(COLUMN_SELLER);
        int colStatus = cursor.getColumnIndex(COLUMN_STATUS);
        int colImg = cursor.getColumnIndex(COLUMN_IMG_NAME);

        Product product = null;
        if (cursor.moveToFirst()) {
            product = new Product(cursor.getString(colName), cursor.getString(colPrice), cursor.getString(colImg),
                    cursor.getString(colSeller), cursor.getString(colStatus), cursor.getInt(colId));
            products.add(product);
            while (cursor.moveToNext()) {
                product = new Product(cursor.getString(colName), cursor.getString(colPrice), cursor.getString(colImg),
                        cursor.getString(colSeller), cursor.getString(colStatus), cursor.getInt(colId));
                products.add(product);
            }
        }
        return products;
    }
}