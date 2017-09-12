package com.example.benjamin.recettes.category;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.HasName;
import com.example.benjamin.recettes.views.BasicListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CardsAdapter<T extends HasName> extends BasicListAdapter<T,CardsAdapter.CardsViewHolder> {

    private List<Integer> selectedPos = new ArrayList<>();

    @Override
    public CardsViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_text, parent, false);
        final CardsViewHolder viewHolder = new CardsViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPos = viewHolder.getAdapterPosition();
                if (currentPos == RecyclerView.NO_POSITION) {
                    return;
                }
                if (selectedPos.contains(currentPos)) {
                    selectedPos.remove(selectedPos.indexOf(currentPos));
                } else {
                    selectedPos.add(currentPos);
                }
                notifyItemChanged(currentPos);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardsAdapter.CardsViewHolder holder, T data, int position) {
        holder.bind(data,selectedPos.contains(position));
    }


    public List<T> getSelectedDatas() {
        List<T> selectedCat = new ArrayList<>();
        for (Integer pos : selectedPos) {
            selectedCat.add(datas.get(pos));
        }
        return selectedCat;
    }


    class CardsViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtName;

        CardsViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.text);
        }

        void bind(T hasName, boolean isSelected) {
            itemView.setBackgroundColor(isSelected ? itemView.getContext().getResources().getColor(R.color.colorPrimaryLight,null): Color.WHITE);
            txtName.setText(hasName.getName());
        }

    }
}
