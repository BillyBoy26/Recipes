package com.example.benjamin.recettes.shoppingList;

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
import com.example.benjamin.recettes.recipes.createForm.IngredientAdapter;
import com.example.benjamin.recettes.recipes.createForm.IngredientWidgetBuilder;
import com.example.benjamin.recettes.views.SimpleItemDividerDecoration;

import java.util.List;

public class FrgShoppingList extends Fragment {

    public static final String ADD_COMMAND = "ADD_COMMAND";
    public static final String DELETE_COMMAND = "DELETE_COMMAND";

    public interface OnIngredientListEditedListener {
        void onIngredientSelected(Ingredient ingredient);
        void onIngredientClicked(Ingredient ingredient);
    }

    private IngredientAdapter adapter;
    private OnIngredientListEditedListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View shoppingView = inflater.inflate(R.layout.shopping_list, container, false);
        initAdapter();
        RecyclerView recyclerView = (RecyclerView) shoppingView.findViewById(R.id.recyclerIngredient);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SimpleItemDividerDecoration(getContext()));
        recyclerView.setAdapter(adapter);


        final SearchView searchView = (SearchView) shoppingView.findViewById(R.id.searchView);
        View dialogView = inflater.inflate(R.layout.ingredient_dialog_quantity, null);
        IngredientWidgetBuilder ingBuilder = new IngredientWidgetBuilder(searchView,dialogView,adapter,listener);
        searchView.setOnQueryTextListener(ingBuilder.createQueryTextListener());


        return shoppingView;
    }

    private void initAdapter() {
        if (adapter == null) {
            adapter = new IngredientAdapter(listener);
        }
    }

    public void fillView(List<Ingredient> ingredients) {
        initAdapter();
        adapter.setDatas(ingredients);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnIngredientListEditedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }

}
