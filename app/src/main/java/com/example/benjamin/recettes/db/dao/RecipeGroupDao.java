package com.example.benjamin.recettes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.db.table.TJGroupRecipe;
import com.example.benjamin.recettes.db.table.TRecipeGroup;
import com.example.benjamin.recettes.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class RecipeGroupDao extends GenericDao {

    private RecipeDao recipeDao;

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
        contentValues.put(TRecipeGroup.C_NAME, recipeGroup.getName());


        fillUpdatedate(contentValues);
        if (recipeGroup.getId() != null) {
            contentValues.put(TRecipeGroup._ID, recipeGroup.getId());
            db.update(TRecipeGroup.T_RECIPE_GROUP, contentValues, "_id=" + recipeGroup.getId(), null);
        } else {
            Long newIdCategory = db.insert(TRecipeGroup.T_RECIPE_GROUP, null, contentValues);
            recipeGroup.setId(newIdCategory);
        }
        deleteAllLinkReciceToGroup(recipeGroup);
        createLinkReciceToGroup(recipeGroup);
        return recipeGroup;
    }

    private void createLinkReciceToGroup(RecipeGroup recipeGroup) {
        if (CollectionUtils.nullOrEmpty(recipeGroup.getRecipes())) {
            return;
        }
        for (Recipe recipe : recipeGroup.getRecipes()) {
            addRecipeToGroup(recipe, recipeGroup);
        }
    }

    private void deleteAllLinkReciceToGroup(RecipeGroup recipeGroup) {
        db.delete(TJGroupRecipe.TJ_GROUP_RECIPE, TJGroupRecipe.C_ID_REG + "= ?", new String[]{recipeGroup.getId().toString()});
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

    public void addRecipeToGroup(Recipe recipe, RecipeGroup recipeGroup) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TJGroupRecipe.C_ID_REG, recipeGroup.getId());
        contentValues.put(TJGroupRecipe.C_ID_REC, recipe.getId());

        Long newId = db.insert(TJGroupRecipe.TJ_GROUP_RECIPE, null, contentValues);
        if (newId < 0) {
            throw new RuntimeException("Error create TJGroupRecipe with idRec = " + recipe.getId() + " and idReg=" + recipeGroup.getId());
        }
    }

    public RecipeGroup findById(Long recipeGrpId) {
        String regIdStr = Long.toString(recipeGrpId);
        Cursor cursor = db.rawQuery("Select * from " + TRecipeGroup.T_RECIPE_GROUP + " where " + TRecipeGroup._ID + " = ?", new String[]{regIdStr});
        if (!cursor.moveToFirst()) {
            return null;
        }
        RecipeGroup recipeGrp = getRecipeGroupFromCursor(cursor);
        if (recipeGrp == null) {
            return null;
        }
        recipeGrp.setRecipes(recipeDao.fetchRecipeByRegId(regIdStr));
        return recipeGrp;
    }

    @Override
    public void open() {
        super.open();
        recipeDao = new RecipeDao(db);
    }
}
