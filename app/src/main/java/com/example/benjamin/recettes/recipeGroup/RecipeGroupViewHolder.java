package com.example.benjamin.recettes.recipeGroup;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;
import com.squareup.picasso.Picasso;

public class RecipeGroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView txtName;
    private final ImageView image;
    private final RecyclerViewClickListener clickListener;

    public RecipeGroupViewHolder(View itemView, RecyclerViewClickListener clickListener) {
        super(itemView);
        txtName = (TextView) itemView.findViewById(R.id.text);
        image = (ImageView) itemView.findViewById(R.id.image1);
        this.clickListener = clickListener;
        itemView.setOnClickListener(this);
    }

    public void bind(RecipeGroup recipeGroup) {
        txtName.setText(recipeGroup.getName());

        if (CollectionUtils.nullOrEmpty(recipeGroup.getRecipes())) {
            image.setImageResource(R.drawable.defaut_recipe);
        } else {
            for (Recipe recipe : recipeGroup.getRecipes()) {
                //TODO
                Picasso.with(image.getContext()).load(recipe.getUrlImage()).centerCrop().fit().into(image);
            }
        }
    }

    @Override
    public void onClick(View v) {
        clickListener.onItemClick(v,getLayoutPosition());
    }
}
