package com.example.benjamin.recettes;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.benjamin.recettes.category.CategoryList;
import com.example.benjamin.recettes.db.dao.GenericDao;
import com.example.benjamin.recettes.recipeGroup.RecipeGroup;
import com.example.benjamin.recettes.shoppingList.ShoppingList;
import com.example.benjamin.recettes.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private List<GenericDao> daos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void enableTabs() {
        View viewById = findViewById(R.id.tabLayout);
        viewById.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_recipes :
                startActivity(new Intent(this, RecipesActivity.class));
                break;
            case R.id.nav_batch_cooking :
                startActivity(new Intent(this, RecipeGroup.class));
                break;
            case R.id.nav_import :
                startActivity(new Intent(this, RecipeImport.class));
                break;
            case R.id.nav_shopping_list :
                startActivity(new Intent(this, ShoppingList.class));
                break;
            case R.id.nav_category :
                startActivity(new Intent(this, CategoryList.class));
                break;
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    protected void setContent(int layoutId) {
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutId, contentFrameLayout);
    }

    protected void initDaos(GenericDao... activityDaos) {
        if (activityDaos != null) {
            daos = new ArrayList<>();
            for (GenericDao dao : activityDaos) {
                daos.add(dao);
            }
            openAllDaos();
        }
    }

    protected void openAllDaos() {
        if (CollectionUtils.notNullOrEmpty(daos)) {
            for (GenericDao dao : daos) {
                dao.open();
            }
        }
    }

    protected void closeAllDaos() {
        if (CollectionUtils.notNullOrEmpty(daos)) {
            for (GenericDao dao : daos) {
                dao.close();
            }
        }
    }

    @Override
    protected void onResume() {
        openAllDaos();
        super.onResume();
    }

    @Override
    protected void onPause() {
        closeAllDaos();
        super.onPause();
    }
}
