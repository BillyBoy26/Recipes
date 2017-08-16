package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public class TJIngRecipe  {
    public static final String TJ_ING_RECIPE = "TJ_ING_RECIPE";
    public static final String C_ID_ING = "ID_ING";
    public static final String C_ID_RECIPE = "ID_RECIPE";
    public static final String C_QUANTITY_UNIT = "ING_QUANTITY_UNIT";
    public static final String C_QUANTITY = "ING_QUANTITY";

    private static final String CREATE_TABLE_TJINGRECIPE =
            "CREATE TABLE " + TJ_ING_RECIPE + " (" +
                    C_ID_ING +  " INTEGER, " +
                    C_ID_RECIPE + " INTEGER, " +
                    C_QUANTITY_UNIT + " TEXT, " +
                    C_QUANTITY + " REAL, " +
                    "PRIMARY KEY (" + C_ID_ING + ", " + C_ID_RECIPE + ") " +
                    "FOREIGN KEY (" + C_ID_ING + ") REFERENCES " +  TIngredient.T_INGREDIENT + "("+ TIngredient._ID + ")" +
                    "FOREIGN KEY (" + C_ID_RECIPE + ") REFERENCES " +  TRecipe.T_RECIPE + "("+ TRecipe._ID + ")" +
                    ")";

    private static final String DELETE_TABLE_TJINGRECIPE =
            "DROP TABLE IF EXISTS " + TJ_ING_RECIPE;

    private TJIngRecipe() {

    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TJINGRECIPE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_TJINGRECIPE);
        onCreate(db);
    }
}
