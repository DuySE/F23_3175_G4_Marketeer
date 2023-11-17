package com.example.f23_3175_g4_marketeer;

import android.content.Context;
import android.content.SharedPreferences;

public class StoredDataHelper {

    // Store data as key-value pair to SharedPreferences
    static void save(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Get value by key from SharedPreferences
    static String get(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("application", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, "");
        return value.toLowerCase();
    }

    static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
