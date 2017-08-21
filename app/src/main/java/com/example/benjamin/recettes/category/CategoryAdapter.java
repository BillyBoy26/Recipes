package com.example.benjamin.recettes.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private List<Category> categories = new ArrayList<>();
    private List<Integer> selectedPos = new ArrayList<>();

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_card, parent, false);
        final CategoryViewHolder viewHolder = new CategoryViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = viewHolder.getAdapterPosition();
                if (currentPos == RecyclerView.NO_POSITION) {
                    return;
                }
                if (selectedPos.contains(currentPos)) {
                    selectedPos.remove(selectedPos.indexOf(currentPos));
                } else {
                    selectedPos.add(currentPos);
                }
                notifyItemChanged(currentPos);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position),selectedPos.contains(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        this.categories = categories;
        selectedPos.clear();
        notifyDataSetChanged();
    }

    public List<Category> getSelectedCategories() {
        List<Category> selectedCat = new ArrayList<>();
        for (Integer pos : selectedPos) {
            selectedCat.add(categories.get(pos));
        }
        return selectedCat;
    }
}
