package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public final class TRecipe implements TCommon{

    public static final String T_RECIPE = "T_RECIPE";
    public static final String C_NAME = "REC_NAME";
    public static final String C_URL_IMAGE = "REC_URL_IMAGE";
    public static final String C_DESCRIPTION= "REC_DESCRIPTION";
    public static final String C_INGREDIENTS = "REC_INGREDIENTS";
    public static final String C_STEPS = "REC_STEPS";


    private static final String CREATE_TABLE_RECIPE =
            "CREATE TABLE " + T_RECIPE + " (" +
                    _ID +  " INTEGER PRIMARY KEY, " +
                    C_NAME + " TEXT, " +
                    C_URL_IMAGE + " TEXT, " +
                    C_DESCRIPTION + " TEXT, " +
                    C_STEPS + " TEXT, " +
                    C_INGREDIENTS + " TEXT, " +
                    C_UPDATE_DATE + " DATE " +
                    ")";

    private static final String DELETE_TABLE_RECIPE =
            "DROP TABLE IF EXISTS " + T_RECIPE;

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
