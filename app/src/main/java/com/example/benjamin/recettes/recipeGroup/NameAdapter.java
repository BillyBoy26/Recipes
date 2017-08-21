package com.example.benjamin.recettes.recipeGroup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;

import java.util.ArrayList;
import java.util.List;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.NameViewHolder> {

    private List<Recipe> recipes = new ArrayList<>();

    @Override
    public NameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_text, parent, false);
        return new NameViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(NameViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
        notifyItemInserted(recipes.size() - 1);
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        if (this.recipes == null) {
            recipes = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }


    public class NameViewHolder extends RecyclerView.ViewHolder{

        private final TextView txtName;

        public NameViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.text);
        }

        public void bind(Recipe recipe) {
            txtName.setText(recipe.getName());
        }
    }
}
