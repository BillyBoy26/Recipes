package com.example.benjamin.recettes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.table.TRecipe;

public class RecipeDao extends GenericDao{

    private CategoryDao categoryDao;

    public RecipeDao(Context context) {
        super(context);
    }

    public Cursor getAllRecipes() {
        String[] columns = new String[]{TRecipe.C_NAME, TRecipe._ID,
                TRecipe.C_URL_IMAGE, TRecipe.C_INGREDIENTS, TRecipe.C_STEPS, TRecipe.C_UPDATE_DATE};
        return db.query(TRecipe.T_RECIPE, columns, null, null, null, null, TRecipe.C_UPDATE_DATE + " DESC");
    }

//    public Cursor getAllRecipes() {
//        String[] columns = new String[]{TRecipe.C_NAME, TRecipe._ID,
//                TRecipe.C_URL_IMAGE, TRecipe.C_INGREDIENTS, TRecipe.C_STEPS, TRecipe.C_UPDATE_DATE};
//
//
//        String select_statement = "select " +
//                "recipe._id recId, " +
//                "category._id catId, " +
//                TRecipe.C_NAME + ", " +
//                TRecipe.C_URL_IMAGE + ", " +
//                TRecipe.C_INGREDIENTS + ", " +
//                TRecipe.C_STEPS + ", " +
//                "recipe.update_date recUpdateDate," +
//                TCategory.C_NAME +
//                " FROM " + TRecipe.T_RECIPE + " recipe" +
//                " LEFT OUTER JOIN " + TJCatRecipe.TJ_CAT_RECIPE + " linkRecCat ON " + TJCatRecipe.C_ID_RECIPE + " = recId " +
//                " LEFT OUTER JOIN " + TCategory.T_CATEGORY + " category ON " + TJCatRecipe.C_ID_CAT + " = catId " +
//                " ORDER BY recUpdateDate DESC" ;
//
//        return db.rawQuery(select_statement,null);
//    }

    public Recipe createOrUpdate(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRecipe.C_NAME,recipe.getName());
        contentValues.put(TRecipe.C_URL_IMAGE,recipe.getUrlImage());
        contentValues.put(TRecipe.C_INGREDIENTS,recipe.getIngredientsAsString());
        contentValues.put(TRecipe.C_STEPS,recipe.getStepsAsString());
        fillUpdatedate(contentValues);


        if (recipe.getId() != null) {
            contentValues.put(TRecipe._ID, recipe.getId());
            db.update(TRecipe.T_RECIPE,contentValues,"_id=" + recipe.getId(),null);
        } else {
            Long newIdRecipe = db.insert(TRecipe.T_RECIPE, null, contentValues);
            recipe.setId(newIdRecipe);
        }


        linkRecipeToCategories(recipe);
        return recipe;
    }

    private void linkRecipeToCategories(Recipe recipe) {
        recipe.setCategories(categoryDao.createCategoriesIfNeeded(recipe.getCategories()));
        categoryDao.deleteLinkRecipeCat(recipe);
        categoryDao.createLinkRecipeCat(recipe);
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

    @Override
    public void open() {
        super.open();
        categoryDao = new CategoryDao(db);
    }

    public Recipe findById(Long recipeId) {
        String recIdStr = Long.toString(recipeId);
        Cursor cursor = db.rawQuery("Select * from " + TRecipe.T_RECIPE + " where " + TRecipe._ID + " = ?",new String[]{recIdStr});
        if (!cursor.moveToFirst()) {
            return null;
        }
        Recipe recipe = getRecipeFromCursor(cursor);
        if (recipe == null) {
            return null;
        }
        recipe.setCategories(categoryDao.fetchCategoriesByRecId(recIdStr));
        return recipe;
    }
}
