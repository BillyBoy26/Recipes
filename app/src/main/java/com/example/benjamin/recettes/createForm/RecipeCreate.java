package com.example.benjamin.recettes.createForm;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.RecipesActivity;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.RecipeContentProvider;
import com.example.benjamin.recettes.db.table.TRecipe;

public class RecipeCreate extends DrawerActivity {

    public static final String CURRENT_RECIPE = "NEW_RECIPE";
    private Recipe recipe;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentGeneral fragmentGen;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableTabs();
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.recipe_create_form, contentFrameLayout);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get(CURRENT_RECIPE) != null) {
            recipe = (Recipe) extras.get(CURRENT_RECIPE);
        }
        if (recipe == null) {
            recipe = new Recipe();
        }
        setRecipe();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_done_white_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrUpdateRecipe();
                startActivity(new Intent(RecipeCreate.this, RecipesActivity.class));
            }
        });
    }

    private void setRecipe() {
        fragmentGen.setRecipe(recipe);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentGen = new FragmentGeneral();
        adapter.addFragment(fragmentGen,getString(R.string.general));
        adapter.addFragment(new FragmentIngredients(),getString(R.string.ingredients));
        adapter.addFragment(new FragmentSteps(),getString(R.string.steps));
        viewPager.setAdapter(adapter);
    }

    private void createOrUpdateRecipe() {
        fragmentGen.getRecipe();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRecipe.C_NAME,recipe.getName());
        contentValues.put(TRecipe.C_URL_IMAGE,recipe.getUrlImage());

        if (recipe != null && recipe.getId() != null) {
            contentValues.put(TRecipe._ID, recipe.getId());
            Uri uri = Uri.parse(RecipeContentProvider.CONTENT_URI + "/" + recipe.getId());
            getContentResolver().update(uri,contentValues,null,null);
        } else {
            getContentResolver().insert(RecipeContentProvider.CONTENT_URI,contentValues);
        }
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
        Uri uri = Uri.parse(RecipeContentProvider.CONTENT_URI + "/" + recipe.getId());
        getContentResolver().delete(uri, null, null);
        startActivity(new Intent(RecipeCreate.this, RecipesActivity.class));
    }
}
