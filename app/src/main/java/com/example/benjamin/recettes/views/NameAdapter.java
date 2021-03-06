package com.example.benjamin.recettes.views;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.HasName;
import com.example.benjamin.recettes.utils.SUtils;

import java.util.ArrayList;
import java.util.List;

public class NameAdapter<T> extends BasicListAdapter<T,NameAdapter.NameViewHolder> {

    private final boolean withPosition;
    private RecyclerViewClickListener clickListener = null;
    private RecyclerViewLongClickListener longClickListener = null;
    private boolean canSelect;
    private List<Integer> positionsSelected = null;
    private boolean isInSelectMode = false;

    public NameAdapter() {
        this(false);
    }
    public NameAdapter(boolean withPosition) {
        this(withPosition, null);
    }

    public NameAdapter(boolean withPosition, RecyclerViewClickListener clickListener) {
        this(withPosition, clickListener, null,false);
    }

    public NameAdapter(boolean withPosition, RecyclerViewClickListener clickListener,RecyclerViewLongClickListener longClickListener,boolean canSelect) {
        this.withPosition = withPosition;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.canSelect = canSelect;
        if (canSelect) {
            positionsSelected = new ArrayList<>();
        }
    }

    @Override
    public NameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item, parent, false);
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

    class NameViewHolder extends ClickableViewHolder{

        private final TextView txtName;


        NameViewHolder(View itemView) {
            super(itemView,clickListener,longClickListener);
            txtName = (TextView) itemView.findViewById(R.id.textViewName);

        }

        void bind(HasName hasName, int position) {
            bind(hasName.getName(),position);
        }

        void bind(String name, int position) {
            String text = "";
            if (withPosition) {
                text += String.valueOf(position) + ". ";
            }
            text += SUtils.capitalize(name);
            txtName.setText(text);

            if (canSelect && positionsSelected.contains(position - 1)) {
                itemView.setBackgroundColor(Color.parseColor("#F8BBD0"));
            }else if (position % 2 == 1) {
                itemView.setBackgroundResource(R.color.listColorSecondary);
            } else {
                itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }

        @Override
        public void onClick(View v) {
            if (canSelect && isInSelectMode) {
                proceedSelection();
            } else {
                super.onClick(v);
            }
        }

        private void proceedSelection() {
            if (positionsSelected.contains(getLayoutPosition())) {
                positionsSelected.remove(Integer.valueOf(getLayoutPosition()));
            } else {
                positionsSelected.add(getLayoutPosition());
            }
            if (positionsSelected.isEmpty()) {
                isInSelectMode = false;
            }
            notifyItemChanged(getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            boolean cbConsumed = super.onLongClick(v);
            if (canSelect) {
                proceedSelection();
                isInSelectMode = true;
                cbConsumed = true;
            }
            return cbConsumed;
        }
    }

}
