package com.example.benjamin.recettes.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.db.dao.CategoryDao;

import java.util.List;

public class CategoryList extends DrawerActivity implements LoaderManager.LoaderCallbacks<List<Category>> {

    private CategoryDao categoryDao;
    private CategoryAdapter categoryAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_category);
        setContent(R.layout.category_list);

        categoryDao = new CategoryDao(this);
        initDaos(categoryDao);
        getSupportLoaderManager().initLoader(5, null, this).forceLoad();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter();
        recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public Loader<List<Category>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Category>>(this) {
            @Override
            public List<Category> loadInBackground() {
                return categoryDao.getAllCategory();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Category>> loader, List<Category> data) {
        categoryAdapter.setCategories(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Category>> loader) {
        categoryAdapter.setCategories(null);
    }
}
