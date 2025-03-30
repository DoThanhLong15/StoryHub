package com.dtl.storyhub.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dtl.storyhub.database.DatabaseHelper;
import com.dtl.storyhub.database.tables.CategoryTable;
import com.dtl.storyhub.models.Category;
import com.dtl.storyhub.utils.DatabaseLoggerUtil;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private SQLiteDatabase db;
    private final DatabaseHelper dbHelper;
    private static final String[] categoryColumns = {
        CategoryTable.COLUMN_ID,
        CategoryTable.COLUMN_NAME,
        CategoryTable.COLUMN_DESCRIPTION,
        CategoryTable.COLUMN_CREATED_AT,
        CategoryTable.COLUMN_UPDATED_AT
    };

    public CategoryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    private void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    private void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public Category insertCategory(Category category) {
        open();
        ContentValues values = new ContentValues();
        values.put(CategoryTable.COLUMN_NAME, category.getName());
        values.put(CategoryTable.COLUMN_DESCRIPTION, category.getDescription());

        long insertId = db.insert(CategoryTable.TABLE_NAME, null, values);
        close();
        category = getCategoryById((int) insertId);
        DatabaseLoggerUtil.logInsert(CategoryTable.TABLE_NAME, category);
        return category;
    }

    public Category updateCategory(Category category) {
        open();
        ContentValues values = new ContentValues();
        values.put(CategoryTable.COLUMN_NAME, category.getName());
        values.put(CategoryTable.COLUMN_DESCRIPTION, category.getDescription());

        int rows = db.update(CategoryTable.TABLE_NAME, values, CategoryTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
        close();
        category = getCategoryById(category.getId());
        DatabaseLoggerUtil.logUpdate(CategoryTable.TABLE_NAME, category);
        return category;
    }

    public int deleteCategory(int id) {
        open();
        int rows = db.delete(CategoryTable.TABLE_NAME, CategoryTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        close();
        return rows;
    }

    public Category getCategoryById(int id) {
        open();
        Cursor cursor = db.query(CategoryTable.TABLE_NAME, categoryColumns, CategoryTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            Category category = cursorToCategory(cursor);
            cursor.close();
            close();
            return category;
        }

        close();
        return null;
    }

    public List<Category> getAllCategories() {
        open();
        List<Category> categoryList = new ArrayList<>();
        Cursor cursor = db.query(CategoryTable.TABLE_NAME, categoryColumns, null, null, null, null, CategoryTable.COLUMN_CREATED_AT + " DESC");

        while (cursor.moveToNext()) {
            categoryList.add(cursorToCategory(cursor));
        }
        cursor.close();
        close();
        return categoryList;
    }

    private Category cursorToCategory(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(CategoryTable.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(CategoryTable.COLUMN_NAME));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(CategoryTable.COLUMN_DESCRIPTION));
        String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(CategoryTable.COLUMN_CREATED_AT));
        String updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(CategoryTable.COLUMN_UPDATED_AT));

        return new Category(id, name, description, createdAt, updatedAt);
    }
}

