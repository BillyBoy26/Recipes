package com.example.benjamin.recettes.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class ClickableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private RecyclerViewClickListener clickListener;
    public ClickableViewHolder(View itemView, RecyclerViewClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onItemClick(v, getLayoutPosition());
        } else {
            Log.w("ClickableViewHolder", "clickListener is null");
        }
    }
}
