package com.example.benjamin.recettes.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.HasName;

public class NameAdapter<T> extends BasicListAdapter<T,NameAdapter.NameViewHolder> {

    private final boolean withPosition;

    public NameAdapter() {
        this(false);
    }
    public NameAdapter(boolean withPosition) {
        this.withPosition = withPosition;
    }

    @Override
    public NameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_text, parent, false);
        return new NameViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(NameAdapter.NameViewHolder holder, T data, int position) {
        if (data instanceof HasName) {
            holder.bind((HasName) data,position + 1);
        } else if (data instanceof String) {
            holder.bind((String) data,position + 1);
        }
    }

    public class NameViewHolder extends RecyclerView.ViewHolder{

        private final TextView txtName;

        public NameViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.text);
        }

        public void bind(HasName hasName, int position) {
            bind(hasName.getName(),position);
        }

        public void bind(String name, int position) {
            String text = "";
            if (withPosition) {
                text += String.valueOf(position) + ". ";
            }
            text += name;
            txtName.setText(text);
        }
    }

}
