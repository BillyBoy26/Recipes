package com.example.benjamin.recettes.recipeGroup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.views.NameAdapter;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;

import java.util.List;

public class FrgGroupRecipes extends Fragment implements RecyclerViewClickListener {

    private RecipeSearchAdapter recipeAdapter;
    private NameAdapter recipeLinkedAdapter;
    private RecipeGroup recipeGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View recipeView = inflater.inflate(R.layout.recipes_group_create_recipes, container, false);

        recipeAdapter = new RecipeSearchAdapter(this);
        RecyclerView lstRecipesSearch = (RecyclerView) recipeView.findViewById(R.id.recyclerRecipes);
        lstRecipesSearch.setAdapter(recipeAdapter);
        lstRecipesSearch.setLayoutManager(new GridLayoutManager(getContext(),2));

        RecyclerView lstRecipesCreated = (RecyclerView) recipeView.findViewById(R.id.recyclerRecipesLink);
        lstRecipesCreated.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeLinkedAdapter = new NameAdapter();
        lstRecipesCreated.setAdapter(recipeLinkedAdapter);

        return recipeView;
    }

    public void getRecipeGroup() {
        recipeGroup.setRecipes(recipeLinkedAdapter.getDatas());
    }

    public void fillView(RecipeGroup recGroup, List<Recipe> allRecipes) {
        this.recipeGroup = recGroup;
        recipeLinkedAdapter.setDatas(this.recipeGroup.getRecipes());
        recipeAdapter.setDatas(allRecipes);
    }

    @Override
    public void onItemClick(View view, int position) {
        Recipe recipe = recipeAdapter.getItem(position);
        boolean added = recipeLinkedAdapter.addItem(recipe);
        if (!added) {
            Toast.makeText(getContext(), R.string.recipe_already_in_group,Toast.LENGTH_SHORT).show();
        }

    }
}
