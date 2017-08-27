package com.example.benjamin.recettes.db.table;

import android.database.sqlite.SQLiteDatabase;

public final class TRecipe implements TCommon{

    public static final String T_RECIPE = "T_RECIPE";
    public static final String C_NAME = "REC_NAME";
    public static final String C_URL_IMAGE = "REC_URL_IMAGE";
    public static final String C_URL_IMAGE_2 = "REC_URL_IMAGE_2";
    public static final String C_URL_IMAGE_3 = "REC_URL_IMAGE_3";
    public static final String C_URL_IMAGE_4 = "REC_URL_IMAGE_4";
    public static final String C_URL_IMAGE_5 = "REC_URL_IMAGE_5";
    public static final String C_DESCRIPTION= "REC_DESCRIPTION";
    public static final String C_STEPS = "REC_STEPS";
    public static final String C_PREPARE_TIME = "REC_PREPARE_TIME";
    public static final String C_COOK_TIME = "REC_COOK_TIME";
    public static final String C_TOTAL_TIME = "REC_TOTAL_TIME";
    public static final String C_NB_COVERS = "REC_NB_COVERS";
    public static final String C_IS_BATCH = "REC_IS_BATCH";
    public static final String C_URL_VIDEO = "REC_URL_VIDEO";


    private static final String CREATE_TABLE_RECIPE =
            "CREATE TABLE " + T_RECIPE + " (" +
                    _ID +  " INTEGER PRIMARY KEY, " +
                    C_NAME + " TEXT, " +
                    C_URL_IMAGE + " TEXT, " +
                    C_URL_IMAGE_2 + " TEXT, " +
                    C_URL_IMAGE_3 + " TEXT, " +
                    C_URL_IMAGE_4 + " TEXT, " +
                    C_URL_IMAGE_5 + " TEXT, " +
                    C_DESCRIPTION + " TEXT, " +
                    C_STEPS + " TEXT, " +
                    C_PREPARE_TIME + " TEXT, " +
                    C_COOK_TIME + " TEXT, " +
                    C_TOTAL_TIME + " TEXT, " +
                    C_NB_COVERS + " TEXT, " +
                    C_IS_BATCH + " INTEGER, " +
                    C_URL_VIDEO + " TEXT, " +
                    C_UPDATE_DATE + " DATE " +
                    ")";

    private static final String DELETE_TABLE_RECIPE =
            "DROP TABLE IF EXISTS " + T_RECIPE;

    private TRecipe() {

    }

    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECIPE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
        db.execSQL(DELETE_TABLE_RECIPE);
        onCreate(db);
    }



}
