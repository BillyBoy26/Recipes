package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public class TIngredient implements TCommon {

    public static final String T_INGREDIENT = "T_INGREDIENT";
    public static final String C_NAME = "ING_NAME";

    private static final String CREATE_TABLE_INGREDIENT =
            "CREATE TABLE " + T_INGREDIENT + " (" +
                    _ID +  " INTEGER PRIMARY KEY, " +
                    C_NAME + " TEXT, " +
                    C_UPDATE_DATE + " DATE " +
                    ")";

    private static final String DELETE_TABLE_INGREDIENT =
            "DROP TABLE IF EXISTS " + T_INGREDIENT;

    private TIngredient() {

    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INGREDIENT);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_INGREDIENT);
        onCreate(db);
    }
}
