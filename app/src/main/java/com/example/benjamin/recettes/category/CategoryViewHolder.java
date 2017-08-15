package com.example.benjamin.recettes.category;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Category;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private final TextView txtName;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        txtName = (TextView) itemView.findViewById(R.id.text);
    }

    public void bind(Category category) {
        txtName.setText(category.getName());
    }
}
