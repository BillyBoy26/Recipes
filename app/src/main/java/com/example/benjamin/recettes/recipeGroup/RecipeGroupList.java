package com.example.benjamin.recettes.recipeGroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.db.dao.RecipeGroupDao;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;

import java.util.List;

public class RecipeGroupList extends DrawerActivity implements LoaderManager.LoaderCallbacks<List<RecipeGroup>>,RecyclerViewClickListener {

    private RecipeGroupDao recipeGroupDao;
    private List<RecipeGroup> recipeGroups;
    private RecipeGroupAdapter recGroupAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_batch_cooking);
        setContent(R.layout.recipes_group_list);
        recipeGroupDao = new RecipeGroupDao(this);
        initDaos(recipeGroupDao);
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();

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
        return new AsyncTaskLoader<List<RecipeGroup>>(this) {
            @Override
            public List<RecipeGroup> loadInBackground() {
                return recipeGroupDao.getAllRecipeGroup();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<RecipeGroup>> loader, List<RecipeGroup> data) {
        recipeGroups = data;
        recGroupAdapter.setRecipeGroup(data);
    }

    @Override
    public void onLoaderReset(Loader<List<RecipeGroup>> loader) {
        recipeGroups = null;
        recGroupAdapter.setRecipeGroup(null);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(RecipeGroupList.this, RecipeGroupCreate.class);
        intent.putExtra(RecipeGroupCreate.CURRENT_GROUP, recipeGroups.get(position));
        startActivity(intent);
    }
}
