package com.example.benjamin.recettes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.Tags;
import com.example.benjamin.recettes.db.table.TCategory;
import com.example.benjamin.recettes.db.table.TJTagRecipe;
import com.example.benjamin.recettes.db.table.TRecipe;
import com.example.benjamin.recettes.db.table.TTags;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.SUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class TagDao extends GenericDao {
    public TagDao(Context context) {
        super(context);
    }

    public TagDao(SQLiteDatabase sqLiteDatabase) {
        super(sqLiteDatabase);
    }


    public List<Tags> getAllTags() {
        Cursor cursor = db.rawQuery("select * from " + TTags.T_TAGS + " order by " + TTags.C_NAME, null);
        List<Tags> tags = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                tags.add(getTagFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return tags;
    }

    private Tags getTagFromCursor(Cursor cursor) {
        return getTagFromCursor(cursor, "_id");
    }
    private Tags getTagFromCursor(Cursor cursor, String columnId) {
        Tags tags = new Tags();
        tags.setId(cursor.getLong(cursor.getColumnIndex(columnId)));
        tags.setName(cursor.getString(cursor.getColumnIndex(TTags.C_NAME)));
        return tags;
    }

    public Tags createOrUpdate(Tags tags) {
        if (tags == null) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TTags.C_NAME,tags.getName());

        fillUpdatedate(contentValues);
        if (tags.getId() != null) {
            contentValues.put(TTags._ID, tags.getId());
            db.update(TTags.T_TAGS,contentValues,"_id=" + tags.getId(),null);
        } else {
            Long newIdTags = db.insert(TTags.T_TAGS, null, contentValues);
            tags.setId(newIdTags);
        }
        return tags;
    }

    public boolean delete(Tags tag) {
        if (tag == null || tag.getId() == null) {
            return false;
        }

        db.delete(TTags.T_TAGS, TCategory._ID + "= ?", new String[]{tag.getId().toString()});
        return true;
    }

    public List<Tags> createTagsIfNeeded(List<Tags> tags) {
        if (tags == null) {
            return null;
        }
        List<Tags> unSavedTags = new ArrayList<>();
        List<Tags> savedTags = new ArrayList<>();
        searchTagsIds(tags, unSavedTags, savedTags);

        for (Tags tag : unSavedTags) {
            savedTags.add(createOrUpdate(tag));
        }
        return savedTags;
    }

    private void searchTagsIds(List<Tags> tags, List<Tags> unSavedTags, List<Tags> savedTags) {
        for (Tags tag : tags) {
            if (tag.getId() != null) {
                savedTags.add(tag);
            } else {
                unSavedTags.add(tag);
            }
        }

        Map<String,Long> idsByName = searchCatIdsByNames(unSavedTags);
        if (idsByName != null) {
            ListIterator<Tags> iterator = unSavedTags.listIterator();
            while (iterator.hasNext()) {
                Tags tag = iterator.next();
                if (idsByName.get(tag.getName()) != null) {
                    tag.setId(idsByName.get(tag.getName()));
                    savedTags.add(tag);
                    iterator.remove();
                }
            }
        }
    }

    private Map<String, Long> searchCatIdsByNames(List<Tags> tags) {
        if (CollectionUtils.nullOrEmpty(tags)) {
            return null;
        }
        List<String> names = new ArrayList<>();
        for (Tags tag : tags) {
            names.add(tag.getName().toUpperCase());
        }

        Cursor cursor = db.rawQuery("Select * " +
                " from " + TTags.T_TAGS +
                " where UPPER(" + TTags.C_NAME + ") IN ("+ makePlaceholders(names.size()) + " )",names.toArray(new String[0]));
        if (!cursor.moveToFirst()) {
            return null;
        }
        Map<String, Long> idsByNames = new HashMap<>();
        do {
            Tags tag = getTagFromCursor(cursor);
            idsByNames.put(tag.getName(), tag.getId());
        } while (cursor.moveToNext());
        return idsByNames;
    }

    public void deleteLinkRecipeTag(Recipe recipe) {
        if (recipe == null) {
            return;
        }
        db.delete(TJTagRecipe.TJ_TAG_REC, TJTagRecipe.C_ID_REC + "= ?", new String[]{recipe.getId().toString()});
    }

    public void createLinkRecipeTag(Recipe recipe) {
        if (recipe == null || CollectionUtils.nullOrEmpty(recipe.getTags())) {
            return;
        }

        Long recipeId = recipe.getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TJTagRecipe.C_ID_REC,recipeId);
        for (Tags tag : recipe.getTags()) {
            contentValues.put(TJTagRecipe.C_ID_TAG, tag.getId());
            db.insert(TJTagRecipe.TJ_TAG_REC, null, contentValues);
        }
    }

    public List<Tags> fetchTagsByRecId(String recId) {
        if (SUtils.nullOrEmpty(recId)) {
            return new ArrayList<>();
        }
        Cursor cursor = db.rawQuery("select " +
                        "tag._id tagId, " +
                        TTags.C_NAME +
                        " FROM " + TTags.T_TAGS + " tag " +
                        " INNER JOIN " + TJTagRecipe.TJ_TAG_REC + " linkTagRec ON " + TJTagRecipe.C_ID_TAG + " =  tagId " +
                        " INNER JOIN " + TRecipe.T_RECIPE + " recipe ON " + TJTagRecipe.C_ID_REC + " =  recipe._id " +
                        " WHERE recipe._id = ?"
                , new String[]{recId});

        List<Tags> tags = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                tags.add(getTagFromCursor(cursor,"tagId"));
            } while (cursor.moveToNext());
        }
        return tags;
    }
}
