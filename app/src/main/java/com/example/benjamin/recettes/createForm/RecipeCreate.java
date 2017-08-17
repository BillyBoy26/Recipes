package com.example.benjamin.recettes.createForm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.RecipesActivity;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.dao.RecipeDao;
import com.example.benjamin.recettes.utils.SUtils;

import java.util.ArrayList;
import java.util.List;

public class RecipeCreate extends DrawerActivity implements LoaderManager.LoaderCallbacks<Recipe> {

    @Override
    public Loader<Recipe> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Recipe>(this) {
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
    }

    @Override
    public void onLoaderReset(Loader<Recipe> loader) {
        this.recipe = null;
    }

    public interface RecipeFiller{
        void setRecipe(Recipe recipe);

        void getRecipe();
    }

    public static final String CURRENT_RECIPE = "NEW_RECIPE";
    private Recipe recipe;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private List<RecipeFiller> fragments = new ArrayList<>();
    private RecipeDao recipeDao;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTabs();
        setContent(R.layout.recipe_create_form);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        recipeDao = new RecipeDao(this);
        initDaos(recipeDao);


        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get(CURRENT_RECIPE) != null) {
            recipe = (Recipe) extras.get(CURRENT_RECIPE);
            getSupportLoaderManager().initLoader(2, null, this).forceLoad();
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
                    startActivity(new Intent(RecipeCreate.this, RecipesActivity.class));
                }
            }
        });
    }

    private void setRecipe() {
        if (recipe == null) {
            recipe = new Recipe();
        }
        for (RecipeFiller fragment : fragments) {
            fragment.setRecipe(recipe);
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        FragmentGeneral fragmentGen = new FragmentGeneral();
        adapter.addFragment(fragmentGen,getString(R.string.general));
        fragments.add(fragmentGen);

        FragmentIngredients fragmentIng = new FragmentIngredients();
        adapter.addFragment(fragmentIng,getString(R.string.ingredients));
        fragments.add(fragmentIng);

        FragmentSteps fragmentSteps = new FragmentSteps();
        adapter.addFragment(fragmentSteps,getString(R.string.steps));
        fragments.add(fragmentSteps);

        viewPager.setAdapter(adapter);
    }

    private boolean createOrUpdateRecipe() {
        for (RecipeFiller fragment : fragments) {
            fragment.getRecipe();
        }
        if (SUtils.nullOrEmpty(recipe.getName())) {
            Toast.makeText(this, "Votre recette n'a pas de nom", Toast.LENGTH_SHORT).show();
            return false;
        }

        recipeDao.createOrUpdate(recipe);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (recipe != null) {
            getMenuInflater().inflate(R.menu.menu_toolbar,menu);
            return true;
        }
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete && recipe != null) {
            deleteRecipe();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteRecipe() {
        recipeDao.delete(recipe);
        startActivity(new Intent(RecipeCreate.this, RecipesActivity.class));
    }

}
