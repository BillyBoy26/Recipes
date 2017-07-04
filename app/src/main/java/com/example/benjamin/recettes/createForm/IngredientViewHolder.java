package com.example.benjamin.recettes.createForm;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Ingredient;

public class IngredientViewHolder extends RecyclerView.ViewHolder {

    private final TextView textView;
    private final ImageView imageView;

    public IngredientViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.textView);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);

    }

    public void bind(Ingredient ingredient) {
        textView.setText(ingredient.getName());
        if (ingredient.getImage() > 0) {
//            imageView.setImageResource(R.drawable.ic_cake_white_24dp);
        }
    }


}
