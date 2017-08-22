package com.example.benjamin.recettes.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.BasicListAdapter;
import com.example.benjamin.recettes.views.ClickableViewHolder;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;
import com.squareup.picasso.Picasso;

class RecipeAdapter extends BasicListAdapter<Recipe,RecipeAdapter.RecipeViewHolder> {


    private final RecyclerViewClickListener clickListener;

    public RecipeAdapter(RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card,parent,false);
        return new RecipeViewHolder(cardView,clickListener);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, Recipe recipe, int position) {
        holder.bind(recipe);
    }


    class RecipeViewHolder extends ClickableViewHolder {


        private final ImageView imageView;
        private final TextView textView;

        public RecipeViewHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView,clickListener);
            imageView = (ImageView) itemView.findViewById(R.id.image1);
            textView = (TextView) itemView.findViewById(R.id.text);
        }

        public void bind(Recipe recipe) {
            textView.setText(recipe.getName());

            if (SUtils.notNullOrEmpty(recipe.getUrlImage())) {
                Picasso.with(imageView.getContext()).load(recipe.getUrlImage()).centerCrop().fit().into(imageView);
            } else {
                imageView.setImageResource(R.drawable.defaut_recipe);
            }
        }
    }
}
