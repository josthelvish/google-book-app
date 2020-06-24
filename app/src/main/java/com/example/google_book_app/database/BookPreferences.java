package com.example.google_book_app.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.google_book_app.R;

public class BookPreferences {

    public static String getFilterPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String filterPrefKey = context.getString(R.string.pref_key_filter_by);
        String filterDefault = context.getString(R.string.pref_filter_by_default);

        return preferences.getString(filterPrefKey, filterDefault);
    }
}
