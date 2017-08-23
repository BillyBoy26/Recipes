package com.example.benjamin.recettes.recipeGroup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.RecipeGroup;

public class FrgGroupGeneral extends Fragment {
    private EditText txtName;
    private RecipeGroup recipeGroup;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View generalView = inflater.inflate(R.layout.recipes_group_create_general, container, false);
        txtName = (EditText) generalView.findViewById(R.id.txtName);
        return generalView;
    }

    public void getRecipeGroup() {
        recipeGroup.setName(txtName.getText().toString());
    }



    public void fillView(RecipeGroup recGroup) {
        this.recipeGroup = recGroup;
        txtName.setText(this.recipeGroup.getName());

    }
}
