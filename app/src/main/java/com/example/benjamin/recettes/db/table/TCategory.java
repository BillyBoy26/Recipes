package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public class TCategory implements TCommon {

    public static final String T_CATEGORY = "T_CATEGORY";
    public static final String C_NAME = "CAT_NAME";
    public static final String CR_UNIQUE_NAME = "CAT_CR_UNIQUE_NAME";

    private static final String CREATE_TABLE_CATEGORY =
            "CREATE TABLE " + T_CATEGORY + " (" +
                    _ID +  " INTEGER PRIMARY KEY, " +
                    C_NAME + " TEXT UNIQUE, " +
                    C_UPDATE_DATE + " DATE, " +
                    "CONSTRAINT "+ CR_UNIQUE_NAME + " UNIQUE (" + C_NAME +")" +
                    ")";

    private static final String DELETE_TABLE_CATEGORY =
            "DROP TABLE IF EXISTS " + T_CATEGORY;

    private TCategory() {
    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_CATEGORY);
        onCreate(db);
    }

}
