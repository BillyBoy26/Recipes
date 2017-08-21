package com.example.benjamin.recettes.recipeGroup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeSearchAdapter extends RecyclerView.Adapter<RecipeSearchAdapter.RecipeSearchViewHolder> {

    private final RecyclerViewClickListener clickListener;
    private List<Recipe> recipes = new ArrayList<>();

    public RecipeSearchAdapter(RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public RecipeSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new RecipeSearchViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(RecipeSearchViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        if (this.recipes == null) {
            this.recipes = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public List<Recipe> getRecipes() {
        return this.recipes;
    }


    public class RecipeSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ImageView imageView;
        private final TextView txtName;

        public RecipeSearchViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.text);
            imageView = (ImageView) itemView.findViewById(R.id.image1);
            itemView.setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            txtName.setText(recipe.getName());

            String urlImage = recipe.getUrlImage();
            if (SUtils.notNullOrEmpty(urlImage)) {
                Picasso.with(imageView.getContext()).load(urlImage).centerCrop().fit().into(imageView);
            } else {
                imageView.setImageResource(R.drawable.defaut_recipe);
            }
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v,getLayoutPosition());
        }
    }
}
