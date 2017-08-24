package com.example.benjamin.recettes.recipeGroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.views.BasicListAdapter;
import com.example.benjamin.recettes.views.ClickableViewHolder;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;
import com.squareup.picasso.Picasso;

public class RecipeGroupAdapter extends BasicListAdapter<RecipeGroup,RecipeGroupAdapter.RecipeGroupViewHolder> {
    private final RecyclerViewClickListener clickListener;

    public RecipeGroupAdapter(RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public RecipeGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_group_card,parent,false);
        return new RecipeGroupViewHolder(cardView,clickListener);
    }

    @Override
    public void onBindViewHolder(RecipeGroupViewHolder holder, RecipeGroup recGroup, int position) {
        holder.bind(recGroup);
    }

    class RecipeGroupViewHolder extends ClickableViewHolder {

        private final TextView txtName;
        private final ImageView image;

        public RecipeGroupViewHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView,clickListener);
            txtName = (TextView) itemView.findViewById(R.id.text);
            image = (ImageView) itemView.findViewById(R.id.image1);
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
    }
}
