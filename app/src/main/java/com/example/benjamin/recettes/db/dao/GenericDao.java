package com.example.benjamin.recettes.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.benjamin.recettes.db.RecipeDbHelper;

public class GenericDao {
    protected RecipeDbHelper dbHelper;
    protected SQLiteDatabase db;

    public GenericDao(Context context) {
        dbHelper = new RecipeDbHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }


    public void close() {
        dbHelper.close();
    }
}
