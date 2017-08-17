package com.example.benjamin.recettes.shoppingList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.createForm.IngredientAdapter;
import com.example.benjamin.recettes.createForm.IngredientWidgetBuilder;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.db.dao.IngredientDao;
import com.example.benjamin.recettes.db.dao.ShoppingDao;
import com.example.benjamin.recettes.utils.CommandWithParam;

import java.util.Collections;
import java.util.List;

public class ShoppingList extends DrawerActivity implements LoaderManager.LoaderCallbacks<List<Ingredient>>{


    private ShoppingDao shoppingDao;
    private IngredientDao ingredientDao;
    private IngredientAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_shopping_list);
        setContent(R.layout.shopping_list);

        shoppingDao = new ShoppingDao(this);
        ingredientDao = new IngredientDao(this);
        initDaos(shoppingDao,ingredientDao);
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();

        adapter = new IngredientAdapter(buildDeleteCommand());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerIngredient);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setAdapter(adapter);

        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        View dialogView = getLayoutInflater().inflate(R.layout.ingredient_dialog_quantity, null);
        IngredientWidgetBuilder ingBuilder = new IngredientWidgetBuilder(searchView,dialogView,adapter,buildCommandeAddIngredient());
        searchView.setOnQueryTextListener(ingBuilder.createQueryTextListener());
    }

    private CommandWithParam<Ingredient> buildDeleteCommand() {
        return new CommandWithParam<Ingredient>() {
            @Override
            public void execute(Ingredient ingRemoved) {
                if (ingRemoved != null) {
                    shoppingDao.delete(ingRemoved);
                }
            }
        };
    }

    private CommandWithParam<Ingredient> buildCommandeAddIngredient() {
        return new CommandWithParam<Ingredient>() {
            @Override
            public void execute(Ingredient ingredient) {
                if (ingredient != null) {
                    ingredientDao.createIngredientsIfNeeded(Collections.singletonList(ingredient));
                    shoppingDao.createOrUpdate(ingredient);
                }
            }
        };
    }

    @Override
    public Loader<List<Ingredient>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Ingredient>>(this) {
            @Override
            public List<Ingredient> loadInBackground() {
                return shoppingDao.getShoppingList();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Ingredient>> loader, List<Ingredient> data) {
        adapter.setIngredient(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Ingredient>> loader) {
        adapter.setIngredient(null);
    }
}
