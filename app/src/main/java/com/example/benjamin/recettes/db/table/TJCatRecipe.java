package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public class TJCatRecipe {

    public static final String TJ_CAT_RECIPE = "TJ_CAT_RECIPE";
    public static final String C_ID_CAT = "ID_CAT";
    public static final String C_ID_RECIPE = "ID_RECIPE";

    private static final String CREATE_TABLE_TJCATRECIPE =
            "CREATE TABLE " + TJ_CAT_RECIPE + " (" +
                    C_ID_CAT +  " INTEGER, " +
                    C_ID_RECIPE + " INTEGER, " +
                    "PRIMARY KEY (" + C_ID_CAT + ", " + C_ID_RECIPE + ") " +
                    "FOREIGN KEY (" + C_ID_CAT + ") REFERENCES " +  TCategory.T_CATEGORY + "("+ TCategory._ID + ")" +
                    "FOREIGN KEY (" + C_ID_RECIPE + ") REFERENCES " +  TRecipe.T_RECIPE + "("+ TRecipe._ID + ")" +
                    ")";

    private static final String DELETE_TABLE_TJCATRECIPE =
            "DROP TABLE IF EXISTS " + TJ_CAT_RECIPE;

    private TJCatRecipe() {

    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TJCATRECIPE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_TJCATRECIPE);
        onCreate(db);
    }
}
