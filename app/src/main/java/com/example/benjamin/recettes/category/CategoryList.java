package com.example.benjamin.recettes.category;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.db.dao.CategoryDao;
import com.example.benjamin.recettes.task.AsyncTaskDataLoader;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.SUtils;

import java.util.List;

public class CategoryList extends DrawerActivity implements LoaderManager.LoaderCallbacks<List<Category>> {

    private CategoryDao categoryDao;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_category);
        setContent(R.layout.category_list);

        categoryDao = new CategoryDao(this);
        initDaos(categoryDao);
        getSupportLoaderManager().initLoader(AsyncTaskDataLoader.getNewUniqueLoaderId(), null, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter();
        recyclerView.setAdapter(categoryAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.text_input,null);
                final EditText editText = (EditText) dialogView.findViewById(R.id.txtText);
                editText.setHint(R.string.name);
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryList.this)
                        .setTitle(R.string.new_category)
                        .setView(dialogView);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createCategory(editText.getText().toString());
                    }
                });
                builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();
            }
        });
    }

    private void createCategory(String name) {
        if (SUtils.nullOrEmpty(name)) {
            return;
        }
        Category category = new Category(name);
        category = categoryDao.createOrUpdate(category);
        if (category.getId() != null) {
            categories.add(category);
            categoryAdapter.setDatas(categories);
        }
    }

    @Override
    public Loader<List<Category>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskDataLoader<List<Category>>(this) {
            @Override
            public List<Category> loadInBackground() {
                categories = categoryDao.getAllCategory();
                return categories;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Category>> loader, List<Category> data) {
        categoryAdapter.setDatas(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Category>> loader) {
        categoryAdapter.setDatas(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (CollectionUtils.nullOrEmpty(categories)) {
            return false;
        }
        getMenuInflater().inflate(R.menu.menu_toolbar_category,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteSelectedCategories();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSelectedCategories() {
        List<Category> selectedCategories = categoryAdapter.getSelectedCategories();
        if (CollectionUtils.nullOrEmpty(selectedCategories)) {
            Toast.makeText(this, R.string.no_category_selected, Toast.LENGTH_SHORT).show();
            return;
        }
        for (Category category : selectedCategories) {
            categoryDao.delete(category);
            if (categories.contains(category)) {
                categories.remove(category);
            }
        }
        categoryAdapter.setDatas(categories);

    }
}
