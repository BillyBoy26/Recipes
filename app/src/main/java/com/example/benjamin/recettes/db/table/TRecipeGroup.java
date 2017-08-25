package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public class TRecipeGroup implements TCommon {

    public static final String T_RECIPE_GROUP = "T_RECIPE_GROUP";
    public static final String C_NAME = "REG_NAME";
    public static final String C_IS_BATCH = "REG_IS_BATCH";


    private static final String CREATE_TABLE_RECIPE_GROUP =
            "CREATE TABLE " + T_RECIPE_GROUP + " (" +
                    _ID +  " INTEGER PRIMARY KEY, " +
                    C_NAME + " TEXT, " +
                    C_IS_BATCH + " INTEGER, " +
                    C_UPDATE_DATE + " DATE " +
                    ")";

    private static final String DELETE_TABLE_RECIPE_GROUP =
            "DROP TABLE IF EXISTS " + T_RECIPE_GROUP;

    private TRecipeGroup() {

    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECIPE_GROUP);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_RECIPE_GROUP);
        onCreate(db);
    }
}
