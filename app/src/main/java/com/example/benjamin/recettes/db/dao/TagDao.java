package com.example.benjamin.recettes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.benjamin.recettes.data.Tags;
import com.example.benjamin.recettes.db.table.TCategory;
import com.example.benjamin.recettes.db.table.TTags;

import java.util.ArrayList;
import java.util.List;

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

}
