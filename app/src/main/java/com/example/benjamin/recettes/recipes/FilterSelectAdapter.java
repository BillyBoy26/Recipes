package com.example.benjamin.recettes.recipes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.HasName;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.BasicListAdapter;

import java.util.HashSet;
import java.util.Set;

public class FilterSelectAdapter<T extends HasName> extends BasicListAdapter<T,FilterSelectAdapter.FilterSelectViewHolder> {

    private Set<HasName> selectedElements = new HashSet<>();

    public FilterSelectAdapter() {
        super();
    }

    @Override
    public FilterSelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_filter_item, parent, false);
        return new FilterSelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilterSelectAdapter.FilterSelectViewHolder holder, T data, int position) {
        holder.bind(data, selectedElements.contains(data));
    }


    public Set<HasName> getSelectedElements() {
        return selectedElements;
    }

    public void setSelectedElements(Set<HasName> selectedElements) {
        this.selectedElements = selectedElements;
        if (this.selectedElements == null) {
            this.selectedElements = new HashSet<>();
        }
        notifyDataSetChanged();
    }

    class FilterSelectViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox cbElement;

        FilterSelectViewHolder(View itemView) {
            super(itemView);
            cbElement = (CheckBox) itemView.findViewById(R.id.cbCategory);
            cbElement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedElements.add(datas.get(getLayoutPosition()));
                    } else {
                        selectedElements.remove(datas.get(getLayoutPosition()));
                    }
                }
            });
        }

        void bind(HasName category, boolean selected) {
            cbElement.setText(SUtils.capitalize(category.getName()));
            cbElement.setChecked(selected);
        }
    }
}
