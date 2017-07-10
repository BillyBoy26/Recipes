package com.example.benjamin.recettes.db;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.benjamin.recettes.db.table.TRecipe;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RecipeContentProvider extends ContentProvider {

    public static final String BASE_PATH = "recipes";
    public static final String AUTHORITY = "com.example.benjamin.recettes.db";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private RecipeDbHelper recipeDbHelper;

    @Override
    public boolean onCreate() {
        recipeDbHelper = new RecipeDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(TRecipe.T_RECIPE);
        SQLiteDatabase db = recipeDbHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = recipeDbHelper.getWritableDatabase();
        getContext().getContentResolver().notifyChange(uri,null);
        if (values != null) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            values.put(TRecipe.C_UPDATE_DATE, format.format(new Date()));
        }

        long id = db.insert(TRecipe.T_RECIPE, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    /**
     * http://vogella.developpez.com/tutoriels/android/utilisation-base-donnees-sqlite/
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = recipeDbHelper.getWritableDatabase();

        String recipeId = uri.getLastPathSegment();
        int rowDeleted = db.delete(TRecipe.T_RECIPE, TRecipe._ID + "=" + recipeId, null);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = recipeDbHelper.getWritableDatabase();
        if (values != null) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            values.put(TRecipe.C_UPDATE_DATE, format.format(new Date()));
        }
        String recipeId = uri.getLastPathSegment();
        int rowUpdated = db.update(TRecipe.T_RECIPE, values, TRecipe._ID + "=" + recipeId, null);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowUpdated;
    }

}
