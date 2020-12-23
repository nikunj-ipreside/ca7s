package com.music.ca7s.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.music.ca7s.contant.SharedPref;

import java.util.Locale;

/**
 * This class is used to change your application locale and persist this change for the next time
 * that your app is going to be used.
 * <p/>
 * You can also change the locale of your application on the fly by using the setLocale method.
 * <p/>
 * Created by gunhansancar on 07/10/15.
 */
public class LocaleHelper {
    public static boolean serviceBound = false;

//    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context, lang);
    }

    public static Context onAttach(Context context, String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);
        return setLocale(context, lang);
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    public static Context setLocale(Context context, String language) {
        persist(context, language);
        Log.e("LocaleHelper : ",language+"");

        if (language.toString().equalsIgnoreCase("Portuguese")){
            language = "pt";
        }else if (language.toString().equalsIgnoreCase("English")){
            language = "en";
        }
        if (language.isEmpty()){
            language =Locale.getDefault().getLanguage();
        }

        Log.e("LocaleHelper1 : ",language);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }

        return updateResourcesLegacy(context, language);
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        TutorialPrefrences sharedPref = new TutorialPrefrences(context);
        return sharedPref.getString(SharedPref.LANGUAGE);
    }

    private static void persist(Context context, String language) {
        TutorialPrefrences sharedPref = new TutorialPrefrences(context);
        sharedPref.putString(SharedPref.LANGUAGE, language);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
       return changeLanguage(language,context);
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//        Configuration configuration = context.getResources().getConfiguration();
//        configuration.setLocale(locale);
//        configuration.setLayoutDirection(locale);

//        return context.createConfigurationContext(configuration);
    }

    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        return changeLanguage(language,context);
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//        Resources resources = context.getResources();
//
//        Configuration configuration = resources.getConfiguration();
//        configuration.locale = locale;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            configuration.setLayoutDirection(locale);
//        }
//        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
//
//        return context;
    }

    public static Context changeLanguage(String language,Context mContext)
    {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
        return mContext;
    }

}
