package com.example.benjamin.recettes.shoppingList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.TabsActivity;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.data.Step;
import com.example.benjamin.recettes.db.dao.BatchCookingDao;
import com.example.benjamin.recettes.db.dao.IngredientDao;
import com.example.benjamin.recettes.db.dao.ShoppingDao;
import com.example.benjamin.recettes.recipes.createForm.FragmentSteps;
import com.example.benjamin.recettes.recipes.createForm.ViewPagerAdapter;
import com.example.benjamin.recettes.task.AsyncTaskDataLoader;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.FrgArgsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BatchCooking extends TabsActivity implements LoaderManager.LoaderCallbacks<BatchCooking.BatchCookingBundle>
        ,FrgShoppingList.OnIngredientListEditedListener{



    private BatchCookingDao batchCookingDao;
    private IngredientDao ingredientDao;
    private ShoppingDao shoppingDao;

    private FrgShoppingList frgShoppingList;
    private FrgRecipeList frgRecipeList;
    private FragmentSteps frgSteps;
    private List<Recipe> recipes;
    private List<Ingredient> ingredients;
    private List<RecipeGroup> recipeGroups;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_shopping_list);

        batchCookingDao = new BatchCookingDao(this);
        ingredientDao = new IngredientDao(this);
        shoppingDao = new ShoppingDao(this);
        initDaos(ingredientDao,batchCookingDao,shoppingDao);
        getSupportLoaderManager().initLoader(AsyncTaskDataLoader.getNewUniqueLoaderId(), null, this);
    }

    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        frgShoppingList = new FrgShoppingList();
        adapter.addFragment(frgShoppingList,getString(R.string.shopping_list));
        frgRecipeList = new FrgRecipeList();
        adapter.addFragment(frgRecipeList,getString(R.string.recipes));

        frgSteps = (FragmentSteps) new FrgArgsBuilder<>
                (new FragmentSteps())
                .putBoolean(FragmentSteps.CAN_ADD, false)
                .create();
        adapter.addFragment(frgSteps,getString(R.string.steps));

        viewPager.setAdapter(adapter);
    }


    @Override
    public Loader<BatchCookingBundle> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskDataLoader<BatchCookingBundle>(this) {
            @Override
            public BatchCookingBundle loadInBackground() {
                return batchCookingDao.getBatchCooking();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<BatchCookingBundle> loader,BatchCookingBundle data) {
        this.ingredients = data.ingredients;
        this.recipes = data.recipes;
        recipeGroups = data.recipeGroups;
        fillView();
    }

    private void fillView() {
        List<Recipe> allRecipe = new ArrayList<>();
        List<Step> allSteps = new ArrayList<>();
        if (recipeGroups != null) {
            for (RecipeGroup recipeGroup : recipeGroups) {
                if (recipeGroup.getRecipes() != null) {
                    for (Recipe recipe : recipeGroup.getRecipes()) {
                        allRecipe.add(recipe);
                    }
                }
                if (recipeGroup.getSteps() != null) {
                    for (Step step : recipeGroup.getSteps()) {
                        allSteps.add(step);
                    }
                }
            }
        }
        if (recipes != null) {
            for (Recipe recipe : recipes) {
                allRecipe.add(recipe);
                for (Step step : recipe.getSteps()) {
                    allSteps.add(step);
                }
            }
        }

        Collections.sort(allRecipe, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe o1, Recipe o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        });


        frgShoppingList.fillView(ingredients);
        frgRecipeList.fillView(allRecipe);
        frgSteps.setSteps(allSteps);

    }

    @Override
    public void onLoaderReset(Loader<BatchCookingBundle> loader) {
        ingredients = null;
        recipes = null;
        fillView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_shopping,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_shopping:
                clearShoppingList();
                break;
            case R.id.action_delete_recipes:
                clearRecipesList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearRecipesList() {
        if (CollectionUtils.nullOrEmpty(recipes) && CollectionUtils.nullOrEmpty(recipeGroups)) {
            Toast.makeText(this, R.string.recipe_list_already_clear, Toast.LENGTH_SHORT).show();
            return;
        }
        batchCookingDao.clearBatchCookingRecipes(recipes);
        batchCookingDao.clearBatchCookingRecipeGroups(recipeGroups);
        recipeGroups.clear();
        recipes.clear();
        frgRecipeList.fillView(null);
        frgSteps.setSteps(null);
        Toast.makeText(this, R.string.recipes_list_cleared, Toast.LENGTH_SHORT).show();
    }

    private void clearShoppingList() {
        if (CollectionUtils.nullOrEmpty(ingredients)) {
            Toast.makeText(this, R.string.shopping_list_already_clear, Toast.LENGTH_SHORT).show();
            return;
        }
        shoppingDao.deleteAll();
        ingredients.clear();
        frgShoppingList.fillView(ingredients);
        Toast.makeText(this, R.string.shopping_list_cleared, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIngredientSelected(Ingredient ingredient) {
        if (ingredient != null) {
            ingredientDao.createIngredientsIfNeeded(Collections.singletonList(ingredient));
            shoppingDao.addIngredientToShoppingList(Arrays.asList(ingredient));
        }
    }

    @Override
    public void onIngredientClicked(Ingredient ingredient) {
        if (ingredient != null) {
            shoppingDao.delete(ingredient);
        }
    }

    public static class BatchCookingBundle{
        public List<Ingredient> ingredients;
        public List<Recipe> recipes;
        public List<RecipeGroup> recipeGroups;
    }
}
