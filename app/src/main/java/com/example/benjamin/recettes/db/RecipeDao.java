package com.example.benjamin.recettes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.dao.GenericDao;
import com.example.benjamin.recettes.db.table.TRecipe;

public class RecipeDao extends GenericDao{


    public RecipeDao(Context context) {
        super(context);
    }

    public Cursor getAllRecipes() {
        String[] columns = new String[]{TRecipe.C_NAME, TRecipe._ID,
                TRecipe.C_URL_IMAGE, TRecipe.C_INGREDIENTS, TRecipe.C_STEPS, TRecipe.C_UPDATE_DATE};
        return db.query(TRecipe.T_RECIPE, columns, null, null, null, null, TRecipe.C_UPDATE_DATE + " DESC");
    }

    public Recipe createOrUpdate(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRecipe.C_NAME,recipe.getName());
        contentValues.put(TRecipe.C_URL_IMAGE,recipe.getUrlImage());
        contentValues.put(TRecipe.C_INGREDIENTS,recipe.getIngredientsAsString());
        contentValues.put(TRecipe.C_STEPS,recipe.getStepsAsString());


        long idRecipe;
        if (recipe.getId() != null) {
            contentValues.put(TRecipe._ID, recipe.getId());
            idRecipe = db.update(TRecipe.T_RECIPE,contentValues,"_id=" + recipe.getId(),null);
        } else {
            idRecipe = db.insert(TRecipe.T_RECIPE, null, contentValues);

        }
        recipe.setId(idRecipe);
        return recipe;
    }

    /**
     * http://vogella.developpez.com/tutoriels/android/utilisation-base-donnees-sqlite/
     */
    public boolean delete(Recipe recipe) {
        if (recipe == null || recipe.getId() == null) {
            return false;
        }

        db.delete(TRecipe.T_RECIPE, TRecipe._ID + "=" + recipe.getId(), null);
        return true;
    }

    public static Recipe getRecipeFromCursor(Cursor cursor) {
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getLong(cursor.getColumnIndex(TRecipe._ID)));
        recipe.setUrlImage(cursor.getString(cursor.getColumnIndex(TRecipe.C_URL_IMAGE)));
        recipe.setName(cursor.getString(cursor.getColumnIndex(TRecipe.C_NAME)));
        recipe.setIngredients(cursor.getString(cursor.getColumnIndex(TRecipe.C_INGREDIENTS)));
        recipe.setSteps(cursor.getString(cursor.getColumnIndex(TRecipe.C_STEPS)));
        return recipe;
    }

}
