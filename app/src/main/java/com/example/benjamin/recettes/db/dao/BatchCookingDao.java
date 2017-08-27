package com.example.benjamin.recettes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.db.table.TRecipe;
import com.example.benjamin.recettes.db.table.TRecipeGroup;
import com.example.benjamin.recettes.shoppingList.BatchCooking.BatchCookingBundle;
import com.example.benjamin.recettes.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class BatchCookingDao extends GenericDao {

    private RecipeDao recipeDao;
    private ShoppingDao shoppingDao;
    private RecipeGroupDao recipeGroupDao;
    public BatchCookingDao(Context context) {
        super(context);
    }

    public BatchCookingDao(SQLiteDatabase sqLiteDatabase) {
        super(sqLiteDatabase);
        initLinkedDao();
    }

    private void initLinkedDao() {
        recipeDao = new RecipeDao(db);
        shoppingDao = new ShoppingDao(db);
        recipeGroupDao = new RecipeGroupDao(db);
    }

    public boolean addRecipeToBatchCooking(Recipe recipe) {
        if (recipe == null || recipe.getId() == null) {
            return false;
        }
        boolean ingAdded = shoppingDao.addIngredientToShoppingList(recipe.getIngredients());

        ContentValues contentValues = new ContentValues();
        contentValues.put(TRecipe.C_IS_BATCH,1);
        int recipeUpdated = db.update(TRecipe.T_RECIPE, contentValues, "_id= ?", new String[]{String.valueOf(recipe.getId())});



        return ingAdded;
    }

    public boolean addRecipeGroupToBatchCooking(RecipeGroup recipeGroup) {
        if (recipeGroup == null || recipeGroup.getId() == null ||
                CollectionUtils.nullOrEmpty(recipeGroup.getRecipes())) {
            return false;
        }
        boolean ingAdded = false;
        for (Recipe recipe : recipeGroup.getRecipes()) {
            ingAdded = shoppingDao.addIngredientToShoppingList(recipe.getIngredients());
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(TRecipeGroup.C_IS_BATCH,1);
        int recipeGroupUpdated = db.update(TRecipeGroup.T_RECIPE_GROUP, contentValues, "_id= ?", new String[]{String.valueOf(recipeGroup.getId())});

        return ingAdded;
    }

    @Override
    public void open() {
        super.open();
        initLinkedDao();
    }

    public List<Ingredient> getShoppingList() {
        return shoppingDao.getShoppingList();
    }

    public BatchCookingBundle getBatchCooking() {
        BatchCookingBundle bundle = new BatchCookingBundle();
        bundle.ingredients = getShoppingList();
        bundle.recipes = getRecipeBatching();
        bundle.recipeGroups = getRecipeGroupBatching();
        return bundle;
    }

    private List<Recipe> getRecipeBatching() {
        Cursor cursor = db.rawQuery("Select " +
                        TRecipe._ID + ", " +
                        TRecipe.C_NAME + ", " +
                        TRecipe.C_URL_IMAGE +
                        " FROM " + TRecipe.T_RECIPE +
                        " WHERE " + TRecipe.C_IS_BATCH + "=1" +
                        " ORDER BY " + TRecipe.C_NAME
                , null);
        List<Recipe> recipes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                recipes.add(RecipeDao.getRecipeFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        recipeDao.fillStepsInRecipe(recipes);
        return recipes;
    }

    public List<RecipeGroup> getRecipeGroupBatching() {
        Cursor cursor = db.rawQuery("Select " +
                        TRecipeGroup._ID + ", " +
                        TRecipeGroup.C_NAME +
                        " FROM " + TRecipeGroup.T_RECIPE_GROUP +
                        " WHERE " + TRecipeGroup.C_IS_BATCH + "=1" +
                        " ORDER BY " + TRecipeGroup.C_NAME
                , null);
        List<RecipeGroup> recipeGroups = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                RecipeGroup recipeGroup = RecipeGroupDao.getRecipeGroupFromCursor(cursor);
                //TODO récup en une fois pour tout les groupes
                recipeGroup.setRecipes(recipeDao.fetchRecipeByRegId(String.valueOf(recipeGroup.getId())));
                recipeGroups.add(recipeGroup);
            } while (cursor.moveToNext());
        }
        for (RecipeGroup recipeGroup : recipeGroups) {
            recipeDao.fillStepsInRecipe(recipeGroup.getRecipes());
        }
        return recipeGroups;
    }

    public void clearBatchCookingRecipes(List<Recipe> recipes) {
        if (CollectionUtils.nullOrEmpty(recipes)) {
            return;
        }
        for (Recipe recipe : recipes) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TRecipe.C_IS_BATCH,0);
            int recipeUpdated = db.update(TRecipe.T_RECIPE, contentValues, "_id= ?", new String[]{String.valueOf(recipe.getId())});
        }

    }

    public void clearBatchCookingRecipeGroups(List<RecipeGroup> recipeGroups) {
        if (CollectionUtils.nullOrEmpty(recipeGroups)) {
            return;
        }
        for (RecipeGroup recGrp : recipeGroups) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TRecipeGroup.C_IS_BATCH,0);
            int recipeGroupUpdated = db.update(TRecipeGroup.T_RECIPE_GROUP, contentValues, "_id= ?", new String[]{String.valueOf(recGrp.getId())});
        }
    }
}
