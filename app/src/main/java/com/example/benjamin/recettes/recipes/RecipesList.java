package com.example.benjamin.recettes.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.dao.RecipeDao;
import com.example.benjamin.recettes.recipes.createForm.RecipeCreate;
import com.example.benjamin.recettes.task.AsyncTaskDataLoader;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.Predicate;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;

import java.util.Comparator;
import java.util.List;

public class RecipesList extends DrawerActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>,RecyclerViewClickListener{

    public static final String NB_RECIPES_IMPORTED = "NB_RECIPES_IMPORTED";

    private RecipeAdapter adapter;
    private RecipeDao recipeDao;
    private boolean sortByAlpha = true;
    private SearchView searchView;
    private List<Recipe> recipes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_recipes);
        setContent(R.layout.recipes_list_layout);

        recipeDao = new RecipeDao(this);
        initDaos(recipeDao);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get(NB_RECIPES_IMPORTED) != null) {
            int nbRecipesImported = (int) extras.get(NB_RECIPES_IMPORTED);
            Toast.makeText(this, nbRecipesImported + " " + getString(R.string.imported_recipes), Toast.LENGTH_SHORT).show();
        }

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
        this.recipes = data;
        adapter.setDatas(this.recipes);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        this.recipes = null;
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
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                doSearch(searchText);
                return false;
            }
        });
        return true;

    }

    private void doSearch(final String searchText) {
        List<Recipe> recipesFiltred = CollectionUtils.filter(recipes, new Predicate<Recipe>() {
            @Override
            public boolean apply(Recipe element) {
                return element.getName() != null && element.getName().toLowerCase().contains(searchText.toLowerCase());
            }
        });
        adapter.setDatas(recipesFiltred);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteAllRecipe();
                break;
            case R.id.action_sort_alpha:
                sortByAlpha();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortByAlpha() {
        adapter.sort(new Comparator<Recipe>() {
            @Override
            public int compare(Recipe o1, Recipe o2) {
                if (o1.getName() == null) {
                    return 1;
                }
                if (o2.getName() == null) {
                    return -1;
                }
                if (sortByAlpha) {
                    return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
                } else {
                    return o2.getName().toUpperCase().compareTo(o1.getName().toUpperCase());
                }
            }
        });
        sortByAlpha = !sortByAlpha;
    }

    private void deleteAllRecipe() {
        recipeDao.deleteAll();
        adapter.setDatas(null);
    }
}
