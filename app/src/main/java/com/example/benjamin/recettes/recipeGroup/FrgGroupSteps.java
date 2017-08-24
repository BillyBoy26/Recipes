package com.example.benjamin.recettes.recipeGroup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.RecipeGroup;
import com.example.benjamin.recettes.data.Step;
import com.example.benjamin.recettes.views.NameAdapter;

import java.util.List;

public class FrgGroupSteps extends Fragment {

    public static final String DATA_STEPS = "DATA_STEPS";
    private NameAdapter adapter;
    private RecipeGroup recipeGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View stepView = inflater.inflate(R.layout.recipes_group_create_steps, container, false);
        initAdapter();
        RecyclerView recyclerView = (RecyclerView) stepView.findViewById(R.id.lstSteps);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        return stepView;
    }

    private void initAdapter() {
        if (adapter == null) {
            adapter = new NameAdapter(true);
        }
    }


    public void getRecipeGroup() {
        List<Step> steps = adapter.getDatas();
        for (Step step : steps) {
            step.setRank(steps.indexOf(step) + 1);
        }
        recipeGroup.setSteps(steps);
    }

    public void fillView(RecipeGroup recipeGrp) {
        this.recipeGroup = recipeGrp;
        initAdapter();
        adapter.setDatas(recipeGroup.getSteps());
    }
}
