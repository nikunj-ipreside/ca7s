package com.music.ca7s.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.music.ca7s.contant.SharedPref;

import java.util.Set;


public class PreferanceHelper {

    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public PreferanceHelper(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(SharedPref.prefName, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        String str = sharedPref.getString(key, "");
        return str;
    }


    public void putStringSet(String key, Set<String> value) {
        editor.putStringSet(key, value);
        editor.apply();
    }

    public Set<String> getStringSet(String key) {
        Set<String> set = sharedPref.getStringSet(key, null);
        return set;
    }

    public String getStringWithDefaultValue(String key, String defaultValue) {
        String str = sharedPref.getString(key, defaultValue);
        return str;
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return sharedPref.getBoolean(key, false);
    }

    public void clearPreference() {
        editor.clear();
        editor.commit();
    }

}
