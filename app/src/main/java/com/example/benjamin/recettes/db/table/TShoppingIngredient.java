package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public class TShoppingIngredient implements TCommon {
    public static final String T_SHOPPING_INGREDIENT = "T_SHOPPING_INGREDIENT";
    public static final String C_ID_ING = "ID_ING";
    public static final String C_QUANTITY_UNIT = "SIG_QUANTITY_UNIT";
    public static final String C_QUANTITY = "SIG_QUANTITY";

    private static final String CREATE_TABLE_SHOPPING_INGREDIENT =
            "CREATE TABLE " + T_SHOPPING_INGREDIENT + " (" +
                    C_ID_ING +  " INTEGER PRIMARY KEY, " +
                    C_QUANTITY_UNIT + " TEXT, " +
                    C_QUANTITY + " REAL, " +
                    C_UPDATE_DATE + " DATE," +
                    "FOREIGN KEY (" + C_ID_ING + ") REFERENCES " +  TIngredient.T_INGREDIENT + "("+ TIngredient._ID + ")" +
                    ")";

    private static final String DELETE_TABLE_SHOPPING_INGREDIENT =
            "DROP TABLE IF EXISTS " + T_SHOPPING_INGREDIENT;

    private TShoppingIngredient() {

    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SHOPPING_INGREDIENT);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_SHOPPING_INGREDIENT);
        onCreate(db);
    }
}
