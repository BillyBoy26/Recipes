package com.example.benjamin.recettes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.table.TIngredient;
import com.example.benjamin.recettes.db.table.TJIngRecipe;
import com.example.benjamin.recettes.db.table.TRecipe;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.CursorUtils;
import com.example.benjamin.recettes.utils.SUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class IngredientDao extends GenericDao {
    public IngredientDao(Context context) {
        super(context);
    }

    public IngredientDao(SQLiteDatabase sqLiteDatabase) {
        super(sqLiteDatabase);
    }

    public Ingredient createOrUpdate(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(TIngredient.C_NAME,ingredient.getName());
        fillUpdatedate(contentValues);
        if (ingredient.getId() != null) {
            contentValues.put(TIngredient._ID, ingredient.getId());
            db.update(TIngredient.T_INGREDIENT, contentValues, "_id=" + ingredient.getId(), null);
        } else {
            Long newIdIngredient = db.insert(TIngredient.T_INGREDIENT, null, contentValues);
            ingredient.setId(newIdIngredient);
        }
        return ingredient;
    }

    public List<Ingredient> fetchIngredientsByRecId(String recId) {
        if (SUtils.nullOrEmpty(recId)) {
            return new ArrayList<>();
        }
        Cursor cursor = db.rawQuery("select " +
                        "ing._id ingId, " +
                        "ing." + TIngredient.C_NAME + "," +
                        "linkIngRec." + TJIngRecipe.C_QUANTITY + "," +
                        "linkIngRec." + TJIngRecipe.C_QUANTITY_UNIT +
                        " FROM " + TIngredient.T_INGREDIENT + " ing " +
                        " INNER JOIN " + TJIngRecipe.TJ_ING_RECIPE + " linkIngRec ON " + TJIngRecipe.C_ID_ING + " =  ingId " +
                        " INNER JOIN " + TRecipe.T_RECIPE + " recipe ON " + TJIngRecipe.C_ID_RECIPE + " =  recipe._id " +
                        " WHERE recipe._id = ?"
                , new String[]{recId});

        List<Ingredient> ingredients = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ingredients.add(getIngredientFromCursor(cursor,"ingId"));
            } while (cursor.moveToNext());
        }
        return ingredients;

    }

    public List<Ingredient> createIngredientsIfNeeded(List<Ingredient> ingredients) {
        if (ingredients == null) {
            return null;
        }
        List<Ingredient> unSavedIngredients = new ArrayList<>();
        List<Ingredient> savedIngredients = new ArrayList<>();
        searchIngsIds(ingredients, unSavedIngredients, savedIngredients);
        for (Ingredient ingredient : unSavedIngredients) {
            savedIngredients.add(createOrUpdate(ingredient));
        }
        return savedIngredients;
    }

    private void searchIngsIds(List<Ingredient> ingredients, List<Ingredient> unSavedIngredients, List<Ingredient> savedIngredients) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() != null) {
                savedIngredients.add(ingredient);
            } else {
                unSavedIngredients.add(ingredient);
            }
        }

        Map<String,Long> idsByName = searchIngIdsByNames(ingredients);
        if (idsByName != null) {
            ListIterator<Ingredient> iterator = unSavedIngredients.listIterator();
            while (iterator.hasNext()) {
                Ingredient ingredient = iterator.next();
                if (idsByName.get(ingredient.getName()) != null) {
                    ingredient.setId(idsByName.get(ingredient.getName()));
                    savedIngredients.add(ingredient);
                    iterator.remove();
                }
            }
        }

    }

    private Map<String, Long> searchIngIdsByNames(List<Ingredient> ingredients) {
        if (CollectionUtils.nullOrEmpty(ingredients)) {
            return null;
        }
        List<String> names = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            names.add(ingredient.getName().toUpperCase());
        }

        Cursor cursor = db.rawQuery("Select ing." + TIngredient.C_NAME +", ing._id ingId " +
                " from " + TIngredient.T_INGREDIENT + " ing" +
                " where UPPER(ing." + TIngredient.C_NAME + ") IN ("+ makePlaceholders(names.size()) + " )",names.toArray(new String[0]));
        if (!cursor.moveToFirst()) {
            return null;
        }
        Map<String, Long> idsByNames = new HashMap<>();
        do {
            Ingredient ing = getIngredientFromCursor(cursor,"ingId");
            idsByNames.put(ing.getName(), ing.getId());
        } while (cursor.moveToNext());
        return idsByNames;
    }

    private Ingredient getIngredientFromCursor(Cursor cursor) {
        return getIngredientFromCursor(cursor,TIngredient._ID);
    }

    private Ingredient getIngredientFromCursor(Cursor cursor, String columnId) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(cursor.getLong(cursor.getColumnIndex(columnId)));
        ingredient.setName(cursor.getString(cursor.getColumnIndex(TIngredient.C_NAME)));
        ingredient.setQuantity(CursorUtils.getFloatColumnOrNull(cursor,TJIngRecipe.C_QUANTITY));
        ingredient.setQuantityUnit(CursorUtils.getStringColumnOrEmpty(cursor,TJIngRecipe.C_QUANTITY_UNIT));
        return ingredient;
    }

    public void deleteLinkRecipeIng(Recipe recipe) {
        if (recipe == null) {
            return;
        }
        db.delete(TJIngRecipe.TJ_ING_RECIPE, TJIngRecipe.C_ID_RECIPE + "= ?", new String[]{recipe.getId().toString()});
    }

    public void createLinkRecipeIng(Recipe recipe) {
        if (recipe == null || CollectionUtils.nullOrEmpty(recipe.getIngredients())) {
            return;
        }

        Long recipeId = recipe.getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TJIngRecipe.C_ID_RECIPE,recipeId);
        for (Ingredient ingredient : recipe.getIngredients()) {
            contentValues.put(TJIngRecipe.C_ID_ING, ingredient.getId());
            contentValues.put(TJIngRecipe.C_QUANTITY,ingredient.getQuantity());
            contentValues.put(TJIngRecipe.C_QUANTITY_UNIT,ingredient.getQuantityUnit());
            db.insert(TJIngRecipe.TJ_ING_RECIPE, null, contentValues);
        }
    }

    public @NonNull Map<Long, List<Ingredient>> fetchIngredientsByRecId(List<String> idsRec) {
        if (CollectionUtils.nullOrEmpty(idsRec)) {
            return new HashMap<>();
        }
        Cursor cursor = db.rawQuery("select" +
                        " rec." + TRecipe._ID + " recId, " +
                        " ing." + TIngredient._ID + " ingId, " +
                        " ing." + TIngredient.C_NAME +
                        " FROM " + TRecipe.T_RECIPE + " rec " +
                        " INNER JOIN " + TJIngRecipe.TJ_ING_RECIPE + " linkIngRec ON linkIngRec." + TJIngRecipe.C_ID_RECIPE + "= rec." + TRecipe._ID +
                        " INNER JOIN " + TIngredient.T_INGREDIENT + " ing ON linkIngRec." + TJIngRecipe.C_ID_ING + "= ing." + TIngredient._ID +
                        " WHERE rec." + TRecipe._ID + " IN (" + makePlaceholders(idsRec.size()) + ")"
                , idsRec.toArray(new String[0]));
        Map<Long, List<Ingredient>> ingsByRecId = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                Long idRec = cursor.getLong(cursor.getColumnIndex("recId"));
                Ingredient ingredient = new Ingredient();
                ingredient.setId(cursor.getLong(cursor.getColumnIndex("ingId")));
                ingredient.setName(cursor.getString(cursor.getColumnIndex(TIngredient.C_NAME)));
                if (ingsByRecId.get(idRec) == null) {
                    ingsByRecId.put(idRec, new ArrayList<Ingredient>());
                }
                ingsByRecId.get(idRec).add(ingredient);

            } while (cursor.moveToNext());
        }
        return ingsByRecId;
    }
}
