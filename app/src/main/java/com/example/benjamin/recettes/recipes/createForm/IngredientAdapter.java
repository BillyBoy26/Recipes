package com.example.benjamin.recettes.recipes.createForm;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.BasicListAdapter;

import java.text.NumberFormat;

public class IngredientAdapter extends BasicListAdapter<Ingredient,IngredientAdapter.IngredientViewHolder> {

    private FragmentIngredients.OnIngredientListEditedListener listener;


    public IngredientAdapter() {
        this(null);
    }

    public IngredientAdapter(FragmentIngredients.OnIngredientListEditedListener listener) {
        this.listener = listener;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item, parent,false);
        final IngredientViewHolder viewHolder = new IngredientViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ingredient ingRemoved = removeItem(viewHolder.getAdapterPosition());
                if (listener != null) {
                    listener.onIngredientClicked(ingRemoved);
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, Ingredient ingredient, int position) {
        holder.bind(ingredient);
    }


    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);

        }

        public void bind(Ingredient ingredient) {
            String ingrName = SUtils.capitalize(ingredient.getName());
            String ingrQuantity = "";
            if (ingredient.getQuantity() != null && ingredient.getQuantity() > 0) {
                ingrQuantity = NumberFormat.getInstance().format((ingredient.getQuantity()));
                if (SUtils.notNullOrEmpty(ingredient.getQuantityUnit())) {
                    ingrQuantity += " " + ingredient.getQuantityUnit();
                }
                ingrQuantity += " ";
            }
            Spannable spannable = new SpannableString(ingrQuantity + ingrName);
            if (SUtils.notNullOrEmpty(ingrQuantity)) {
                spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#9CCC65")),0,ingrQuantity.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textViewName.setText(spannable);
        }


    }

    @Override
    public boolean addItem(Ingredient data) {
        if (!datas.contains(data)) {
            datas.add(data);
            notifyItemInserted(datas.size() - 1);
            return true;
        }
        int position = datas.indexOf(data);
        Ingredient firstIngredient = datas.get(position);
        if (firstIngredient.mergeIngredient(data)) {
            notifyItemChanged(position);
            return true;
        }
        return false;
    }
}
