package com.example.benjamin.recettes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.benjamin.recettes.db.RecipeContentProvider;
import com.example.benjamin.recettes.db.table.TRecipe;

public class RecipesActivity extends DrawerActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    private RecipeAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNavigationView().setCheckedItem(R.id.nav_camera);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.recipes_list_layout, contentFrameLayout);



        getSupportLoaderManager().initLoader(1, null, this);

        adapter = new RecipeAdapter(this, null, 0);
        ListView recyclerView = (ListView) findViewById(R.id.listView);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecipesActivity.this, RecipeCreate.class));
            }
        });
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] projection = {TRecipe._ID, TRecipe.C_NAME,TRecipe.C_URL_IMAGE};
        CursorLoader cursorLoader = new CursorLoader(this, RecipeContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }
}
