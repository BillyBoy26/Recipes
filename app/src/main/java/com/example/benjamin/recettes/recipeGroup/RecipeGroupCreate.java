package com.example.benjamin.recettes.recipeGroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.db.dao.RecipeDao;
import com.example.benjamin.recettes.db.dao.RecipeGroupDao;
import com.example.benjamin.recettes.db.dao.ShoppingDao;
import com.example.benjamin.recettes.task.AsyncTaskDataLoader;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.views.NameAdapter;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;

import java.util.EnumSet;
import java.util.List;

public class RecipeGroupCreate extends DrawerActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>,RecyclerViewClickListener {

    public static final String CURRENT_GROUP = "CURRENT_GROUP";
    private EditText txtName;
    private RecipeGroup recipeGroup;
    private RecipeGroupDao recipeGroupDao;
    private ShoppingDao shoppingDao;
    private RecipeDao recipeDao;
    private RecipeSearchAdapter recipeAdapter;
    private NameAdapter recipeLinkedAdapter;
    private List<Recipe> allRecipes;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.recipes_group_create);
        recipeGroupDao = new RecipeGroupDao(this);
        recipeDao = new RecipeDao(this);
        shoppingDao = new ShoppingDao(this);
        initDaos(recipeGroupDao,recipeDao,shoppingDao);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get(CURRENT_GROUP) != null) {
            recipeGroup = (RecipeGroup) extras.get(CURRENT_GROUP);
        }

        getSupportLoaderManager().initLoader(AsyncTaskDataLoader.getNewUniqueLoaderId(), null, this);

        recipeAdapter = new RecipeSearchAdapter(this);

        txtName = (EditText) findViewById(R.id.txtName);
        RecyclerView lstRecipesSearch = (RecyclerView) findViewById(R.id.recyclerRecipes);
        lstRecipesSearch.setAdapter(recipeAdapter);
        lstRecipesSearch.setLayoutManager(new GridLayoutManager(this,2));

        RecyclerView lstRecipesCreated = (RecyclerView) findViewById(R.id.recyclerRecipesLink);
        lstRecipesCreated.setLayoutManager(new LinearLayoutManager(this));
        recipeLinkedAdapter = new NameAdapter();
        lstRecipesCreated.setAdapter(recipeLinkedAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_done_white_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ok = createOrUpdateRecipeGroup();
                if (ok) {
                    startActivity(new Intent(RecipeGroupCreate.this, RecipeGroupList.class));
                }
            }
        });
    }


    private boolean createOrUpdateRecipeGroup() {
        getRecipeGroup();

        recipeGroupDao.createOrUpdate(recipeGroup);
        return true;
    }

    private void getRecipeGroup() {
        if (recipeGroup == null) {
            recipeGroup = new RecipeGroup();
        }
        recipeGroup.setName(txtName.getText().toString());
        recipeGroup.setRecipes(recipeLinkedAdapter.getDatas());
    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskDataLoader<List<Recipe>>(this) {
            @Override
            public List<Recipe> loadInBackground() {
                allRecipes = recipeDao.getAllRecipes(EnumSet.of(Recipe.RecipeFiller.WITH_ING));
                if (recipeGroup != null && recipeGroup.getId() != null) {
                    recipeGroup = recipeGroupDao.findById(recipeGroup.getId());
                }
                return allRecipes;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        recipeAdapter.setDatas(allRecipes);
        fillView();
    }

    private void fillView() {
        if (recipeGroup == null) {
            return;
        }
        txtName.setText(recipeGroup.getName());
        recipeLinkedAdapter.setDatas(recipeGroup.getRecipes());
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        recipeAdapter.setDatas(null);
        recipeLinkedAdapter.setDatas(null);
    }

    @Override
    public void onItemClick(View view, int position) {
        Recipe recipe = recipeAdapter.getItem(position);
        recipeLinkedAdapter.addItem(recipe);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (recipeGroup != null && recipeGroup.getId() != null) {
            getMenuInflater().inflate(R.menu.menu_toolbar_recipegrp_create,menu);
            return true;
        }
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteGroupRecipe();
                break;
            case R.id.action_add_shoppping_list:
                addToShoppingList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToShoppingList() {
        if (CollectionUtils.nullOrEmpty(recipeGroup.getRecipes())) {
            Toast.makeText(this, R.string.none_recipe_to_add_shopping_list, Toast.LENGTH_SHORT).show();
            return;
        }
        boolean created = shoppingDao.createFromRecipeGroup(recipeGroup);
        if (created) {
            Toast.makeText(this, R.string.ingredients_from_recipe_group_added_to_shopping_list, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.none_ingredient_to_add_shopping_list, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteGroupRecipe() {
        recipeGroupDao.delete(recipeGroup);
        startActivity(new Intent(RecipeGroupCreate.this, RecipeGroupList.class));
    }
}
