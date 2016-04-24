package org.vzw.beta.homestation.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by user109 on 21/03/2016.
 */
public class PreferencesHelper {

    private final static String PREF_LANGUAGE = "language";
    private final static String LANGUAGE_NL = "nl";
    private final static String LANGUAGE_EN = "en";
    private final static String PREF_LOCATION="location";
    public static String COUNTRY_CODE="code";

    private static Activity activity = new Activity();
    private static Context locationActivity;

    /*Language settings*/
    public static String getLanguagePreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PREF_LANGUAGE, LANGUAGE_NL);
    }

    public static void setLanguagePreference(Context context, String language) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_LANGUAGE, language);
        editor.apply();
    }
}
