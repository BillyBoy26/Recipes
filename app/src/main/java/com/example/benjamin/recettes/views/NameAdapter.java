package com.example.benjamin.recettes.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.HasName;
import com.example.benjamin.recettes.utils.SUtils;

public class NameAdapter<T> extends BasicListAdapter<T,NameAdapter.NameViewHolder> {

    private final boolean withPosition;
    private RecyclerViewClickListener clickListener = null;

    public NameAdapter() {
        this(false);
    }
    public NameAdapter(boolean withPosition) {
        this(withPosition, null);
    }

    public NameAdapter(boolean withPosition, RecyclerViewClickListener clickListener) {
        this.withPosition = withPosition;
        this.clickListener = clickListener;
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

    public class NameViewHolder extends ClickableViewHolder{

        private final TextView txtName;

        public NameViewHolder(View itemView) {
            super(itemView,clickListener);
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
            text += SUtils.capitalize(name);
            txtName.setText(text);
        }
    }

}
