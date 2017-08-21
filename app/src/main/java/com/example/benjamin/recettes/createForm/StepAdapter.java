package com.example.benjamin.recettes.createForm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benjamin.recettes.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class StepAdapter extends RecyclerView.Adapter<StepViewHolder>{

    private List<String> steps = new ArrayList<>();

    public StepAdapter(List<String> steps) {
        setSteps(steps,false);
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_text, parent, false);
        return new StepViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        holder.bind(steps.get(position),position + 1);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }



    public void moveItem(int oldPosition, int targetPosition) {
        Collections.swap(steps, oldPosition, targetPosition);
        notifyItemMoved(oldPosition,targetPosition);
        notifyItemChanged(oldPosition);
        notifyItemChanged(targetPosition);
    }

    public void addStep(String step) {
        steps.add(step);
        notifyItemInserted(steps.size() - 1);
    }

    public void setSteps(List<String> steps, boolean notify) {
        this.steps = steps;
        if (steps == null) {
            this.steps = new ArrayList<>();
        }
        if (notify) {
            notifyDataSetChanged();
        }
    }
}
