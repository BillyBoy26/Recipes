package com.example.benjamin.recettes.createForm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.utils.SUtils;

import java.util.ArrayList;

public class FragmentSteps extends Fragment implements RecipeCreate.RecipeFiller{


    private StepAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.recipe_create_steps, container, false);
        final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerSteps);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StepAdapter(new ArrayList<String>());
        recyclerView.setAdapter(adapter);

        final EditText editTxtStep = (EditText) layout.findViewById(R.id.editTxtStep);
        final ImageView iconAddStep = (ImageView) layout.findViewById(R.id.iconAddStep);
        iconAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SUtils.nullOrEmpty(editTxtStep.getText().toString())) {
                    return;
                }
                adapter.addStep(editTxtStep.getText().toString());
            }
        });
        return layout;
    }

    @Override
    public void setRecipe(Recipe recipe) {

    }

    @Override
    public void getRecipe() {

    }
}
