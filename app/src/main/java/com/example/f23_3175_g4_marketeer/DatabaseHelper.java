package com.example.f23_3175_g4_marketeer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Database information
    private static final String DB_NAME = "MARKETEER.DB";
    // Table name
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_PRODUCTS = "PRODUCTS";
    private static final String TABLE_TRANSACTIONS = "Transactions";
    // Database version
    private static final int DB_VERSION = 1;
    // Column name
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_ADDRESS = "Address";
    private static final String COLUMN_PHONE = "Phone";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_SELLER = "seller";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_IMG_NAME = "image";
    private static final String COLUMN_PRODUCT_NAME = "ProductName";
    private static final String COLUMN_TRANSACTION_DATE = "TransactionDate";
    private static final String COLUMN_AMOUNT = "Amount";
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

    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
            COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
            COLUMN_IMG_NAME + " TEXT NOT NULL, " +
            COLUMN_TRANSACTION_DATE + " TEXT NOT NULL, " +
            COLUMN_AMOUNT + " INTEGER NOT NULL, " +
            COLUMN_USERNAME + " TEXT NOT NULL)";

    // This method is called the first time a database is accessed.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
        sqLiteDatabase.execSQL(CREATE_TABLE_PRODUCTS);
        sqLiteDatabase.execSQL(CREATE_TABLE_TRANSACTIONS);
    }

    /* This method is called if the database version number changes.
    It prevents previous users apps from breaking when change the database design.
    */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(sqLiteDatabase);
    }

    // Table Users methods
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
        cursor.close();
        db.close();
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
            String where = COLUMN_USERNAME + " = ?";
            String[] whereArgs = new String[]{user.getUsername()};
            db.update(TABLE_USERS, contentValues, where, whereArgs);
            db.close();
        }
    }

    // Table Products methods
    public void addProduct(String name, double price, String seller, String imgName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_PRICE, price);
        contentValues.put(DatabaseHelper.COLUMN_SELLER, seller);
        contentValues.put(DatabaseHelper.COLUMN_STATUS, "Available");
        contentValues.put(DatabaseHelper.COLUMN_IMG_NAME, imgName);
        db.insert(DatabaseHelper.TABLE_PRODUCTS, null, contentValues);
    }

    public void updateProduct(String name, double price, String seller, String status, String imgName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, name);
        contentValues.put(DatabaseHelper.COLUMN_PRICE, price);
        contentValues.put(DatabaseHelper.COLUMN_SELLER, seller);
        contentValues.put(DatabaseHelper.COLUMN_STATUS, status);
        contentValues.put(DatabaseHelper.COLUMN_IMG_NAME, imgName);
        String where = COLUMN_PRODUCT_ID + " = 5";
        String[] whereArgs = new String[]{};
        db.update(TABLE_PRODUCTS, contentValues, where, whereArgs);
    }

    // Table Transactions methods
    public void addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String transactionDate = formatter.format(date);
        contentValues.put(DatabaseHelper.COLUMN_PRODUCT_NAME, transaction.getProductName());
        contentValues.put(DatabaseHelper.COLUMN_IMG_NAME, transaction.getImageName());
        contentValues.put(DatabaseHelper.COLUMN_TRANSACTION_DATE, transactionDate);
        contentValues.put(DatabaseHelper.COLUMN_USERNAME, transaction.getUsername());
        db.insert(DatabaseHelper.TABLE_TRANSACTIONS, null, contentValues);
        db.close();
    }

    public List<Transaction> getTransactionsChart(String username) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = new String[]{username};
        String[] columns = new String[]{COLUMN_PRODUCT_NAME, COLUMN_IMG_NAME,
                COLUMN_TRANSACTION_DATE, "SUM(Amount) AS Amount"};
        Cursor cursor = db.query(TABLE_TRANSACTIONS, columns, selection,
                selectionArgs, COLUMN_TRANSACTION_DATE, null, null);
        int colProductName = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
        int colImageName = cursor.getColumnIndex(COLUMN_IMG_NAME);
        int colTransactionDate = cursor.getColumnIndex(COLUMN_TRANSACTION_DATE);
        int colAmount = cursor.getColumnIndex(COLUMN_AMOUNT);
        Transaction transaction;
        while (cursor.moveToNext()) {
            transaction = new Transaction(cursor.getString(colTransactionDate),
                    cursor.getString(colProductName), cursor.getString(colImageName), username);
            transaction.setAmount(Integer.parseInt(cursor.getString(colAmount)));
            transactions.add(transaction);
        }
        cursor.close();
        db.close();
        return transactions;
    }

    public List<Transaction> getTransactions(String username, String date) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{COLUMN_PRODUCT_NAME, COLUMN_IMG_NAME,
                COLUMN_TRANSACTION_DATE};
        String selection;
        String[] selectionArgs;
        if (date != null) {
            selection = COLUMN_USERNAME + " = ? AND " + COLUMN_TRANSACTION_DATE + " = ?";
            selectionArgs = new String[]{username, date};
        } else {
            selection = COLUMN_USERNAME + " = ?";
            selectionArgs = new String[]{username};
        }
        Cursor cursor = db.query(TABLE_TRANSACTIONS, columns, selection,
                selectionArgs, null, null, null);
        int colProductName = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
        int colImageName = cursor.getColumnIndex(COLUMN_IMG_NAME);
        int colTransactionDate = cursor.getColumnIndex(COLUMN_TRANSACTION_DATE);
        Transaction transaction;
        while (cursor.moveToNext()) {
            transaction = new Transaction(cursor.getString(colTransactionDate),
                    cursor.getString(colProductName), cursor.getString(colImageName), username);
            transactions.add(transaction);
        }
        cursor.close();
        db.close();
        return transactions;
    }
}