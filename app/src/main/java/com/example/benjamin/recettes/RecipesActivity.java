package com.example.benjamin.recettes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.benjamin.recettes.createForm.RecipeCreate;
import com.example.benjamin.recettes.cursor.SimpleCursorLoader;
import com.example.benjamin.recettes.db.RecipeDao;

public class RecipesActivity extends DrawerActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    private RecipeAdapter adapter;
    private RecipeDao recipeDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_recipes);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.recipes_list_layout, contentFrameLayout);

        recipeDao = new RecipeDao(this);
        recipeDao.open();


        getSupportLoaderManager().initLoader(1, null, this);

        adapter = new RecipeAdapter(this, null, 0);
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                Intent intent = new Intent(RecipesActivity.this, RecipeCreate.class);
                intent.putExtra(RecipeCreate.CURRENT_RECIPE, RecipeDao.getRecipeFromCursor(cursor));
                startActivity(intent);
            }
        });

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
        return new SimpleCursorLoader(this) {
            @Override
            public Cursor loadInBackground() {
                Cursor allRecipes = recipeDao.getAllRecipes();
                return allRecipes;
            }
        };
    }



    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }

    @Override
    protected void onResume() {
        recipeDao.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        recipeDao.close();
        super.onPause();
    }
}
