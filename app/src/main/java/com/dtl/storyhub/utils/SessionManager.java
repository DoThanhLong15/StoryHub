package com.dtl.storyhub.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private static final String PREF_NAME = "StoryHubUserSession";
    private static final String EMAIL_KEY = "emailLogIn";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL_KEY, null);
    }

    public void setLogin(String email) {
        editor.putString(EMAIL_KEY, email);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getString(EMAIL_KEY, null) != null;
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}