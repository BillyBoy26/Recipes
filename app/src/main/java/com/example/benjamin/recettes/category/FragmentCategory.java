package com.example.benjamin.recettes.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Category;

import java.util.List;

public class FragmentCategory extends Fragment {

    private CardsAdapter<Category> categoryAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.recycler_layout, container, false);

        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initAdapter();

        return layout;
    }

    private void initAdapter() {
        if (categoryAdapter == null) {
            categoryAdapter = new CardsAdapter<>();
            recyclerView.setAdapter(categoryAdapter);
        }
    }

    public void setCategories(List<Category> categories) {
        initAdapter();
        categoryAdapter.setDatas(categories);
    }

    public List<Category> getSelectedCategories() {
        return categoryAdapter.getSelectedDatas();
    }
}
