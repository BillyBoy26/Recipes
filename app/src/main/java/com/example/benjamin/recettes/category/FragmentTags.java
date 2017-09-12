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
import com.example.benjamin.recettes.data.Tags;

import java.util.List;

public class FragmentTags extends Fragment {

    private RecyclerView recyclerView;
    private CardsAdapter<Tags> tagsAdapter;

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
        if (tagsAdapter == null) {
            tagsAdapter = new CardsAdapter<>();
            recyclerView.setAdapter(tagsAdapter);
        }
    }

    public void setTags(List<Tags> tags) {
        initAdapter();
        tagsAdapter.setDatas(tags);
    }

    public List<Tags> getSelectedTags() {
        return tagsAdapter.getSelectedDatas();
    }
}
