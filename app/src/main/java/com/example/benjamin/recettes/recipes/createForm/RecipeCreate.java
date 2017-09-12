package com.example.benjamin.recettes.recipes.createForm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.TabsActivity;
import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.Tags;
import com.example.benjamin.recettes.db.dao.BatchCookingDao;
import com.example.benjamin.recettes.db.dao.CategoryDao;
import com.example.benjamin.recettes.db.dao.RecipeDao;
import com.example.benjamin.recettes.db.dao.TagDao;
import com.example.benjamin.recettes.recipes.RecipesList;
import com.example.benjamin.recettes.task.AsyncTaskDataLoader;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.SimpleImageActionListener;

import java.util.List;

public class RecipeCreate extends TabsActivity implements LoaderManager.LoaderCallbacks<Recipe> {


    public static final String CURRENT_RECIPE = "NEW_RECIPE";
    public static final int PERM_REQUEST_READ_STORAGE = 1;
    private Recipe recipe;
    private FragmentGeneral fragmentGeneral;
    private FragmentIngredients fragmentIngredients;
    private FragmentSteps fragmentSteps;
    private RecipeDao recipeDao;
    private CategoryDao categoryDao;
    private TagDao tagDao;
    private BatchCookingDao batchCookingDao;
    private List<Category> allCategory;
    private SimpleImageActionListener imgActionListener;
    private Intent intentLoadImage;
    private List<Tags> allTags;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeDao = new RecipeDao(this);
        categoryDao = new CategoryDao(this);
        batchCookingDao = new BatchCookingDao(this);
        tagDao = new TagDao(this);
        initDaos(recipeDao,batchCookingDao,categoryDao,tagDao);

        getSupportLoaderManager().initLoader(AsyncTaskDataLoader.getNewUniqueLoaderId(), null, this);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get(CURRENT_RECIPE) != null) {
            recipe = (Recipe) extras.get(CURRENT_RECIPE);
        } else {
            setRecipe();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_done_white_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = createOrUpdateRecipe();
                if (ok) {
                    startActivity(new Intent(RecipeCreate.this, RecipesList.class));
                }
            }
        });
    }

    private void setRecipe() {
        if (recipe == null) {
            recipe = new Recipe();
        }
        fragmentGeneral.setDatas(recipe,allCategory,allTags);
        fragmentIngredients.setRecipe(recipe);
        fragmentSteps.setData(recipe);
    }

    @Override
    protected void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        fragmentGeneral = new FragmentGeneral();
        imgActionListener = new SimpleImageActionListener(this);
        fragmentGeneral.setImgActionListner(imgActionListener);
        adapter.addFragment(fragmentGeneral,getString(R.string.general));

        fragmentIngredients = new FragmentIngredients();
        adapter.addFragment(fragmentIngredients,getString(R.string.ingredients));

        fragmentSteps = new FragmentSteps();
        adapter.addFragment(fragmentSteps,getString(R.string.steps));

        viewPager.setAdapter(adapter);
    }

    private boolean createOrUpdateRecipe() {
        fragmentGeneral.getRecipe();
        fragmentIngredients.getRecipe();
        fragmentSteps.getData();
        if (SUtils.nullOrEmpty(recipe.getName())) {
            Toast.makeText(this, R.string.your_recipe_has_no_name, Toast.LENGTH_SHORT).show();
            return false;
        }

        recipeDao.createOrUpdate(recipe);
        return true;
    }

    @Override
    public Loader<Recipe> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskDataLoader<Recipe>(this) {
            @Override
            public Recipe loadInBackground() {
                allCategory = categoryDao.getAllCategory();
                allTags = tagDao.getAllTags();
                return recipeDao.findById(recipe.getId());
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Recipe> loader, Recipe data) {
        this.recipe = data;
        setRecipe();
        invalidateOptionsMenu();
    }

    @Override
    public void onLoaderReset(Loader<Recipe> loader) {
        this.recipe = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (recipe != null && recipe.getId() != null) {
            getMenuInflater().inflate(R.menu.menu_toolbar_recipe_create,menu);
            return true;
        }
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteRecipe();
                break;
            case R.id.action_add_shoppping_list:
                addToShoppingList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToShoppingList() {
        if (CollectionUtils.nullOrEmpty(recipe.getIngredients())) {
            Toast.makeText(this, R.string.none_ingredient_to_add_shopping_list, Toast.LENGTH_SHORT).show();
            return;
        }
        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.getId() == null) {
                Toast.makeText(this, R.string.update_in_ingredient_list_not_saved, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        boolean ingCreated = batchCookingDao.addRecipeToBatchCooking(recipe);
        if (ingCreated) {
            Toast.makeText(this, R.string.ingredients_from_recipe_added_to_shopping_list, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRecipe() {
        recipeDao.delete(recipe);
        startActivity(new Intent(RecipeCreate.this, RecipesList.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SimpleImageActionListener.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            this.intentLoadImage = data;
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                imgActionListener.handleIntent(data);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERM_REQUEST_READ_STORAGE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERM_REQUEST_READ_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imgActionListener.handleIntent(intentLoadImage);
                }
                intentLoadImage = null;
                return;
        }
    }
}
