package com.example.benjamin.recettes.recipes.createForm;

import android.content.Context;
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

public class FragmentIngredients extends Fragment{


    public interface OnIngredientListEditedListener {
        void onIngredientSelected(Ingredient ingredient);
        void onIngredientClicked(Ingredient ingredient);
    }
    private OnIngredientListEditedListener listener;

    private List<Ingredient> ingredients = new ArrayList<>();
    private IngredientAdapter adapter;
    private Recipe recipe;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.ingredients_list, container, false);
        final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerIngredient);
        fillView();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new SimpleItemDividerDecoration(getContext()));

        final SearchView searchView = (SearchView) layout.findViewById(R.id.searchView);
        View dialogView = inflater.inflate(R.layout.ingredient_dialog_quantity, null);
        IngredientWidgetBuilder ingBuilder = new IngredientWidgetBuilder(searchView,dialogView,adapter);
        searchView.setOnQueryTextListener(ingBuilder.createQueryTextListener());
        fillView();

        return layout;
    }

    private void fillView() {
        if (adapter == null) {
            adapter = new IngredientAdapter(listener);
        }
        adapter.setDatas(ingredients);
    }


    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        this.ingredients = recipe != null ? recipe.getIngredients() : null;
        fillView();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        fillView();
    }

    public void getRecipe() {
        recipe.setIngredients(ingredients);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIngredientListEditedListener) {
            listener = (OnIngredientListEditedListener) context;
        } else {
            listener = null;
        }
    }

}
