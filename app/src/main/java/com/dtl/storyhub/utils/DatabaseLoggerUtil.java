package com.dtl.storyhub.utils;

import android.util.Log;

public class DatabaseLoggerUtil {
    private static final String TAG = "DatabaseLogger";

    public static void logInsert(String tableName, Object object) {
        Log.d(TAG, "Inserted into " + tableName + ": " + object.toString());
    }

    public static void logUpdate(String tableName, Object object) {
        Log.d(TAG, "Updated " + tableName + ": " + object.toString());
    }
}
