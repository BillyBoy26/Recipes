package com.example.benjamin.recettes.recipeGroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.db.dao.RecipeGroupDao;
import com.example.benjamin.recettes.task.AsyncTaskDataLoader;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.Predicate;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;

import java.util.Comparator;
import java.util.List;

public class RecipeGroupList extends DrawerActivity implements LoaderManager.LoaderCallbacks<List<RecipeGroup>>,RecyclerViewClickListener {

    private RecipeGroupDao recipeGroupDao;
    private List<RecipeGroup> recipeGroups;
    private RecipeGroupAdapter recGroupAdapter;
    private boolean sortByAlpha = true;
    private SearchView searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_batch_cooking);
        setContent(R.layout.recipes_group_list);
        recipeGroupDao = new RecipeGroupDao(this);
        initDaos(recipeGroupDao);
        getSupportLoaderManager().initLoader(AsyncTaskDataLoader.getNewUniqueLoaderId(), null, this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recGroupAdapter = new RecipeGroupAdapter(this);
        recyclerView.setAdapter(recGroupAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecipeGroupList.this, RecipeGroupCreate.class));
            }
        });

    }

    @Override
    public Loader<List<RecipeGroup>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskDataLoader<List<RecipeGroup>>(this) {
            @Override
            public List<RecipeGroup> loadInBackground() {
                return recipeGroupDao.getAllRecipeGroup();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<RecipeGroup>> loader, List<RecipeGroup> data) {
        recipeGroups = data;
        recGroupAdapter.setDatas(data);
    }

    @Override
    public void onLoaderReset(Loader<List<RecipeGroup>> loader) {
        recipeGroups = null;
        recGroupAdapter.setDatas(null);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(RecipeGroupList.this, RecipeGroupCreate.class);
        intent.putExtra(RecipeGroupCreate.CURRENT_GROUP, recGroupAdapter.getDatas().get(position));
        startActivity(intent);
    }

    private void sortByAlpha() {
        recGroupAdapter.sort(new Comparator<RecipeGroup>() {
            @Override
            public int compare(RecipeGroup o1, RecipeGroup o2) {
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
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_toolbar_recipe_group_list, menu);
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
        List<RecipeGroup> recipesGrpFiltred = CollectionUtils.filter(recipeGroups, new Predicate<RecipeGroup>() {
            @Override
            public boolean apply(RecipeGroup element) {
                return element.getName() != null && element.getName().toLowerCase().contains(searchText.toLowerCase());
            }
        });
        recGroupAdapter.setDatas(recipesGrpFiltred);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_sort_alpha:
                sortByAlpha();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
