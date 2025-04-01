package com.dtl.storyhub.database.tables;

public class UserTable {
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FIRST_NAME + " TEXT NOT NULL, "
            + COLUMN_LAST_NAME + " TEXT NOT NULL, "
            + COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, "
            + COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, "
            + COLUMN_PASSWORD + " TEXT NOT NULL, "
            + COLUMN_AVATAR + " TEXT, "
            + COLUMN_ROLE + " TEXT NOT NULL, "
            + COLUMN_CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + COLUMN_UPDATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

    public static final String CREATE_TRIGGER = "CREATE TRIGGER IF NOT EXISTS update_user_timestamp "
            + "AFTER UPDATE ON " + TABLE_NAME + " "
            + "FOR EACH ROW "
            + "BEGIN "
            + "    UPDATE " + TABLE_NAME + " SET " + COLUMN_UPDATED_AT + " = CURRENT_TIMESTAMP WHERE " + COLUMN_ID + " = OLD." + COLUMN_ID + "; "
            + "END;";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
