package com.example.benjamin.recettes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.table.TCategory;
import com.example.benjamin.recettes.db.table.TJCatRecipe;
import com.example.benjamin.recettes.db.table.TRecipe;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.SUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class CategoryDao extends GenericDao{

    public CategoryDao(SQLiteDatabase sqLiteDatabase) {
        super(sqLiteDatabase);
    }


    public CategoryDao(Context context) {
        super(context);
    }

//    public Cursor getAllCategoriess() {
//        String[] columns = new String[]{TCategory.C_NAME, TCategory._ID,
//                TCategory.C_URL_IMAGE, TCategory.C_INGREDIENTS, TCategory.C_STEPS, TCategory.C_UPDATE_DATE};
//        return db.query(TCategory.T_RECIPE, columns, null, null, null, null, TCategory.C_UPDATE_DATE + " DESC");
//    }

    public Category createOrUpdate(Category category) {
        if (category == null) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TCategory.C_NAME,category.getName());

        fillUpdatedate(contentValues);
        if (category.getId() != null) {
            contentValues.put(TCategory._ID, category.getId());
            db.update(TCategory.T_CATEGORY,contentValues,"_id=" + category.getId(),null);
        } else {
            Long newIdCategory = db.insert(TCategory.T_CATEGORY, null, contentValues);
            category.setId(newIdCategory);
        }
        return category;
    }

    public List<Category> createCategoriesIfNeeded(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        List<Category> unSavedCategories = new ArrayList<>();
        List<Category> savedCategories = new ArrayList<>();
        searchCatsIds(categories, unSavedCategories, savedCategories);

        for (Category category : unSavedCategories) {
            savedCategories.add(createOrUpdate(category));
        }
        return savedCategories;
    }

    private void searchCatsIds(List<Category> categories, List<Category> unSavedCategories, List<Category> savedCategories) {
        for (Category category : categories) {
            if (category.getId() != null) {
                savedCategories.add(category);
            } else {
                unSavedCategories.add(category);
            }
        }

        Map<String,Long> idsByName = searchCatIdsByNames(unSavedCategories);
        if (idsByName != null) {
            ListIterator<Category> iterator = unSavedCategories.listIterator();
            while (iterator.hasNext()) {
                Category category = iterator.next();
                if (idsByName.get(category.getName()) != null) {
                    category.setId(idsByName.get(category.getName()));
                    savedCategories.add(category);
                    iterator.remove();
                }
            }
        }

    }

    private Map<String, Long> searchCatIdsByNames(List<Category> categories) {
        if (CollectionUtils.nullOrEmpty(categories)) {
            return null;
        }
        List<String> names = new ArrayList<>();
        for (Category category : categories) {
            names.add(category.getName().toUpperCase());
        }

        Cursor cursor = db.rawQuery("Select cat." + TCategory.C_NAME +", cat._id catId " +
                " from " + TCategory.T_CATEGORY + " cat" +
                " where UPPER(cat." + TCategory.C_NAME + ") IN ("+ makePlaceholders(names.size()) + " )",names.toArray(new String[0]));
        if (!cursor.moveToFirst()) {
            return null;
        }
        Map<String, Long> idsByNames = new HashMap<>();
        do {
            Category category = getCategoryFromCursor(cursor,"catId");
            idsByNames.put(category.getName(), category.getId());
        } while (cursor.moveToNext());
        return idsByNames;
    }


    public boolean delete(Category category) {
        if (category == null || category.getId() == null) {
            return false;
        }

        db.delete(TCategory.T_CATEGORY, TCategory._ID + "= ?", new String[]{category.getId().toString()});
        return true;
    }

    public void deleteLinkRecipeCat(Recipe recipe) {
        if (recipe == null) {
            return;
        }
        db.delete(TJCatRecipe.TJ_CAT_RECIPE, TJCatRecipe.C_ID_RECIPE + "= ?", new String[]{recipe.getId().toString()});
    }

    public void createLinkRecipeCat(Recipe recipe) {
        if (recipe == null || CollectionUtils.nullOrEmpty(recipe.getCategories())) {
            return;
        }

        Long recipeId = recipe.getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TJCatRecipe.C_ID_RECIPE,recipeId);
        for (Category category : recipe.getCategories()) {
            contentValues.put(TJCatRecipe.C_ID_CAT, category.getId());
            db.insert(TJCatRecipe.TJ_CAT_RECIPE, null, contentValues);
        }
    }

    public List<Category> fetchCategoriesByRecId(String recId) {
        if (SUtils.nullOrEmpty(recId)) {
            return new ArrayList<>();
        }
        Cursor cursor = db.rawQuery("select " +
                        "cat._id catId, " +
                        TCategory.C_NAME +
                        " FROM " + TCategory.T_CATEGORY + " cat " +
                        " INNER JOIN " + TJCatRecipe.TJ_CAT_RECIPE + " linkCatRec ON " + TJCatRecipe.C_ID_CAT + " =  catId " +
                        " INNER JOIN " + TRecipe.T_RECIPE + " recipe ON " + TJCatRecipe.C_ID_RECIPE + " =  recipe._id " +
                        " WHERE recipe._id = ?"
                , new String[]{recId});

        List<Category> categories = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                categories.add(getCategoryFromCursor(cursor,"catId"));
            } while (cursor.moveToNext());
        }
        return categories;

    }


    public static Category getCategoryFromCursor(Cursor cursor) {
        return getCategoryFromCursor(cursor, TCategory._ID);
    }
    public static Category getCategoryFromCursor(Cursor cursor,String columnId) {
        Category category = new Category();
        category.setId(cursor.getLong(cursor.getColumnIndex(columnId)));
        category.setName(cursor.getString(cursor.getColumnIndex(TCategory.C_NAME)));
        return category;
    }

    public List<Category> getAllCategory() {
        Cursor cursor = db.rawQuery("select * from " + TCategory.T_CATEGORY + " order by " + TCategory.C_NAME, null);
        List<Category> categories = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                categories.add(getCategoryFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return categories;
    }

    public Map<Long,List<Category>> fetchCategoriesByRecId(List<String> idsRec) {
        if (CollectionUtils.nullOrEmpty(idsRec)) {
            return new HashMap<>();
        }
        Cursor cursor = db.rawQuery("select" +
                        " rec." + TRecipe._ID + " recId, " +
                        " cat." + TCategory._ID + " catId, " +
                        " cat." + TCategory.C_NAME +
                        " FROM " + TRecipe.T_RECIPE + " rec " +
                        " INNER JOIN " + TJCatRecipe.TJ_CAT_RECIPE + " linkCatRec ON linkCatRec." + TJCatRecipe.C_ID_RECIPE + "= rec." + TRecipe._ID +
                        " INNER JOIN " + TCategory.T_CATEGORY + " cat ON linkCatRec." + TJCatRecipe.C_ID_CAT + "= cat." + TCategory._ID +
                        " WHERE rec." + TRecipe._ID + " IN (" + makePlaceholders(idsRec.size()) + ")"
                , idsRec.toArray(new String[0]));
        Map<Long, List<Category>> catsByRecId = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                Long idRec = cursor.getLong(cursor.getColumnIndex("recId"));
                Category category = new Category();
                category.setId(cursor.getLong(cursor.getColumnIndex("catId")));
                category.setName(cursor.getString(cursor.getColumnIndex(TCategory.C_NAME)));
                if (catsByRecId.get(idRec) == null) {
                    catsByRecId.put(idRec, new ArrayList<Category>());
                }
                catsByRecId.get(idRec).add(category);

            } while (cursor.moveToNext());
        }
        return catsByRecId;
    }
}
