package com.dtl.storyhub.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dtl.storyhub.database.tables.AuthorTable;
import com.dtl.storyhub.database.tables.CategoryTable;
import com.dtl.storyhub.database.tables.UserTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "story_hub.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper instance;
    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (database == null || !database.isOpen()) {
            database = getWritableDatabase();
        }
        return database;
    }

    public synchronized void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
            database = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table
        db.execSQL(UserTable.CREATE_TABLE);
        db.execSQL(AuthorTable.CREATE_TABLE);
        db.execSQL(CategoryTable.CREATE_TABLE);

        // create trigger
        db.execSQL(UserTable.CREATE_TRIGGER);
        db.execSQL(CategoryTable.CREATE_TRIGGER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserTable.DROP_TABLE);
        db.execSQL(AuthorTable.DROP_TABLE);
        db.execSQL(CategoryTable.DROP_TABLE);

        onCreate(db);
    }
}
