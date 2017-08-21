package com.example.benjamin.recettes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.benjamin.recettes.createForm.RecipeCreate;
import com.example.benjamin.recettes.cursor.SimpleCursorLoader;
import com.example.benjamin.recettes.db.dao.RecipeDao;

public class RecipesActivity extends DrawerActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    private RecipeAdapter adapter;
    private RecipeDao recipeDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_recipes);
        setContent(R.layout.recipes_list_layout);

        recipeDao = new RecipeDao(this);
        initDaos(recipeDao);


        getSupportLoaderManager().initLoader(3, null, this);

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
                return recipeDao.getAllRecipesAsCursor();
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

}
