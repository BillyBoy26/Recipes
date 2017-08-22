package com.example.benjamin.recettes.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BasicListAdapter<T,V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    protected List<T> datas = new ArrayList<>();



    @Override
    public abstract V onCreateViewHolder(ViewGroup parent, int viewType) ;

    @Override
    public void onBindViewHolder(V holder, int position) {
        onBindViewHolder(holder, datas.get(position),position);
    }

    public abstract void onBindViewHolder(V holder, T data,int position);

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public @NonNull List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        if (this.datas == null) {
            this.datas = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public void addItem(T data) {
        if (!datas.contains(data)) {
            datas.add(data);
            notifyItemInserted(datas.size() - 1);

        }
    }

    public T removeItem(int position) {
        T ingRemoved = datas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,datas.size());
        return ingRemoved;
    }

    public void moveItem(int oldPosition, int targetPosition) {
        Collections.swap(datas, oldPosition, targetPosition);
        notifyItemMoved(oldPosition,targetPosition);
        notifyItemChanged(oldPosition);
        notifyItemChanged(targetPosition);
    }

    public T getItem(int position) {
        return datas.get(position);
    }

}
