package com.example.benjamin.recettes.shoppingList;

import android.content.Intent;
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
import com.example.benjamin.recettes.recipes.RecipeAdapter;
import com.example.benjamin.recettes.recipes.createForm.RecipeCreate;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;

import java.util.List;

public class FrgRecipeList extends Fragment implements RecyclerViewClickListener {

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
            adapter = new RecipeAdapter(this);
        }
    }

    public void fillView(List<Recipe> recipes) {
        initAdapter();
        adapter.setDatas(recipes);
    }

    @Override
    public void onItemClick(View view, int position) {
        Recipe recipe = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), RecipeCreate.class);
        intent.putExtra(RecipeCreate.CURRENT_RECIPE, recipe);
        startActivity(intent);
    }
}
