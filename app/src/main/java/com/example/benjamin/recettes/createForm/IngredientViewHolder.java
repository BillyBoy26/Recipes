package com.example.benjamin.recettes.createForm;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.utils.SUtils;

public class IngredientViewHolder extends RecyclerView.ViewHolder {

    private final TextView textViewName;
    private final TextView textViewQuantity;
    private final ImageView imageView;

    public IngredientViewHolder(View itemView) {
        super(itemView);
        textViewName = (TextView) itemView.findViewById(R.id.textViewName);
        textViewQuantity = (TextView) itemView.findViewById(R.id.textViewQuant);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);

    }

    public void bind(Ingredient ingredient) {
        textViewName.setText(ingredient.getName());
        if (ingredient.getImage() > 0) {
//            imageView.setImageResource(R.drawable.ic_cake_white_24dp);
        }
        if (ingredient.getQuantity() > 0) {
            String quantity = String.valueOf(ingredient.getQuantity());
            if (SUtils.notNullOrEmpty(ingredient.getQuantityUnit())) {
                quantity += " " + ingredient.getQuantityUnit();
            }
            textViewQuantity.setText(quantity);
        }
    }


}
