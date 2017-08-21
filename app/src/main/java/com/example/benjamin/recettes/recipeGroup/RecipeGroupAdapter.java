package com.example.benjamin.recettes.recipeGroup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;

import java.util.ArrayList;
import java.util.List;

public class RecipeGroupAdapter extends RecyclerView.Adapter<RecipeGroupViewHolder>{
    private final RecyclerViewClickListener clickListener;
    private List<RecipeGroup> recipeGroups = new ArrayList<>();

    public RecipeGroupAdapter(RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public RecipeGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_group_card,parent,false);
        return new RecipeGroupViewHolder(cardView,clickListener);
    }

    @Override
    public void onBindViewHolder(RecipeGroupViewHolder holder, int position) {
        holder.bind(recipeGroups.get(position));
    }

    @Override
    public int getItemCount() {
        return recipeGroups.size();
    }

    public void setRecipeGroup(List<RecipeGroup> recipeGroups) {
        this.recipeGroups = recipeGroups;
        if (this.recipeGroups == null) {
            this.recipeGroups = new ArrayList<>();
        }
        notifyDataSetChanged();
    }
}
