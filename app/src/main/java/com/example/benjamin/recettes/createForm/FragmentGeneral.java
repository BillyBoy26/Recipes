package com.example.benjamin.recettes.createForm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.task.DownloadImageTask;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.ImageInputView;

import java.util.ArrayList;

public class FragmentGeneral  extends Fragment implements RecipeCreate.RecipeFiller{


    private EditText txtName;
    private ImageInputView imageView;
    private Recipe recipe;
    private EditText txtCategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View generalView = inflater.inflate(R.layout.recipe_create_general, container, false);
        txtName = (EditText) generalView.findViewById(R.id.name);
        imageView = (ImageInputView) generalView.findViewById(R.id.image1);
        txtCategory = (EditText) generalView.findViewById(R.id.category);
        fillRecipeView();
        return generalView;
    }

    public void fillRecipeView() {
        if (recipe != null && txtName != null) {
            txtName.setText(recipe.getName());
            if (recipe.getUrlImage() != null) {
                new DownloadImageTask(imageView).execute(recipe.getUrlImage());
            }
            //TODO
            for (Category category : recipe.getCategories()) {
                txtCategory.setText(category.getName());
            }
        }

    }

    @Override
    public void getRecipe() {
        String name = txtName.getText().toString();
        String imageUrl = imageView.getUrlImage();
        recipe.setName(name);
        recipe.setUrlImage(imageUrl);
        if (SUtils.notNullOrEmpty(txtCategory.getText().toString())) {
            recipe.getCategories().add(new Category(txtCategory.getText().toString()));
        } else {
            recipe.setCategories(new ArrayList<Category>());
        }
    }


    @Override
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        fillRecipeView();
    }
}
