package com.dtl.storyhub.database.tables;

public class CategoryTable {
    public static final String TABLE_NAME = "category";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    COLUMN_UPDATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    public static final String CREATE_TRIGGER =
            "CREATE TRIGGER IF NOT EXISTS update_category_timestamp " +
                    "AFTER UPDATE ON " + TABLE_NAME + " " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "    UPDATE " + TABLE_NAME + " SET " + COLUMN_UPDATED_AT + " = CURRENT_TIMESTAMP WHERE " + COLUMN_ID + " = OLD.id; " +
                    "END;";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}

