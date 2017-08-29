package com.example.benjamin.recettes.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.dao.RecipeDao;
import com.example.benjamin.recettes.recipes.createForm.RecipeCreate;
import com.example.benjamin.recettes.task.AsyncTaskDataLoader;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;

import java.util.List;

public class RecipesList extends DrawerActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>,RecyclerViewClickListener{


    private RecipeAdapter adapter;
    private RecipeDao recipeDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_recipes);
        setContent(R.layout.recipes_list_layout);

        recipeDao = new RecipeDao(this);
        initDaos(recipeDao);


        getSupportLoaderManager().initLoader(AsyncTaskDataLoader.getNewUniqueLoaderId(), null, this);

        adapter = new RecipeAdapter(this);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lstRecipes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecipesList.this, RecipeCreate.class));
            }
        });
    }


    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskDataLoader<List<Recipe>>(this) {
            @Override
            public List<Recipe> loadInBackground() {
                return recipeDao.getAllRecipes();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        adapter.setDatas(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.setDatas(null);
    }

    @Override
    public void onItemClick(View view, int position) {
        Recipe recipe = adapter.getItem(position);
        Intent intent = new Intent(RecipesList.this, RecipeCreate.class);
        intent.putExtra(RecipeCreate.CURRENT_RECIPE, recipe);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_recipe_list,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteAllRecipe();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllRecipe() {
        recipeDao.deleteAll();
        adapter.setDatas(null);
    }
}
