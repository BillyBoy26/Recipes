package com.example.benjamin.recettes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.benjamin.recettes.db.table.TCategory;
import com.example.benjamin.recettes.db.table.TIngredient;
import com.example.benjamin.recettes.db.table.TJCatRecipe;
import com.example.benjamin.recettes.db.table.TJGroupRecipe;
import com.example.benjamin.recettes.db.table.TJIngRecipe;
import com.example.benjamin.recettes.db.table.TRecipe;
import com.example.benjamin.recettes.db.table.TRecipeGroup;
import com.example.benjamin.recettes.db.table.TShoppingIngredient;

public class RecipeDbHelper  extends SQLiteOpenHelper{


    public static final String RECIPE_DB_NAME = "recipe.db";
    public static final int RECIPE_DB_VERSION = 20;



    public RecipeDbHelper(Context context) {
        super(context, RECIPE_DB_NAME, null, RECIPE_DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        TCategory.onCreate(db);
        TRecipe.onCreate(db);
        TIngredient.onCreate(db);
        TRecipeGroup.onCreate(db);
        TJCatRecipe.onCreate(db);
        TJIngRecipe.onCreate(db);
        TJGroupRecipe.onCreate(db);
        TShoppingIngredient.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TCategory.onUpgrade(db,oldVersion,newVersion);
        TRecipe.onUpgrade(db,oldVersion,newVersion);
        TIngredient.onUpgrade(db,oldVersion,newVersion);
        TRecipeGroup.onUpgrade(db,oldVersion,newVersion);
        TJCatRecipe.onUpgrade(db,oldVersion,newVersion);
        TJIngRecipe.onUpgrade(db,oldVersion,newVersion);
        TJGroupRecipe.onUpgrade(db,oldVersion,newVersion);
        TShoppingIngredient.onUpgrade(db,oldVersion,newVersion);
    }




}
