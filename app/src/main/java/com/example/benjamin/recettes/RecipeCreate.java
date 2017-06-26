package com.example.benjamin.recettes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.benjamin.recettes.data.Recipe;

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
                Intent intent = new Intent(RecipeCreate.this, RecipesActivity.class);
                intent.putExtra(NEW_RECIPE, new Recipe(txtName.getText().toString(), txtUrl.getText().toString()));
                startActivity(intent);
            }
        });
    }
}
