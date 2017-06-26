package com.example.benjamin.recettes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.example.benjamin.recettes.data.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends DrawerActivity{


    private List<Recipe> recipes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNavigationView().setCheckedItem(R.id.nav_camera);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.recipes_list_layout, contentFrameLayout);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (recipes == null) {
            recipes = new ArrayList<>();
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            Object recipe = getIntent().getExtras().get(RecipeCreate.NEW_RECIPE);
            if (recipe != null) {
                recipes.add((Recipe) recipe);
            }
        }

        addRecipes(recipes);
        recyclerView.setAdapter(new RecipeAdapter(recipes));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecipesActivity.this, RecipeCreate.class));
            }
        });
    }

    private void addRecipes(List<Recipe> recipes) {
        recipes.add(new Recipe("One-Pot Ham & Potato Soup", "https://img.buzzfeed.com/buzzfeed-static/static/2017-01/26/13/asset/buzzfeed-prod-fastlane-03/sub-buzz-27993-1485454420-2.jpg"));
    }


}
