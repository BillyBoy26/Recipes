package com.example.benjamin.recettes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.db.table.TRecipeGroup;

import java.util.ArrayList;
import java.util.List;

public class RecipeGroupDao extends GenericDao {
    public RecipeGroupDao(Context context) {
        super(context);
    }

    public RecipeGroupDao(SQLiteDatabase sqLiteDatabase) {
        super(sqLiteDatabase);
    }

    public RecipeGroup createOrUpdate(RecipeGroup recipeGroup) {
        if (recipeGroup == null) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRecipeGroup.C_NAME,recipeGroup.getName());

        fillUpdatedate(contentValues);
        if (recipeGroup.getId() != null) {
            contentValues.put(TRecipeGroup._ID, recipeGroup.getId());
            db.update(TRecipeGroup.T_RECIPE_GROUP,contentValues,"_id=" + recipeGroup.getId(),null);
        } else {
            Long newIdCategory = db.insert(TRecipeGroup.T_RECIPE_GROUP, null, contentValues);
            recipeGroup.setId(newIdCategory);
        }
        return recipeGroup;
    }

    public void delete(RecipeGroup recipeGroup) {
        if (recipeGroup.getId() == null) {
            return;
        }
        db.delete(TRecipeGroup.T_RECIPE_GROUP, TRecipeGroup._ID + "= ?", new String[]{recipeGroup.getId().toString()});
    }

    public List<RecipeGroup> getAllRecipeGroup() {
        Cursor cursor = db.rawQuery("select * " +
                " from " + TRecipeGroup.T_RECIPE_GROUP +
                " order by " + TRecipeGroup.C_UPDATE_DATE + " DESC", null);
        List<RecipeGroup> recipeGroups = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                recipeGroups.add(getRecipeGroupFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return recipeGroups;
    }

    private RecipeGroup getRecipeGroupFromCursor(Cursor cursor) {
        RecipeGroup recipGroup = new RecipeGroup();
        recipGroup.setId(cursor.getLong(cursor.getColumnIndex(TRecipeGroup._ID)));
        recipGroup.setName(cursor.getString(cursor.getColumnIndex(TRecipeGroup.C_NAME)));
        return recipGroup;
    }
}
