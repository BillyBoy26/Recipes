package com.example.benjamin.recettes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.Step;
import com.example.benjamin.recettes.db.table.TJGroupRecipe;
import com.example.benjamin.recettes.db.table.TRecipe;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.SUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static com.example.benjamin.recettes.data.Recipe.RecipeFiller.WITH_CAT;
import static com.example.benjamin.recettes.data.Recipe.RecipeFiller.WITH_ING;
import static com.example.benjamin.recettes.data.Recipe.RecipeFiller.WITH_STEPS;
import static com.example.benjamin.recettes.utils.CursorUtils.getStringColumnOrEmpty;

public class RecipeDao extends GenericDao{

    private CategoryDao categoryDao;
    private IngredientDao ingredientDao;
    private StepDao stepDao;

    public RecipeDao(Context context) {
        super(context);
    }

    public RecipeDao(SQLiteDatabase db) {
        super(db);
        initLinkDao();
    }

    private void initLinkDao() {
        categoryDao = new CategoryDao(db);
        ingredientDao = new IngredientDao(db);
        stepDao = new StepDao(db);
    }

    public Cursor getAllRecipesAsCursor() {
        String[] columns = new String[]{TRecipe.C_NAME, TRecipe._ID,
                TRecipe.C_URL_IMAGE, TRecipe.C_STEPS, TRecipe.C_UPDATE_DATE};
        return db.query(TRecipe.T_RECIPE, columns, null, null, null, null, TRecipe.C_UPDATE_DATE + " DESC");
    }

    public List<Recipe> getAllRecipes() {
        return getAllRecipes(EnumSet.noneOf(Recipe.RecipeFiller.class));
    }

    public List<Recipe> getAllRecipes(EnumSet<Recipe.RecipeFiller> recipeFillers) {
        Cursor cursor = getAllRecipesAsCursor();
        List<Recipe> recipes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                recipes.add(getRecipeFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        if (recipeFillers.contains(WITH_ING)) {
            fillIngredientsInRecipe(recipes);
        }
        if (recipeFillers.contains(WITH_STEPS)) {
            fillStepsInRecipe(recipes);
        }
        if (recipeFillers.contains(WITH_CAT)) {
            fillCatsInRecipes(recipes);
        }
        return recipes;
    }

    private void fillCatsInRecipes(List<Recipe> recipes) {
        List<String> ids = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ids.add(String.valueOf(recipe.getId()));
        }
        Map<Long, List<Category>> catsByRecID = categoryDao.fetchCategoriesByRecId(ids);
        if (!catsByRecID.isEmpty()) {
            for (Recipe recipe : recipes) {
                if (catsByRecID.containsKey(recipe.getId())) {
                    recipe.setCategories(catsByRecID.get(recipe.getId()));
                }
            }
        }
    }

    private void fillIngredientsInRecipe(List<Recipe> recipes) {
        List<String> ids = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ids.add(String.valueOf(recipe.getId()));
        }
        Map<Long, List<Ingredient>> ingByRecId = ingredientDao.fetchIngredientsByRecId(ids);
        if (!ingByRecId.isEmpty()) {
            for (Recipe recipe : recipes) {
                if (ingByRecId.containsKey(recipe.getId())) {
                    recipe.setIngredients(ingByRecId.get(recipe.getId()));
                }
            }
        }
    }


    public Recipe createOrUpdate(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRecipe.C_NAME,recipe.getName());
        contentValues.put(TRecipe.C_URL_IMAGE,recipe.getMainImage().asStorableString());
        contentValues.put(TRecipe.C_URL_IMAGE_2,recipe.getImage2().asStorableString());
        contentValues.put(TRecipe.C_URL_IMAGE_3,recipe.getImage3().asStorableString());
        contentValues.put(TRecipe.C_URL_IMAGE_4,recipe.getImage4().asStorableString());
        contentValues.put(TRecipe.C_URL_IMAGE_5,recipe.getImage5().asStorableString());
        contentValues.put(TRecipe.C_NB_COVERS,recipe.getNbCovers());
        contentValues.put(TRecipe.C_COOK_TIME,recipe.getCookTime());
        contentValues.put(TRecipe.C_TOTAL_TIME,recipe.getTotalTime());
        contentValues.put(TRecipe.C_PREPARE_TIME,recipe.getPrepareTime());
        contentValues.put(TRecipe.C_IS_BATCH,recipe.isBatchCooking());
        contentValues.put(TRecipe.C_URL_VIDEO,recipe.getUrlVideo());
        fillUpdatedate(contentValues);


        if (recipe.getId() != null) {
            contentValues.put(TRecipe._ID, recipe.getId());
            db.update(TRecipe.T_RECIPE,contentValues,"_id=" + recipe.getId(),null);
        } else {
            Long newIdRecipe = db.insert(TRecipe.T_RECIPE, null, contentValues);
            recipe.setId(newIdRecipe);
        }


        linkRecipeToCategories(recipe);
        linkRecipeToIngredients(recipe);
        linkRecipeToSteps(recipe);
        return recipe;
    }

    private void linkRecipeToSteps(Recipe recipe) {
        List<String> idSteps = new ArrayList<>();
        if (CollectionUtils.notNullOrEmpty(recipe.getSteps())) {
            for (Step step : recipe.getSteps()) {
                if (step.getId() != null) {
                    idSteps.add(String.valueOf(step.getId()));
                }
            }
        }
        stepDao.deleteStepsFromRecId(recipe.getId(),idSteps);
        stepDao.createOrUpdate(recipe.getSteps(), recipe.getId());
    }

    private void linkRecipeToIngredients(Recipe recipe) {
        recipe.setIngredients(ingredientDao.createIngredientsIfNeeded(recipe.getIngredients()));
        ingredientDao.deleteLinkRecipeIng(recipe);
        ingredientDao.createLinkRecipeIng(recipe);
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
        return getRecipeFromCursor(cursor, TRecipe._ID);
    }
    public static Recipe getRecipeFromCursor(Cursor cursor, String nameColumnId) {
        Recipe recipe = new Recipe();
        recipe.setId(cursor.getLong(cursor.getColumnIndex(nameColumnId)));
        recipe.getMainImage().parseStorableData(getStringColumnOrEmpty(cursor,TRecipe.C_URL_IMAGE));
        recipe.setName(cursor.getString(cursor.getColumnIndex(TRecipe.C_NAME)));
        recipe.setCookTime(getStringColumnOrEmpty(cursor, TRecipe.C_COOK_TIME));
        recipe.setNbCovers(getStringColumnOrEmpty(cursor, TRecipe.C_NB_COVERS));
        recipe.setPrepareTime(getStringColumnOrEmpty(cursor, TRecipe.C_PREPARE_TIME));
        recipe.setTotalTime(getStringColumnOrEmpty(cursor, TRecipe.C_TOTAL_TIME));
        recipe.setUrlVideo(getStringColumnOrEmpty(cursor, TRecipe.C_URL_VIDEO));
        recipe.getImage2().parseStorableData(getStringColumnOrEmpty(cursor,TRecipe.C_URL_IMAGE_2));
        recipe.getImage3().parseStorableData(getStringColumnOrEmpty(cursor,TRecipe.C_URL_IMAGE_3));
        recipe.getImage4().parseStorableData(getStringColumnOrEmpty(cursor,TRecipe.C_URL_IMAGE_4));
        recipe.getImage5().parseStorableData(getStringColumnOrEmpty(cursor,TRecipe.C_URL_IMAGE_5));

        return recipe;
    }



    @Override
    public void open() {
        super.open();
        initLinkDao();
    }

    public Recipe findById(Long recipeId) {
        if (recipeId == null) {
            return null;
        }
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
        recipe.setIngredients(ingredientDao.fetchIngredientsByRecId(recIdStr));
        recipe.setSteps(stepDao.fetchStepsByRecId(recIdStr));
        return recipe;
    }

    public List<Recipe> fetchRecipeByRegId(String regId) {
        if (SUtils.nullOrEmpty(regId)) {
            return new ArrayList<>();
        }
        Cursor cursor = db.rawQuery("select " +
                        "recipe._id recId, " +
                        "recipe."  + TRecipe.C_NAME  +
                        " FROM " + TRecipe.T_RECIPE + " recipe " +
                        " INNER JOIN " + TJGroupRecipe.TJ_GROUP_RECIPE + " linkRegRec ON " + TJGroupRecipe.C_ID_REC + " =  recId " +
                        " WHERE linkRegRec." + TJGroupRecipe.C_ID_REG +  "= ?"
                , new String[]{regId});

        List<Recipe> recipes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                recipes.add(getRecipeFromCursor(cursor,"recId"));
            } while (cursor.moveToNext());
        }
        fillIngredientsInRecipe(recipes);
        fillStepsInRecipe(recipes);

        return recipes;
    }

    public void fillStepsInRecipe(List<Recipe> recipes) {
        List<String> ids = new ArrayList<>();
        for (Recipe recipe : recipes) {
            ids.add(String.valueOf(recipe.getId()));
        }
        Map<Long, List<Step>> stepsByRecId = stepDao.fetchStepsByRecId(ids);
        if (!stepsByRecId.isEmpty()) {
            for (Recipe recipe : recipes) {
                if (stepsByRecId.containsKey(recipe.getId())) {
                    recipe.setSteps(stepsByRecId.get(recipe.getId()));
                }
            }
        }
    }

    public void deleteAll() {
        db.delete(TRecipe.T_RECIPE,null, null);
    }
}
