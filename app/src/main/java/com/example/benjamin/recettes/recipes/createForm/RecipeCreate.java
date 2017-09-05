package com.example.benjamin.recettes.recipes.createForm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.dao.BatchCookingDao;
import com.example.benjamin.recettes.db.dao.RecipeDao;
import com.example.benjamin.recettes.recipes.RecipesList;
import com.example.benjamin.recettes.task.AsyncTaskDataLoader;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.SUtils;

public class RecipeCreate extends TabsActivity implements LoaderManager.LoaderCallbacks<Recipe> {


    public static final String CURRENT_RECIPE = "NEW_RECIPE";
    private Recipe recipe;
    private FragmentGeneral fragmentGeneral;
    private FragmentIngredients fragmentIngredients;
    private FragmentSteps fragmentSteps;
    private RecipeDao recipeDao;
    private BatchCookingDao batchCookingDao;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeDao = new RecipeDao(this);
        batchCookingDao = new BatchCookingDao(this);
        initDaos(recipeDao,batchCookingDao);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get(CURRENT_RECIPE) != null) {
            recipe = (Recipe) extras.get(CURRENT_RECIPE);
            getSupportLoaderManager().initLoader(AsyncTaskDataLoader.getNewUniqueLoaderId(), null, this);
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
        fragmentGeneral.setRecipe(recipe);
        fragmentIngredients.setRecipe(recipe);
        fragmentSteps.setData(recipe);
    }

    @Override
    protected void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        fragmentGeneral = new FragmentGeneral();
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


}
