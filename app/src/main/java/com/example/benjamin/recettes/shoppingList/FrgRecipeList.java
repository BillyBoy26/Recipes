package com.example.benjamin.recettes.shoppingList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.recipes.RecipeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FrgRecipeList extends Fragment {

    private RecipeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View recipesView = inflater.inflate(R.layout.recipes_list_layout, container, false);
        initAdapter();
        RecyclerView recyclerView = (RecyclerView) recipesView.findViewById(R.id.lstRecipes);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setAdapter(adapter);

        return recipesView;
    }

    private void initAdapter() {
        if (adapter == null) {
            adapter = new RecipeAdapter(null);
        }
    }

    public void fillView(List<Recipe> recipes, List<RecipeGroup> recipeGroups) {
        initAdapter();
        List<Recipe> allRecipe = new ArrayList<>();
        allRecipe.addAll(recipes);
        for (RecipeGroup recipeGroup : recipeGroups) {
            if (recipeGroup.getRecipes() != null) {
                allRecipe.addAll(recipeGroup.getRecipes());
            }
        }
        Collections.sort(allRecipe, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe o1, Recipe o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        });
        adapter.setDatas(allRecipe);
    }
}
