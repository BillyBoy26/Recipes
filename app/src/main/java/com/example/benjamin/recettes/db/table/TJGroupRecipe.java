package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public class TJGroupRecipe {

    public static final String TJ_GROUP_RECIPE = "TJ_GROUP_RECIPE";
    public static final String C_ID_REG = "ID_REG";
    public static final String C_ID_REC = "ID_REC";

    private static final String CREATE_TABLE_TJGROUPRECIPE =
            "CREATE TABLE " + TJ_GROUP_RECIPE + " (" +
                    C_ID_REG +  " INTEGER, " +
                    C_ID_REC + " INTEGER, " +
                    "PRIMARY KEY (" + C_ID_REG + ", " + C_ID_REC + ") " +
                    "FOREIGN KEY (" + C_ID_REG + ") REFERENCES " +  TRecipeGroup.T_RECIPE_GROUP + "("+ TRecipeGroup._ID + ")" +
                    "FOREIGN KEY (" + C_ID_REC + ") REFERENCES " +  TRecipe.T_RECIPE + "("+ TRecipe._ID + ")" +
                    ")";

    private static final String DELETE_TABLE_TJGROUPRECIPE =
            "DROP TABLE IF EXISTS " + TJ_GROUP_RECIPE;


    private TJGroupRecipe() {
    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TJGROUPRECIPE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_TJGROUPRECIPE);
        onCreate(db);
    }
}
