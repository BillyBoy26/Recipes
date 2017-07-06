package com.example.benjamin.recettes.createForm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;

public class FragmentSteps extends Fragment implements RecipeCreate.RecipeFiller{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_create_steps, container, false);
    }

    @Override
    public void setRecipe(Recipe recipe) {

    }

    @Override
    public void getRecipe() {

    }
}
