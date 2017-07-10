package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class TRecipe implements BaseColumns{

    public static final String T_RECIPE = "T_RECIPE";
    public static final String C_NAME = "NAME";
    public static final String C_URL_IMAGE = "URL_IMAGE";
    public static final String C_DESCRIPTION= "DESCRIPTION";
    public static final String C_INGREDIENTS = "INGREDIENTS";
    public static final String C_STEPS = "STEPS";
    public static final String C_UPDATE_DATE = "UPDATE_DATE";


    private static final String CREATE_TABLE_RECIPE =
            "CREATE TABLE " + TRecipe.T_RECIPE + " (" +
                    TRecipe._ID +  " INTEGER PRIMARY KEY, " +
                    TRecipe.C_NAME + " TEXT, " +
                    TRecipe.C_URL_IMAGE + " TEXT, " +
                    TRecipe.C_DESCRIPTION + " TEXT, " +
                    TRecipe.C_STEPS + " TEXT, " +
                    TRecipe.C_INGREDIENTS + " TEXT, " +
                    TRecipe.C_UPDATE_DATE + " DATE " +
                    ")";

    private static final String DELETE_TABLE_RECIPE =
            "DROP TABLE IF EXISTS " + TRecipe.T_RECIPE;

    private TRecipe() {

    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECIPE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_RECIPE);
        onCreate(db);
    }



}
