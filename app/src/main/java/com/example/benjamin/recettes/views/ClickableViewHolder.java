package com.example.benjamin.recettes.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class ClickableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    private RecyclerViewClickListener clickListener;
    private RecyclerViewLongClickListener longClickListener;
    public ClickableViewHolder(View itemView, RecyclerViewClickListener clickListener) {
        this(itemView, clickListener, null);
    }
    public ClickableViewHolder(View itemView, RecyclerViewClickListener clickListener, RecyclerViewLongClickListener longClickListener) {
        super(itemView);
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onItemClick(v, getLayoutPosition());
        } else {
            Log.w("ClickableViewHolder", "clickListener is null");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        boolean callbackConsumed = false;
        if (longClickListener != null) {
            longClickListener.onItemLongClick(v,getLayoutPosition());
            callbackConsumed = true;
        }

        return callbackConsumed;
    }
}
