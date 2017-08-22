package com.example.benjamin.recettes.category;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.views.BasicListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BasicListAdapter<Category,CategoryAdapter.CategoryViewHolder> {

    private List<Integer> selectedPos = new ArrayList<>();

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_text, parent, false);
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
    public void onBindViewHolder(CategoryViewHolder holder, Category category, int position) {
        holder.bind(category,selectedPos.contains(position));
    }



    public List<Category> getSelectedCategories() {
        List<Category> selectedCat = new ArrayList<>();
        for (Integer pos : selectedPos) {
            selectedCat.add(datas.get(pos));
        }
        return selectedCat;
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.text);
        }

        public void bind(Category category, boolean isSelected) {
            itemView.setBackgroundColor(isSelected ? itemView.getContext().getResources().getColor(R.color.colorPrimaryLight,null): Color.TRANSPARENT);
            txtName.setText(category.getName());
        }

    }
}
