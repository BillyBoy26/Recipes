package com.example.benjamin.recettes.recipes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.BasicListAdapter;

import java.util.HashSet;
import java.util.Set;

public class CategoryFilterAdapter extends BasicListAdapter<Category,CategoryFilterAdapter.CategoryFilterViewHolder> {

    private Set<Category> selectedCategories = new HashSet<>();

    public CategoryFilterAdapter() {
        super();
    }

    @Override
    public CategoryFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_filter_item, parent, false);
        return new CategoryFilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryFilterViewHolder holder, Category data, int position) {
        holder.bind(data, selectedCategories.contains(data));
    }

    public Set<Category> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(Set<Category> selectedCategories) {
        this.selectedCategories = selectedCategories;
        if (this.selectedCategories == null) {
            this.selectedCategories = new HashSet<>();
        }
        notifyDataSetChanged();
    }

    class CategoryFilterViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox cbCategory;

        CategoryFilterViewHolder(View itemView) {
            super(itemView);
            cbCategory = (CheckBox) itemView.findViewById(R.id.cbCategory);
            cbCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedCategories.add(datas.get(getLayoutPosition()));
                    } else {
                        selectedCategories.remove(datas.get(getLayoutPosition()));
                    }
                }
            });
        }

        void bind(Category category, boolean selected) {
            cbCategory.setText(SUtils.capitalize(category.getName()));
            cbCategory.setChecked(selected);
        }
    }
}
