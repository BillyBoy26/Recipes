package com.example.benjamin.recettes.createForm;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.benjamin.recettes.R;


public class StepViewHolder extends RecyclerView.ViewHolder {

    private final TextView txtStep;

    public StepViewHolder(View itemView) {
        super(itemView);
        txtStep = (TextView) itemView.findViewById(R.id.text);
    }

    public void bind(String step,int position) {
        txtStep.setText(String.valueOf(position) + ". " + step);
    }
}
