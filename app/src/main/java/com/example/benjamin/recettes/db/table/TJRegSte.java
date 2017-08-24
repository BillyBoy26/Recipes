package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public class TJRegSte {

    public static final String TJ_REG_STE = "TJ_REG_STE";
    public static final String C_ID_REG = "ID_REG";
    public static final String C_ID_STEP = "ID_STEP";
    public static final String C_RANK = "STG_RANK";

    private static final String CREATE_TABLE_TJREGSTE =
            "CREATE TABLE " + TJ_REG_STE + " (" +
                    C_ID_REG +  " INTEGER, " +
                    C_ID_STEP + " INTEGER, " +
                    C_RANK + " INTEGER, " +
                    "PRIMARY KEY (" + C_ID_REG + ", " + C_ID_STEP + ") " +
                    "FOREIGN KEY (" + C_ID_STEP + ") REFERENCES " +  TStep.T_STEP + "("+ TStep._ID + ")" +
                    "FOREIGN KEY (" + C_ID_REG + ") REFERENCES " +  TRecipeGroup.T_RECIPE_GROUP + "("+ TRecipeGroup._ID + ")" +
                    ")";

    private static final String DELETE_TABLE_TJREGSTE =
            "DROP TABLE IF EXISTS " + TJ_REG_STE;

    private TJRegSte() {

    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TJREGSTE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_TJREGSTE);
        onCreate(db);
    }
}
