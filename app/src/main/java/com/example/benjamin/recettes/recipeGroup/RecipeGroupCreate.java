package com.example.benjamin.recettes.recipeGroup;

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
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.data.Step;
import com.example.benjamin.recettes.db.dao.BatchCookingDao;
import com.example.benjamin.recettes.db.dao.RecipeDao;
import com.example.benjamin.recettes.db.dao.RecipeGroupDao;
import com.example.benjamin.recettes.recipes.createForm.FragmentSteps;
import com.example.benjamin.recettes.recipes.createForm.ViewPagerAdapter;
import com.example.benjamin.recettes.task.AsyncTaskDataLoader;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.FrgArgsBuilder;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static com.example.benjamin.recettes.data.Recipe.RecipeFiller.WITH_ING;
import static com.example.benjamin.recettes.data.Recipe.RecipeFiller.WITH_STEPS;

public class RecipeGroupCreate extends TabsActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>,FrgGroupRecipes.OnRecipeSelectedListener {

    public static final String CURRENT_GROUP = "CURRENT_GROUP";

    private RecipeGroup recipeGroup;
    private RecipeGroupDao recipeGroupDao;
    private BatchCookingDao batchCookingDao;
    private RecipeDao recipeDao;

    private List<Recipe> allRecipes;
    private FrgGroupGeneral fragmentGen;
    private FrgGroupRecipes fragmentRecipes;
    private FragmentSteps fragmentSteps;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeGroupDao = new RecipeGroupDao(this);
        recipeDao = new RecipeDao(this);
        batchCookingDao = new BatchCookingDao(this);
        initDaos(recipeGroupDao,recipeDao,batchCookingDao);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get(CURRENT_GROUP) != null) {
            recipeGroup = (RecipeGroup) extras.get(CURRENT_GROUP);
        }

        getSupportLoaderManager().initLoader(AsyncTaskDataLoader.getNewUniqueLoaderId(), null, this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_done_white_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = createOrUpdateRecipeGroup();
                if (ok) {
                    startActivity(new Intent(RecipeGroupCreate.this, RecipeGroupList.class));
                }
            }
        });
    }

    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentGen = new FrgGroupGeneral();
        adapter.addFragment(fragmentGen,getString(R.string.general));
        fragmentRecipes = new FrgGroupRecipes();
        adapter.addFragment(fragmentRecipes,getString(R.string.recipes));
        fragmentSteps = (FragmentSteps) new FrgArgsBuilder<>
                (new FragmentSteps())
                .putBoolean(FragmentSteps.CAN_ADD, false)
                .create();
        adapter.addFragment(fragmentSteps,getString(R.string.steps));
        viewPager.setAdapter(adapter);
    }


    private boolean createOrUpdateRecipeGroup() {
        getRecipeGroup();

        recipeGroupDao.createOrUpdate(recipeGroup);
        return true;
    }

    private void getRecipeGroup() {
        fragmentGen.getRecipeGroup();
        fragmentRecipes.getRecipeGroup();
        fragmentSteps.getData();
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskDataLoader<List<Recipe>>(this) {
            @Override
            public List<Recipe> loadInBackground() {
                allRecipes = recipeDao.getAllRecipes(EnumSet.of(WITH_ING, WITH_STEPS));
                if (recipeGroup != null && recipeGroup.getId() != null) {
                    recipeGroup = recipeGroupDao.findById(recipeGroup.getId());
                }
                return allRecipes;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        fillFragmentsView();
    }

    private void fillFragmentsView() {
        if (recipeGroup == null) {
            recipeGroup = new RecipeGroup();
        }
        fragmentGen.fillView(recipeGroup);
        fragmentRecipes.fillView(recipeGroup,allRecipes);
        fragmentSteps.setData(recipeGroup);

    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        recipeGroup = new RecipeGroup();
        fragmentGen.fillView(recipeGroup);
        fragmentRecipes.fillView(recipeGroup,allRecipes);
        fragmentSteps.setData(recipeGroup);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (recipeGroup != null && recipeGroup.getId() != null) {
            getMenuInflater().inflate(R.menu.menu_toolbar_recipegrp_create,menu);
            return true;
        }
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteGroupRecipe();
                break;
            case R.id.action_add_shoppping_list:
                addToShoppingList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToShoppingList() {
        if (CollectionUtils.nullOrEmpty(recipeGroup.getRecipes())) {
            Toast.makeText(this, R.string.none_recipe_to_add_shopping_list, Toast.LENGTH_SHORT).show();
            return;
        }
        boolean created = batchCookingDao.addRecipeGroupToBatchCooking(recipeGroup);
        if (created) {
            Toast.makeText(this, R.string.ingredients_from_recipe_group_added_to_shopping_list, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.none_ingredient_to_add_shopping_list, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteGroupRecipe() {
        recipeGroupDao.delete(recipeGroup);
        startActivity(new Intent(RecipeGroupCreate.this, RecipeGroupList.class));
    }

    @Override
    public void onRecipeSelectedListener(Recipe recipe) {
        if (recipeGroup.getSteps() == null) {
            recipeGroup.setSteps(new ArrayList<Step>());
        }
        recipeGroup.getSteps().addAll(recipe.getSteps());
        fragmentSteps.setData(recipeGroup);
    }
}
