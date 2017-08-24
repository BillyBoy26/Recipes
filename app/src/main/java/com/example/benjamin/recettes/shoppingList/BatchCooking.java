package com.example.benjamin.recettes.shoppingList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.TabsActivity;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.db.dao.IngredientDao;
import com.example.benjamin.recettes.db.dao.ShoppingDao;
import com.example.benjamin.recettes.recipes.createForm.ViewPagerAdapter;
import com.example.benjamin.recettes.task.AsyncTaskDataLoader;
import com.example.benjamin.recettes.utils.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class BatchCooking extends TabsActivity implements LoaderManager.LoaderCallbacks<List<Ingredient>>,FrgShoppingList.OnIngredientListEditedListener{



    private ShoppingDao shoppingDao;
    private IngredientDao ingredientDao;

    private List<Ingredient> ingredients;
    private FrgShoppingList frgShoppingList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_shopping_list);

        shoppingDao = new ShoppingDao(this);
        ingredientDao = new IngredientDao(this);
        initDaos(shoppingDao,ingredientDao);
        getSupportLoaderManager().initLoader(AsyncTaskDataLoader.getNewUniqueLoaderId(), null, this);
    }

    @Override
    protected void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        frgShoppingList = new FrgShoppingList();
//        Bundle args = new Bundle();
//        args.putSerializable(FrgShoppingList.ADD_COMMAND,buildCommandeAddIngredient());
//        args.putSerializable(FrgShoppingList.DELETE_COMMAND,buildDeleteCommand());
//        frgShoppingList.setArguments(args);

        adapter.addFragment(frgShoppingList,getString(R.string.shopping_list));
        viewPager.setAdapter(adapter);
    }


    @Override
    public Loader<List<Ingredient>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskDataLoader<List<Ingredient>>(this) {
            @Override
            public List<Ingredient> loadInBackground() {
                return shoppingDao.getShoppingList();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Ingredient>> loader, List<Ingredient> data) {
        this.ingredients = data;
        fillView();
    }

    private void fillView() {
        frgShoppingList.fillView(ingredients);

    }

    @Override
    public void onLoaderReset(Loader<List<Ingredient>> loader) {
        ingredients = null;
        fillView();
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
        frgShoppingList.fillView(ingredients);
        Toast.makeText(this, R.string.shopping_list_cleared, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIngredientSelected(Ingredient ingredient) {
        if (ingredient != null) {
            ingredientDao.createIngredientsIfNeeded(Collections.singletonList(ingredient));
            shoppingDao.createOrUpdate(ingredient);
        }
    }

    @Override
    public void onIngredientClicked(Ingredient ingredient) {
        if (ingredient != null) {
            shoppingDao.delete(ingredient);
        }
    }
}
