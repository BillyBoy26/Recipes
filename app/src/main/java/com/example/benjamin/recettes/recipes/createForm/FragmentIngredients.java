package com.example.benjamin.recettes.recipes.createForm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.views.SimpleItemDividerDecoration;

import java.util.ArrayList;
import java.util.List;

public class FragmentIngredients extends Fragment implements RecipeCreate.RecipeFiller{



    private List<Ingredient> ingredients = new ArrayList<>();
    private IngredientAdapter adapter;
    private Recipe recipe;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.recipe_create_ingredients, container, false);
        final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerIngredient);
        fillView();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new SimpleItemDividerDecoration(getContext()));

        final SearchView searchView = (SearchView) layout.findViewById(R.id.searchView);
        View dialogView = inflater.inflate(R.layout.ingredient_dialog_quantity, null);
        IngredientWidgetBuilder ingBuilder = new IngredientWidgetBuilder(searchView,dialogView,adapter);
        searchView.setOnQueryTextListener(ingBuilder.createQueryTextListener());
        return layout;
    }

    private void fillView() {
        if (adapter == null) {
            adapter = new IngredientAdapter();
        }
        adapter.setDatas(ingredients);
    }


    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        this.ingredients = recipe != null ? recipe.getIngredients() : null;
        fillView();

    }

    @Override
    public void getRecipe() {
        recipe.setIngredients(ingredients);
    }
}
