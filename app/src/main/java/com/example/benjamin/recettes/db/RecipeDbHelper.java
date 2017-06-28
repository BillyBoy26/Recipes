package com.example.benjamin.recettes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.benjamin.recettes.db.table.TRecipe;

public class RecipeDbHelper  extends SQLiteOpenHelper{


    public static final String RECIPE_DB_NAME = "recipe.db";
    public static final int RECIPE_DB_VERSION = 3;



    public RecipeDbHelper(Context context) {
        super(context, RECIPE_DB_NAME, null, RECIPE_DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        TRecipe.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TRecipe.onUpgrade(db,oldVersion,newVersion);
    }




}