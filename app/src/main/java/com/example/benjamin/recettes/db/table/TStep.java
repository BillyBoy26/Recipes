package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public class TStep implements TCommon {

    public static final String T_STEP = "T_STEP";
    public static final String C_NAME = "STE_NAME";
    public static final String C_RANK = "STE_RANK";
    public static final String C_ID_REC = "ID_REC";

    private static final String CREATE_TABLE_STEP =
            "CREATE TABLE " + T_STEP + " (" +
                    _ID +  " INTEGER PRIMARY KEY, " +
                    C_NAME + " TEXT, " +
                    C_RANK + " INTEGER, " +
                    C_ID_REC + " INTEGER, " +
                    C_UPDATE_DATE + " DATE ," +
                    "FOREIGN KEY (" + C_ID_REC + ") REFERENCES " +  TRecipe.T_RECIPE + "("+ TRecipe._ID + ")" +
                    ")";

    private static final String DELETE_TABLE_STEP =
            "DROP TABLE IF EXISTS " + T_STEP;

    private TStep() {
    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STEP);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_STEP);
        onCreate(db);
    }

}
