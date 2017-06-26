package com.example.benjamin.recettes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benjamin.recettes.data.Recipe;
import com.squareup.picasso.Picasso;

class RecipeViewHolder extends RecyclerView.ViewHolder {


    private final ImageView imageView;
    private final TextView textView;

    public RecipeViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        textView = (TextView) itemView.findViewById(R.id.text);
    }

    public void bind(Recipe recipe) {
        textView.setText(recipe.getName());
        Picasso.with(imageView.getContext()).load(recipe.getUrlImage()).centerCrop().fit().into(imageView);
    }
}
