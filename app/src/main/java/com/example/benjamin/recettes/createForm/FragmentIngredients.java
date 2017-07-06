package com.example.benjamin.recettes.createForm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class FragmentIngredients extends Fragment {

    private List<Ingredient> ingredients = new ArrayList<>();
    private IngredientAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.recipe_create_ingredients, container, false);
        final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerIngredient);
        adapter = new IngredientAdapter(ingredients);
        recyclerView.setAdapter(adapter);


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        SearchView searchView = (SearchView) layout.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    return false;
                }
                adapter.addIngredient(new Ingredient(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return layout;
    }
}