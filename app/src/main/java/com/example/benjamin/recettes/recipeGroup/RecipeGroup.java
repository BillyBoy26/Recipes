package com.example.benjamin.recettes.recipeGroup;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;

public class RecipeGroup extends DrawerActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNavigationView().setCheckedItem(R.id.nav_batch_cooking);
        setContent(R.layout.recipes_group_layout);

    }
}
