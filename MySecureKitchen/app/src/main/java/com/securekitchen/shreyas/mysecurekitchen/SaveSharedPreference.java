package com.securekitchen.shreyas.mysecurekitchen;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "productcode";
    static final String PREF_DEVICE_TOKEN= "devicetoken";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setDeviceToken(Context ctx, String deviceToken)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_DEVICE_TOKEN, deviceToken);
        editor.commit();
    }

    public static String getDeviceToken(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_DEVICE_TOKEN, "");
    }
}
