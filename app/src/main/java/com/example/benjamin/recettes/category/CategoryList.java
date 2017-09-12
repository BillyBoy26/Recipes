package com.example.benjamin.recettes.category;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.TabsActivity;
import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.data.Tags;
import com.example.benjamin.recettes.db.dao.CategoryDao;
import com.example.benjamin.recettes.db.dao.TagDao;
import com.example.benjamin.recettes.recipes.createForm.ViewPagerAdapter;
import com.example.benjamin.recettes.task.AsyncTaskDataLoader;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.SUtils;

import java.util.List;

public class CategoryList extends TabsActivity implements LoaderManager.LoaderCallbacks<List<Category>> {

    private CategoryDao categoryDao;
    private TagDao tagDao;

    private List<Category> categories;

    private FragmentCategory fragmentCategory;
    private FragmentTags fragmentTags;
    private List<Tags> tags;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_category);
        setContent(R.layout.recycler_layout);

        categoryDao = new CategoryDao(this);
        tagDao = new TagDao(this);
        initDaos(categoryDao,tagDao);
        getSupportLoaderManager().initLoader(AsyncTaskDataLoader.getNewUniqueLoaderId(), null, this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.text_input,null);
                final EditText editText = (EditText) dialogView.findViewById(R.id.txtText);
                editText.setHint(R.string.name);
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryList.this)
                        .setTitle(viewPager.getCurrentItem() == 0 ? R.string.new_category : R.string.new_tags)
                        .setView(dialogView);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = editText.getText().toString();
                        if (viewPager.getCurrentItem() == 0) {
                            createCategory(name);
                        } else {
                            createTags(name);
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();
            }
        });
    }


    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        fragmentCategory = new FragmentCategory();
        adapter.addFragment(fragmentCategory,getString(R.string.categories));

        fragmentTags = new FragmentTags();
        adapter.addFragment(fragmentTags,getString(R.string.tags));

        viewPager.setAdapter(adapter);
    }

    private void createTags(String name) {
        if (SUtils.nullOrEmpty(name)) {
            return;
        }
        Tags tag = new Tags(name);
        tag = tagDao.createOrUpdate(tag);
        if (tag.getId() != null) {
            tags.add(tag);
            setTagsToView();
        }
    }


    private void createCategory(String name) {
        if (SUtils.nullOrEmpty(name)) {
            return;
        }
        Category category = new Category(name);
        category = categoryDao.createOrUpdate(category);
        if (category.getId() != null) {
            categories.add(category);
            setCategoriesToView();
        }
    }

    @Override
    public Loader<List<Category>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskDataLoader<List<Category>>(this) {
            @Override
            public List<Category> loadInBackground() {
                tags = tagDao.getAllTags();
                categories = categoryDao.getAllCategory();
                return categories;
            }
        };
    }

    private void setCategoriesToView() {
        fragmentCategory.setCategories(categories);
    }

    @Override
    public void onLoadFinished(Loader<List<Category>> loader, List<Category> data) {
        setCategoriesToView();
        setTagsToView();
    }

    private void setTagsToView() {
        fragmentTags.setTags(tags);
    }

    @Override
    public void onLoaderReset(Loader<List<Category>> loader) {
        this.categories = null;
        setCategoriesToView();
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
                deleteSelectedItems();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSelectedItems() {
        if (viewPager.getCurrentItem() == 0) {
            deleteSelectedCategories();
        } else {
            deleteSelectedTags();
        }
    }

    private void deleteSelectedTags() {
        List<Tags> selectedTags = fragmentTags.getSelectedTags();
        if (CollectionUtils.nullOrEmpty(selectedTags)) {
            Toast.makeText(this, R.string.no_tags_selected, Toast.LENGTH_SHORT).show();
            return;
        }
        for (Tags tag : selectedTags) {
            tagDao.delete(tag);
            if (tags.contains(tag)) {
                tags.remove(tag);
            }
        }
        setTagsToView();
    }


    private void deleteSelectedCategories() {
        List<Category> selectedCategories = fragmentCategory.getSelectedCategories();
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
        setCategoriesToView();
    }
}
