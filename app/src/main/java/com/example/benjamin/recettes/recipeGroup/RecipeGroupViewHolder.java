package com.example.benjamin.recettes.recipeGroup;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.RecipeGroup;

public class RecipeGroupViewHolder extends RecyclerView.ViewHolder {

    private final TextView txtName;

    public RecipeGroupViewHolder(View itemView) {
        super(itemView);
        txtName = (TextView) itemView.findViewById(R.id.text);
    }

    public void bind(RecipeGroup recipeGroup) {
        txtName.setText(recipeGroup.getName());
    }
}
