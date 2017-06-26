package com.example.benjamin.recettes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.example.benjamin.recettes.data.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends DrawerActivity{



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNavigationView().setCheckedItem(R.id.nav_camera);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.recipes_list_layout, contentFrameLayout);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Recipe> recipes = new ArrayList<>();
        addRecipes(recipes);
        recyclerView.setAdapter(new RecipeAdapter(recipes));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener();
    }

    private void addRecipes(List<Recipe> recipes) {
        recipes.add(new Recipe("One-Pot Ham & Potato Soup", "https://img.buzzfeed.com/buzzfeed-static/static/2017-01/26/13/asset/buzzfeed-prod-fastlane-03/sub-buzz-27993-1485454420-2.jpg"));
    }


}
