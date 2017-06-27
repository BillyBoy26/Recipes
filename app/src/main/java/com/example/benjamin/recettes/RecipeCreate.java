package com.example.benjamin.recettes;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.benjamin.recettes.db.RecipeContentProvider;
import com.example.benjamin.recettes.db.table.TRecipe;

public class RecipeCreate extends DrawerActivity {

    public static final String NEW_RECIPE = "NEW_RECIPE";
    private EditText txtName;
    private EditText txtUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.recipe_create_form, contentFrameLayout);

        txtName = (EditText) findViewById(R.id.name);
        txtUrl = (EditText) findViewById(R.id.image);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_done_white_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRecipe();
                startActivity(new Intent(RecipeCreate.this, RecipesActivity.class));
            }
        });
    }

    private void createRecipe() {
        String name = txtName.getText().toString();
        String imageUrl = txtUrl.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TRecipe.C_NAME,name);
        contentValues.put(TRecipe.C_URL_IMAGE,imageUrl);
        getContentResolver().insert(RecipeContentProvider.CONTENT_URI,contentValues);
    }
}
