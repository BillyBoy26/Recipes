package com.example.benjamin.recettes.db.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.benjamin.recettes.db.RecipeDbHelper;
import com.example.benjamin.recettes.db.table.TCommon;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GenericDao {
    protected RecipeDbHelper dbHelper;
    protected SQLiteDatabase db;

    public GenericDao(Context context) {
        dbHelper = new RecipeDbHelper(context);
    }

    public GenericDao(SQLiteDatabase sqLiteDatabase) {
        db = sqLiteDatabase;
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }


    public void close() {
        dbHelper.close();
    }

    protected void fillUpdatedate(ContentValues contentValues) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        contentValues.put(TCommon.C_UPDATE_DATE, format.format(new Date()));
    }
    protected String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

}
