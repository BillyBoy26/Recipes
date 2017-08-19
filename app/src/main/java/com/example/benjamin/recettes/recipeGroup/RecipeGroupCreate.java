package com.example.benjamin.recettes.recipeGroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.db.dao.RecipeGroupDao;

public class RecipeGroupCreate extends DrawerActivity {

    private EditText txtName;
    private RecipeGroup recipeGroup;
    private RecipeGroupDao recipeGroupDao;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(R.layout.recipes_group_create);
        recipeGroupDao = new RecipeGroupDao(this);
        initDaos(recipeGroupDao);
        txtName = (EditText) findViewById(R.id.txtName);

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
        fillRecipeGroup();

        recipeGroupDao.createOrUpdate(recipeGroup);
        return true;
    }

    private void fillRecipeGroup() {
        if (recipeGroup == null) {
            recipeGroup = new RecipeGroup();
        }
        recipeGroup.setName(txtName.getText().toString());
    }
}
