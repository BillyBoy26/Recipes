package com.example.benjamin.recettes.recipes.createForm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.task.DownloadImageTask;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.ImageInputView;
import com.google.android.flexbox.FlexboxLayout;

public class FragmentGeneral  extends Fragment implements RecipeCreate.RecipeFiller{


    private EditText txtName;
    private EditText txtTimePrepare;
    private EditText txtTimeCook;
    private EditText txtTotalTime;
    private EditText txtNbCovers;
    private ImageInputView imageView;
    private Recipe recipe;
    private EditText txtCategory;
    private FlexboxLayout pnlCategories;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View generalView = inflater.inflate(R.layout.recipe_create_general, container, false);
        txtName = (EditText) generalView.findViewById(R.id.name);
        imageView = (ImageInputView) generalView.findViewById(R.id.image1);
        txtCategory = (EditText) generalView.findViewById(R.id.category);
        txtNbCovers = (EditText) generalView.findViewById(R.id.nb_covers);
        txtTimeCook = (EditText) generalView.findViewById(R.id.time_cook);
        txtTimePrepare = (EditText) generalView.findViewById(R.id.time_prepare);
        txtTotalTime = (EditText) generalView.findViewById(R.id.time_total);
        pnlCategories = (FlexboxLayout) generalView.findViewById(R.id.pnlCategories);
        ImageView btnAddCat = (ImageView) generalView.findViewById(R.id.iconAddCat);
        btnAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String catName = txtCategory.getText().toString();
                if (SUtils.nullOrEmpty(catName)) {
                    return;
                }
                addCatToView(catName, inflater);
                txtCategory.setText("");
                recipe.getCategories().add(new Category(catName));
            }
        });
        fillRecipeView();
        return generalView;
    }

    private void addCatToView(final String catName, LayoutInflater inflater) {
        final Button btn = (Button) inflater.inflate(R.layout.btn_tag, null);
        btn.setText(catName);
        pnlCategories.addView(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Category category : recipe.getCategories()) {
                    if (catName.equalsIgnoreCase(category.getName())) {
                        recipe.getCategories().remove(category);
                        break;
                    }
                }
                pnlCategories.removeView(btn);
            }
        });
    }

    public void fillRecipeView() {
        if (recipe != null && txtName != null) {
            txtName.setText(recipe.getName());
            txtTimeCook.setText(recipe.getCookTime());
            txtNbCovers.setText(recipe.getNbCovers());
            txtTotalTime.setText(recipe.getTotalTime());
            txtTimePrepare.setText(recipe.getPrepareTime());
            if (recipe.getUrlImage() != null) {
                new DownloadImageTask(imageView).execute(recipe.getUrlImage());
            }


            for (Category category : recipe.getCategories()) {
                addCatToView(category.getName(),getActivity().getLayoutInflater());
            }
        }

    }

    @Override
    public void getRecipe() {
        String name = txtName.getText().toString();
        String imageUrl = imageView.getUrlImage();
        recipe.setName(name);
        recipe.setUrlImage(imageUrl);
        recipe.setPrepareTime(txtTimePrepare.getText().toString());
        recipe.setCookTime(txtTimeCook.getText().toString());
        recipe.setTotalTime(txtTotalTime.getText().toString());
        recipe.setNbCovers(txtNbCovers.getText().toString());
        //les catégories sont gérés automatiquement
    }


    @Override
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        fillRecipeView();
    }
}
