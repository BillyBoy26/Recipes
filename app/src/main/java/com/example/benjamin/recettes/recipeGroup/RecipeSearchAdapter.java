package com.example.benjamin.recettes.recipeGroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.utils.ImageUtils;
import com.example.benjamin.recettes.views.BasicListAdapter;
import com.example.benjamin.recettes.views.ClickableViewHolder;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;

public class RecipeSearchAdapter extends BasicListAdapter<Recipe,RecipeSearchAdapter.RecipeSearchViewHolder> {

    private final RecyclerViewClickListener clickListener;

    public RecipeSearchAdapter(RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public RecipeSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        return new RecipeSearchViewHolder(cardView,clickListener);
    }

    @Override
    public void onBindViewHolder(RecipeSearchViewHolder holder, Recipe recipe, int position) {
        holder.bind(recipe);
    }


    public class RecipeSearchViewHolder extends ClickableViewHolder{

        private final ImageView imageView;
        private final TextView txtName;

        public RecipeSearchViewHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView,clickListener);
            txtName = (TextView) itemView.findViewById(R.id.text);
            imageView = (ImageView) itemView.findViewById(R.id.image1);
        }

        public void bind(Recipe recipe) {
            txtName.setText(recipe.getName());
            ImageUtils.loadImage(recipe.getMainImage(),imageView,R.drawable.defaut_recipe);

        }



    }
}
