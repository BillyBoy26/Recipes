package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public class TTags implements TCommon {

    public static final String T_TAGS = "T_TAGS";
    public static final String C_NAME = "TAG_NAME";
    public static final String CR_UNIQUE_NAME = "TAG_CR_UNIQUE_NAME";

    private static final String CREATE_TABLE_TAGS =
            "CREATE TABLE " + T_TAGS + " (" +
                    _ID +  " INTEGER PRIMARY KEY, " +
                    C_NAME + " TEXT UNIQUE, " +
                    C_UPDATE_DATE + " DATE, " +
                    "CONSTRAINT "+ CR_UNIQUE_NAME + " UNIQUE (" + C_NAME +")" +
                    ")";

    private static final String DELETE_TABLE_TAGS =
            "DROP TABLE IF EXISTS " + T_TAGS;

    private TTags() {
    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TAGS);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_TAGS);
        onCreate(db);
    }
}
