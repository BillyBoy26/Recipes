package com.example.benjamin.recettes.utils;

import android.database.Cursor;

public class CursorUtils {

    public static String getStringColumnOrEmpty(Cursor cursor, String columnName) {
        if (cursor.getColumnIndex(columnName) == -1) {
            return "";
        }
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
    public static Long getLongColumnOrNull(Cursor cursor, String columnName) {
        if (cursor.getColumnIndex(columnName) == -1) {
            return null;
        }
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
    public static Float getFloatColumnOrNull(Cursor cursor, String columnName) {
        if (cursor.getColumnIndex(columnName) == -1) {
            return null;
        }
        return cursor.getFloat(cursor.getColumnIndex(columnName));
    }
}
