package com.example.administrator.mysocialapp.utils;

import android.content.Context;

import static android.content.Context.MODE_PRIVATE;

public class SPUtils {
    private static final String SP_NAME = "user";
    private static final String LAST_LOGIN_USERNAME = "LastLoginUserName";

    public static void setLastLoginUserName(Context context, String userName) {

        context.getSharedPreferences(SP_NAME, MODE_PRIVATE)
                .edit()
                .putString(LAST_LOGIN_USERNAME, userName)
                .apply();
    }

    public static String getLastLoginUserName(Context context) {

        return context.getSharedPreferences(SP_NAME, MODE_PRIVATE)
                .getString(LAST_LOGIN_USERNAME, "");

    }
}
