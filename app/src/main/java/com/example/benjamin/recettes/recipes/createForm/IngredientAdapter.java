package com.example.benjamin.recettes.recipes.createForm;

import android.graphics.Color;
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
import com.example.benjamin.recettes.views.ClickableViewHolder;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;

import java.text.NumberFormat;

public class IngredientAdapter extends BasicListAdapter<Ingredient,IngredientAdapter.IngredientViewHolder> {

    private RecyclerViewClickListener listener;

    public IngredientAdapter(RecyclerViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutText = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item, parent,false);
        return new IngredientViewHolder(layoutText);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, Ingredient ingredient, int position) {
        holder.bind(ingredient,position);
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


    class IngredientViewHolder extends ClickableViewHolder {

        private final TextView textViewName;

        IngredientViewHolder(View itemView) {
            super(itemView,listener);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);

        }

        void bind(Ingredient ingredient,int position) {
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

            if (position % 2 == 1) {
                itemView.setBackgroundResource(R.color.listColorSecondary);
            } else {
                itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }


    }
}
