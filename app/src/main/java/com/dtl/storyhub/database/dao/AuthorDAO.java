package com.dtl.storyhub.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dtl.storyhub.database.DatabaseHelper;
import com.dtl.storyhub.database.tables.AuthorTable;
import com.dtl.storyhub.models.Author;
import com.dtl.storyhub.utils.DatabaseLoggerUtil;

import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {
    private SQLiteDatabase db;
    private final DatabaseHelper dbHelper;
    private static final String[] authorColumns = {
            AuthorTable.COLUMN_ID,
            AuthorTable.COLUMN_NAME
    };

    public AuthorDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    private void open() {
        db = dbHelper.getWritableDatabase();
    }

    private void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public Author insertAuthor(Author author) {
        open();
        ContentValues values = new ContentValues();
        values.put(AuthorTable.COLUMN_NAME, author.getName());

        long result = db.insert(AuthorTable.TABLE_NAME, null, values);
        close();
        author = getAuthorById((int) result);
        DatabaseLoggerUtil.logInsert(AuthorTable.TABLE_NAME, author);
        return author;
    }

    public List<Author> getAllAuthors() {
        List<Author> authorList = new ArrayList<>();
        open();
        Cursor cursor = db.query(AuthorTable.TABLE_NAME, authorColumns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            authorList.add(cursorToAuthor(cursor));
        }
        cursor.close();
        close();
        return authorList;
    }

    public Author getAuthorById(long id) {
        open();
        Cursor cursor = db.query(AuthorTable.TABLE_NAME, authorColumns, AuthorTable.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            Author author = cursorToAuthor(cursor);
            cursor.close();
            close();
            return author;
        }
        cursor.close();
        close();
        return null;
    }

    public Author updateAuthor(Author author) {
        open();
        ContentValues values = new ContentValues();
        values.put(AuthorTable.COLUMN_NAME, author.getName());

        db.update(AuthorTable.TABLE_NAME, values, AuthorTable.COLUMN_ID + " = ?", new String[]{String.valueOf(author.getId())});
        close();

        author = getAuthorById(author.getId());
        DatabaseLoggerUtil.logUpdate(AuthorTable.TABLE_NAME, author);
        return author;
    }

    public int deleteAuthor(long id) {
        open();
        int rowsDeleted = db.delete(AuthorTable.TABLE_NAME, AuthorTable.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        close();
        return rowsDeleted;
    }

    private Author cursorToAuthor(Cursor cursor) {
        Author author = new Author();
        author.setId(cursor.getLong(cursor.getColumnIndexOrThrow(AuthorTable.COLUMN_ID)));
        author.setName(cursor.getString(cursor.getColumnIndexOrThrow(AuthorTable.COLUMN_NAME)));
        return author;
    }
}

