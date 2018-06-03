package com.android.zakaria.weatherappdemo.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    private static String currentTemp;
    private static String currentHumidity;
    private static String currentDate;
    private static String currentSummary;
    private static boolean isTempType;

    private static MySharedPreferences mySharedPreferences;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private MySharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public static MySharedPreferences getMyPreferences(Context context) {
        if (mySharedPreferences == null) {
            mySharedPreferences = new MySharedPreferences(context);
        }
        return mySharedPreferences;
    }

    public static void setIsTempType(boolean isTempType) {
        MySharedPreferences.isTempType = isTempType;
    }

    public static void setCurrentTemp(String currentTemp) {
        editor.putString(Config.CURRENT_TEMP, currentTemp);
        editor.apply();
    }

    public static void setCurrentHumidity(String currentHumidity) {
        editor.putString(Config.CURRENT_HUMIDITY, currentHumidity);
        editor.apply();
    }

    public static void setCurrentDate(String currentDate) {
        editor.putString(Config.CURRENT_DATE, currentDate);
        editor.apply();
    }

    public static void setCurrentSummary(String currentSummary) {
        editor.putString(Config.CURRENT_SUMMARY, currentSummary);
        editor.apply();
    }

    public static boolean getIsTempType() {
        return isTempType;
    }

    public static String getCurrentTemp() {
        return sharedPreferences.getString(Config.CURRENT_TEMP, "");
    }

    public static String getCurrentHumidity() {
        return sharedPreferences.getString(Config.CURRENT_HUMIDITY, "");
    }

    public static String getCurrentDate() {
        return sharedPreferences.getString(Config.CURRENT_DATE, "");
    }

    public static String getCurrentSummary() {
        return sharedPreferences.getString(Config.CURRENT_SUMMARY, "");
    }
}
