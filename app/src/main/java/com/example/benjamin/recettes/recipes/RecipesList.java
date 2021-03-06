package com.example.benjamin.recettes.recipes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.Tags;
import com.example.benjamin.recettes.db.dao.BatchCookingDao;
import com.example.benjamin.recettes.db.dao.CategoryDao;
import com.example.benjamin.recettes.db.dao.RecipeDao;
import com.example.benjamin.recettes.db.dao.TagDao;
import com.example.benjamin.recettes.recipes.createForm.RecipeCreate;
import com.example.benjamin.recettes.task.AsyncTaskDataLoader;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.Predicate;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import static com.example.benjamin.recettes.data.Recipe.RecipeFiller.WITH_CAT;
import static com.example.benjamin.recettes.data.Recipe.RecipeFiller.WITH_TAGS;

public class RecipesList extends DrawerActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>,RecyclerViewClickListener,RecipeAdapter.ActionRecipeListener{

    public static final String NB_RECIPES_IMPORTED = "NB_RECIPES_IMPORTED";

    private RecipeAdapter recipeAdapter;
    private RecipeDao recipeDao;
    private CategoryDao categoryDao;
    private TagDao tagDao;
    private BatchCookingDao batchCookingDao;
    private boolean sortByAlpha = true;
    private SearchView searchView;
    private List<Recipe> recipes;
    private boolean layoutGrid = false;
    private RecyclerView recyclerView;
    private List<Category> allCategory;
    private FilterSelectAdapter catAdapter;
    private Set<Category> selectedCategories = new HashSet<>();
    private AlertDialog dialogFilters;
    private Float ratingSelected;
    private RatingBar ratingBar;
    private CheckBox cbSelectAllCats;
    private FloatingActionButton fab;
    private FilterSelectAdapter tagAdapter;
    private CheckBox cbSelectAllTags;
    private Set<Tags> selectedTags = new HashSet<>();
    private List<Tags> allTags;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_recipes);
        setContent(R.layout.recipes_list_layout);

        recipeDao = new RecipeDao(this);
        categoryDao = new CategoryDao(this);
        tagDao = new TagDao(this);
        batchCookingDao = new BatchCookingDao(this);
        initDaos(recipeDao,categoryDao,batchCookingDao,tagDao);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get(NB_RECIPES_IMPORTED) != null) {
            int nbRecipesImported = (int) extras.get(NB_RECIPES_IMPORTED);
            Toast.makeText(this, nbRecipesImported + " " + getString(R.string.imported_recipes), Toast.LENGTH_SHORT).show();
        }

        getSupportLoaderManager().initLoader(AsyncTaskDataLoader.getNewUniqueLoaderId(), null, this);

        recipeAdapter = new RecipeAdapter(this,this);
        recyclerView = (RecyclerView) findViewById(R.id.lstRecipes);
        setLayout();
        recyclerView.setAdapter(recipeAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CollectionUtils.nullOrEmpty(recipes)) {
                    goToCreateActivity();
                } else {
                    showFilterDialog();
                }
            }
        });

    }

    private void goToCreateActivity() {
        startActivity(new Intent(RecipesList.this, RecipeCreate.class));
    }


    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskDataLoader<List<Recipe>>(this) {
            @Override
            public List<Recipe> loadInBackground() {
                allCategory = categoryDao.getAllCategory();
                allTags = tagDao.getAllTags();
                return recipeDao.getAllRecipes(EnumSet.of(WITH_CAT, WITH_TAGS));
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        this.recipes = data;
        recipeAdapter.setDatas(this.recipes);
        dialogFilters = buildDialogBoxFilters();
        if (CollectionUtils.notNullOrEmpty(recipes)) {
            fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_tune_white_24dp));
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        this.recipes = null;
        recipeAdapter.setDatas(null);
    }

    @Override
    public void onItemClick(View view, int position) {
        Recipe recipe = recipeAdapter.getItem(position);
        Intent intent = new Intent(RecipesList.this, RecipeCreate.class);
        intent.putExtra(RecipeCreate.CURRENT_RECIPE, recipe);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_recipe_list,menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchText) {
                doSearch(searchText);
                return false;
            }
        });
        return true;

    }

    private void doSearch(final String searchText) {
        List<Recipe> recipesFiltred = CollectionUtils.filter(recipes, new Predicate<Recipe>() {
            @Override
            public boolean apply(Recipe element) {
                return element.getName() != null && element.getName().toLowerCase().contains(searchText.toLowerCase());
            }
        });
        recipeAdapter.setDatas(recipesFiltred);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteAllRecipe();
                break;
            case R.id.action_sort_alpha:
                sortByAlpha();
                break;
            case R.id.action_add_recipe:
                goToCreateActivity();
                break;
            case R.id.action_change_layout:
                this.layoutGrid = !layoutGrid;
                setLayout();
                swapIcon(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        cbSelectAllCats.setChecked(selectedCategories.size() == allCategory.size());
        catAdapter.setSelectedElements(selectedCategories);
        ratingBar.setRating(ratingSelected != null ? ratingSelected : 0);
        if (!dialogFilters.isShowing()) {
            dialogFilters.show();
        }

    }

    @NonNull
    private AlertDialog buildDialogBoxFilters() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View filterView = getLayoutInflater().inflate(R.layout.recipe_filter,null);

        final RecyclerView gridCategory = (RecyclerView) filterView.findViewById(R.id.gridCategory);
        gridCategory.setLayoutManager(new GridLayoutManager(this,2));
        catAdapter = new FilterSelectAdapter();
        catAdapter.setDatas(allCategory);
        catAdapter.setSelectedElements(selectedCategories);
        gridCategory.setAdapter(catAdapter);
        cbSelectAllCats = (CheckBox) filterView.findViewById(R.id.selectAllCat);
        cbSelectAllCats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && allCategory != null) {
                    selectedCategories.addAll(allCategory);
                } else {
                    selectedCategories.clear();
                }
                catAdapter.setSelectedElements(selectedCategories);
            }
        });

        final RecyclerView gridCats = (RecyclerView) filterView.findViewById(R.id.gridTags);
        gridCats.setLayoutManager(new GridLayoutManager(this,2));
        tagAdapter = new FilterSelectAdapter();
        tagAdapter.setDatas(allTags);
        tagAdapter.setSelectedElements(selectedTags);
        gridCats.setAdapter(tagAdapter);
        cbSelectAllTags = (CheckBox) filterView.findViewById(R.id.selectAllTags);
        cbSelectAllTags.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && allTags != null) {
                    selectedTags.addAll(allTags);
                } else {
                    selectedTags.clear();
                }
                tagAdapter.setSelectedElements(selectedTags);
            }
        });



        ratingBar = (RatingBar) filterView.findViewById(R.id.ratingBar);

        builder.setPositiveButton("OK", new DialogInterface.    OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedCategories = catAdapter.getSelectedElements();
                selectedTags = tagAdapter.getSelectedElements();
                ratingSelected = ratingBar.getRating();
                filter();
            }
        });
        builder.setNeutralButton("RAZ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedCategories = new HashSet<>();
                selectedTags = new HashSet<>();
                ratingSelected = null;
                filter();
            }
        });
        builder.setView(filterView);
        builder.setTitle(R.string.filters);
        return builder.create();
    }

    private void filter() {
        List<Recipe> filtredRecipes = new ArrayList<>(recipes);
        filterByCategories(filtredRecipes);
        filterByRating(filtredRecipes);
        filterByTags(filtredRecipes);
        recipeAdapter.setDatas(filtredRecipes);
    }

    private void filterByTags(List<Recipe> filtredRecipes) {
        if (CollectionUtils.notNullOrEmpty(selectedTags)) {
            ListIterator<Recipe> iterator = filtredRecipes.listIterator();
            while (iterator.hasNext()) {
                Recipe recipe = iterator.next();
                for (Tags tag : selectedTags) {
                    if (!recipe.getTags().contains(tag)) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    private void filterByRating(List<Recipe> filtredRecipes) {

        if (ratingSelected != null && ratingSelected > 1) {
            ListIterator<Recipe> iterator = filtredRecipes.listIterator();
            while (iterator.hasNext()) {
                Recipe recipe = iterator.next();
                if (recipe.getRating() == null || recipe.getRating() < ratingSelected) {
                    iterator.remove();
                }
            }
        }
    }

    private void filterByCategories(List<Recipe> filtredRecipes) {
        if (CollectionUtils.notNullOrEmpty(selectedCategories)) {
            ListIterator<Recipe> iterator = filtredRecipes.listIterator();
            while (iterator.hasNext()) {
                Recipe recipe = iterator.next();
                for (Category category : selectedCategories) {
                    if (!recipe.getCategories().contains(category)) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    private void swapIcon(MenuItem item) {
        if (layoutGrid) {
            item.setIcon(getDrawable(R.drawable.ic_view_stream_white_24dp));
        } else {
            item.setIcon(getDrawable(R.drawable.ic_view_module_white_24dp));
        }
    }

    private void setLayout() {
        if (layoutGrid) {
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private void sortByAlpha() {
        recipeAdapter.sort(new Comparator<Recipe>() {
            @Override
            public int compare(Recipe o1, Recipe o2) {
                if (o1.getName() == null) {
                    return 1;
                }
                if (o2.getName() == null) {
                    return -1;
                }
                if (sortByAlpha) {
                    return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
                } else {
                    return o2.getName().toUpperCase().compareTo(o1.getName().toUpperCase());
                }
            }
        });
        sortByAlpha = !sortByAlpha;
    }

    private void deleteAllRecipe() {
        recipeDao.deleteAll();
        recipeAdapter.setDatas(null);
    }

    @Override
    public void onIconBatchClicked(Recipe recipe) {
        addToBatchCooking(recipe);
    }

    private void addToBatchCooking(Recipe recipe) {
        recipe = recipeDao.findById(recipe.getId());
        batchCookingDao.addRecipeToBatchCooking(recipe);
        Toast.makeText(this, "La recette a été ajouté au batch cooking", Toast.LENGTH_SHORT).show();
    }
}
