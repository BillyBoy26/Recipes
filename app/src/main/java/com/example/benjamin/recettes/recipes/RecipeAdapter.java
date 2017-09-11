package com.example.benjamin.recettes.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.utils.ImageUtils;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.BasicListAdapter;
import com.example.benjamin.recettes.views.ClickableViewHolder;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;

public class RecipeAdapter extends BasicListAdapter<Recipe,RecipeAdapter.RecipeViewHolder> {

    interface ActionRecipeListener{
        void onIconBatchClicked(Recipe recipe);
    }


    private ActionRecipeListener actionRecipeListener;


    private final RecyclerViewClickListener clickListener;

    public RecipeAdapter(RecyclerViewClickListener clickListener) {
        this(clickListener,null);
    }
    public RecipeAdapter(RecyclerViewClickListener clickListener,ActionRecipeListener actionRecipeListener) {
        this.clickListener = clickListener;
        this.actionRecipeListener = actionRecipeListener;
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
        private final RatingBar ratingBar;
        private final ImageView iconBatchCook;

        public RecipeViewHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView,clickListener);
            imageView = (ImageView) itemView.findViewById(R.id.image1);
            textView = (TextView) itemView.findViewById(R.id.text);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            iconBatchCook = (ImageView) itemView.findViewById(R.id.iconAddBatch);
            LinearLayout pnlActions = (LinearLayout) itemView.findViewById(R.id.pnlActions);
            pnlActions.setVisibility(actionRecipeListener != null ? View.VISIBLE:View.GONE);
        }

        public void bind(final Recipe recipe) {
            textView.setText(SUtils.capitalize(recipe.getName()));
            ratingBar.setRating(recipe.getRating() != null ? recipe.getRating():0);
            ImageUtils.loadImage(recipe.getMainImage(),imageView,R.drawable.defaut_recipe);
            iconBatchCook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actionRecipeListener != null) {
                        actionRecipeListener.onIconBatchClicked(recipe);
                    }
                }
            });
        }
    }
}
