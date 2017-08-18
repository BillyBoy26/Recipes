package com.example.benjamin.recettes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.table.TIngredient;
import com.example.benjamin.recettes.db.table.TShoppingIngredient;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.CursorUtils;

import java.util.ArrayList;
import java.util.List;

public class ShoppingDao extends GenericDao{
    public ShoppingDao(Context context) {
        super(context);
    }

    public Ingredient createOrUpdate(Ingredient ingredient) {
        if (ingredient == null) {
            return null;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(TShoppingIngredient.C_ID_ING,ingredient.getId());
        if (ingredient.getQuantity() != null) {
            contentValues.put(TShoppingIngredient.C_QUANTITY,ingredient.getQuantity());
        }
        if (ingredient.getQuantityUnit() != null) {
            contentValues.put(TShoppingIngredient.C_QUANTITY_UNIT,ingredient.getQuantityUnit());
        }
        fillUpdatedate(contentValues);
//        if (ingredient.getId() != null) {
//            contentValues.put(TIngredient._ID, ingredient.getId());
//            db.update(TIngredient.T_INGREDIENT, contentValues, "_id=" + ingredient.getId(), null);
//        } else {
        db.insert(TShoppingIngredient.T_SHOPPING_INGREDIENT, null, contentValues);
//        }
        return ingredient;
    }

    public List<Ingredient> getShoppingList() {
        Cursor cursor = db.rawQuery("select " +
                        "sig." + TShoppingIngredient.C_ID_ING + ", " +
                        "sig." + TShoppingIngredient.C_QUANTITY + ", " +
                        "sig." + TShoppingIngredient.C_QUANTITY_UNIT + ", " +
                        "ing." + TIngredient.C_NAME  +
                        " FROM " + TShoppingIngredient.T_SHOPPING_INGREDIENT + " sig" +
                        " INNER JOIN " + TIngredient.T_INGREDIENT + " ing ON sig." + TShoppingIngredient.C_ID_ING + "=ing." + TIngredient._ID +
                        " ORDER BY sig." + TShoppingIngredient.C_UPDATE_DATE + " DESC", null);
        List<Ingredient> ingredients = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ingredients.add(getIngredientFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return ingredients;
    }

    private Ingredient getIngredientFromCursor(Cursor cursor) {
        return getIngredientFromCursor(cursor,TShoppingIngredient.C_ID_ING);
    }

    private Ingredient getIngredientFromCursor(Cursor cursor, String columnId) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(cursor.getLong(cursor.getColumnIndex(columnId)));
        ingredient.setName(cursor.getString(cursor.getColumnIndex(TIngredient.C_NAME)));
        ingredient.setQuantity(CursorUtils.getFloatColumnOrNull(cursor,TShoppingIngredient.C_QUANTITY));
        ingredient.setQuantityUnit(CursorUtils.getStringColumnOrEmpty(cursor,TShoppingIngredient.C_QUANTITY_UNIT));
        return ingredient;
    }


    public void delete(Ingredient ingredient) {
        if (ingredient.getId() == null) {
            return;
        }
        db.delete(TShoppingIngredient.T_SHOPPING_INGREDIENT, TShoppingIngredient.C_ID_ING + "= ?", new String[]{ingredient.getId().toString()});
    }

    public boolean createFromRecipe(Recipe recipe) {
        if (recipe == null || CollectionUtils.nullOrEmpty(recipe.getIngredients())) {
            return false;
        }
        boolean ingAdded = false;
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getId() != null) {
                createOrUpdate(ingredient);
                ingAdded = true;
            }
        }
        return ingAdded;
    }

    public void deleteAll() {
        db.delete(TShoppingIngredient.T_SHOPPING_INGREDIENT,null, null);
    }
}
