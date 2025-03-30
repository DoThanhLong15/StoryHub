package com.dtl.storyhub.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dtl.storyhub.database.DatabaseHelper;
import com.dtl.storyhub.database.tables.UserTable;
import com.dtl.storyhub.enums.UserRole;
import com.dtl.storyhub.models.User;
import com.dtl.storyhub.utils.DatabaseLoggerUtil;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final SQLiteDatabase db;

    private static final String[] userColumns = {
            UserTable.COLUMN_ID,
            UserTable.COLUMN_FIRST_NAME,
            UserTable.COLUMN_LAST_NAME,
            UserTable.COLUMN_USERNAME,
            UserTable.COLUMN_EMAIL,
            UserTable.COLUMN_PASSWORD,
            UserTable.COLUMN_AVATAR,
            UserTable.COLUMN_ROLE,
            UserTable.COLUMN_CREATED_AT,
            UserTable.COLUMN_UPDATED_AT
    };

    public UserDAO(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        db = dbHelper.openDatabase();
    }

//    private void open() throws SQLException {
//        db = dbHelper.getWritableDatabase();
//    }
//
//    private void close() {
//        if (db != null && db.isOpen()) {
//            db.close();
//        }
//    }

    public User insertUser(User user) {
        if (isEmailExists(user.getEmail())) {
            Log.e("DB_Error " + UserTable.TABLE_NAME, "Email already exists: " + user.getEmail());
            return null;
        }

        ContentValues values = new ContentValues();
        values.put(UserTable.COLUMN_FIRST_NAME, user.getFirstName());
        values.put(UserTable.COLUMN_LAST_NAME, user.getLastName());
        values.put(UserTable.COLUMN_USERNAME, user.getUsername());
        values.put(UserTable.COLUMN_EMAIL, user.getEmail());
        values.put(UserTable.COLUMN_PASSWORD, user.getPassword());
        values.put(UserTable.COLUMN_AVATAR, user.getAvatar());
        values.put(UserTable.COLUMN_ROLE, user.getRole());

        long result = db.insert(UserTable.TABLE_NAME, null, values);
        user = getUserById((int) result);
        DatabaseLoggerUtil.logInsert(UserTable.TABLE_NAME, user);
        return user;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        Cursor cursor = db.query(UserTable.TABLE_NAME, userColumns, null, null, null, null, UserTable.COLUMN_CREATED_AT + " DESC");

        while (cursor.moveToNext()) {
            userList.add(cursorToUser(cursor));
        }
        cursor.close();
        return userList;
    }

    public User getUserById(int id) {
        Cursor cursor = db.query(UserTable.TABLE_NAME, userColumns, UserTable.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            User user = cursorToUser(cursor);
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public User updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(UserTable.COLUMN_FIRST_NAME, user.getFirstName());
        values.put(UserTable.COLUMN_LAST_NAME, user.getLastName());
        values.put(UserTable.COLUMN_USERNAME, user.getUsername());
        values.put(UserTable.COLUMN_EMAIL, user.getEmail());
        values.put(UserTable.COLUMN_AVATAR, user.getAvatar());

        int rowsAffected = db.update(UserTable.TABLE_NAME, values, UserTable.COLUMN_ID + " = ?", new String[]{String.valueOf(user.getId())});
        user = getUserById(user.getId());
        DatabaseLoggerUtil.logUpdate(UserTable.TABLE_NAME, user);
        return user;
    }

    public int deleteUser(int id) {
        int rowsDeleted = db.delete(UserTable.TABLE_NAME, UserTable.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return rowsDeleted;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COLUMN_ID)));
        user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_FIRST_NAME)));
        user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_LAST_NAME)));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_USERNAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_EMAIL)));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_PASSWORD)));
        user.setAvatar(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_AVATAR)));
        user.setRole(UserRole.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_ROLE))));
        user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_CREATED_AT)));
        user.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_UPDATED_AT)));
        return user;
    }

    public boolean isEmailExists(String email) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.COLUMN_EMAIL + " = ?", new String[]{email});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}
