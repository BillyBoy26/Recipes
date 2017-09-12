package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public class TJTagRecipe {

    public static final String TJ_TAG_REC = "TJ_TAG_REC";
    public static final String C_ID_REC = "ID_REC";
    public static final String C_ID_TAG = "ID_TAG";

    private static final String CREATE_TABLE_TJTAGREC =
            "CREATE TABLE " + TJ_TAG_REC + " (" +
                    C_ID_REC +  " INTEGER, " +
                    C_ID_TAG + " INTEGER, " +
                    "PRIMARY KEY (" + C_ID_REC + ", " + C_ID_TAG + ") " +
                    "FOREIGN KEY (" + C_ID_TAG + ") REFERENCES " +  TTags.T_TAGS + "("+ TTags._ID + ")" +
                    "FOREIGN KEY (" + C_ID_REC + ") REFERENCES " +  TRecipe.T_RECIPE + "("+ TRecipe._ID + ")" +
                    ")";

    private static final String DELETE_TABLE_TJTAGREC =
            "DROP TABLE IF EXISTS " + TJ_TAG_REC;

    private TJTagRecipe() {

    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TJTAGREC);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_TJTAGREC);
        onCreate(db);
    }
}
