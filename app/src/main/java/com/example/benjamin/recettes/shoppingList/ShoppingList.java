package com.example.benjamin.recettes.shoppingList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.recipes.createForm.IngredientAdapter;
import com.example.benjamin.recettes.recipes.createForm.IngredientWidgetBuilder;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.db.dao.IngredientDao;
import com.example.benjamin.recettes.db.dao.ShoppingDao;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.CommandWithParam;

import java.util.Collections;
import java.util.List;

public class ShoppingList extends DrawerActivity implements LoaderManager.LoaderCallbacks<List<Ingredient>>{


    private ShoppingDao shoppingDao;
    private IngredientDao ingredientDao;
    private IngredientAdapter adapter;
    private List<Ingredient> ingredients;

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
        this.ingredients = data;
        adapter.setDatas(this.ingredients);
    }

    @Override
    public void onLoaderReset(Loader<List<Ingredient>> loader) {
        adapter.setDatas(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_shopping,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                clearShoppingList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearShoppingList() {
        if (CollectionUtils.nullOrEmpty(ingredients)) {
            Toast.makeText(this, R.string.shopping_list_already_clear, Toast.LENGTH_SHORT).show();
        }
        shoppingDao.deleteAll();
        ingredients.clear();
        adapter.setDatas(ingredients);
        Toast.makeText(this, R.string.shopping_list_cleared, Toast.LENGTH_SHORT).show();
    }
}
