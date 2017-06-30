package com.example.benjamin.recettes;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.RecipeContentProvider;
import com.example.benjamin.recettes.db.table.TRecipe;

public class RecipeCreate extends DrawerActivity {

    public static final String CURRENT_RECIPE = "NEW_RECIPE";
    private EditText txtName;
    private EditText txtUrl;
    private Recipe recipe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.recipe_create_form, contentFrameLayout);

        txtName = (EditText) findViewById(R.id.name);
        txtUrl = (EditText) findViewById(R.id.image);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.get(CURRENT_RECIPE) != null) {
            recipe = (Recipe) extras.get(CURRENT_RECIPE);
            if (recipe != null) {
                fillRecipe();
            }
        } else {
            recipe = null;
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_done_white_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrUpdateRecipe();
                startActivity(new Intent(RecipeCreate.this, RecipesActivity.class));
            }
        });
    }

    private void fillRecipe() {
        txtName.setText(recipe.getName());
        txtUrl.setText(recipe.getUrlImage());
    }

    private void createOrUpdateRecipe() {
        String name = txtName.getText().toString();
        String imageUrl = txtUrl.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TRecipe.C_NAME,name);
        contentValues.put(TRecipe.C_URL_IMAGE,imageUrl);

        if (recipe != null && recipe.getId() != null) {
            contentValues.put(TRecipe._ID, recipe.getId());
            Uri uri = Uri.parse(RecipeContentProvider.CONTENT_URI + "/" + recipe.getId());
            getContentResolver().update(uri,contentValues,null,null);
        } else {
            getContentResolver().insert(RecipeContentProvider.CONTENT_URI,contentValues);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (recipe != null) {
            getMenuInflater().inflate(R.menu.menu_toolbar,menu);
            return true;
        }
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete && recipe != null) {
            deleteRecipe();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteRecipe() {
        Uri uri = Uri.parse(RecipeContentProvider.CONTENT_URI + "/" + recipe.getId());
        getContentResolver().delete(uri, null, null);
        startActivity(new Intent(RecipeCreate.this, RecipesActivity.class));
    }
}
