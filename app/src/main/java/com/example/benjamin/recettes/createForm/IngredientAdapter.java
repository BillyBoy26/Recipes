package com.example.benjamin.recettes.createForm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientViewHolder> {

    private List<Ingredient> ingredientList = new ArrayList<>();



    public IngredientAdapter() {
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_card, parent,false);
        final IngredientViewHolder viewHolder = new IngredientViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        holder.bind(ingredientList.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public void addIngredient(Ingredient ingredient) {
        if (!ingredientList.contains(ingredient)) {
            ingredientList.add(ingredient);
            notifyItemInserted(ingredientList.size() - 1);

        }
    }

    private void removeItem(int position) {
        ingredientList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,ingredientList.size());
    }

    public void setIngredient(List<Ingredient> ingredients) {
        this.ingredientList = ingredients;
        if (this.ingredientList == null) {
            this.ingredientList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }
}
