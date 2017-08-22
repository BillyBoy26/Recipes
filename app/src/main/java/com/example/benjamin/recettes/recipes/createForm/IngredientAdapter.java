package com.example.benjamin.recettes.recipes.createForm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.utils.CommandWithParam;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.BasicListAdapter;

import java.text.NumberFormat;

public class IngredientAdapter extends BasicListAdapter<Ingredient,IngredientAdapter.IngredientViewHolder> {

    private CommandWithParam<Ingredient> deleteCommande;


    public IngredientAdapter() {
        this(null);
    }

    public IngredientAdapter(CommandWithParam<Ingredient> deleteCommande) {
        this.deleteCommande = deleteCommande;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_card, parent,false);
        final IngredientViewHolder viewHolder = new IngredientViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ingredient ingRemoved = removeItem(viewHolder.getAdapterPosition());
                if (deleteCommande != null) {
                    deleteCommande.execute(ingRemoved);
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
        private final TextView textViewQuantity;
        private final ImageView imageView;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewQuantity = (TextView) itemView.findViewById(R.id.textViewQuant);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }

        public void bind(Ingredient ingredient) {
            textViewName.setText(ingredient.getName());
            if (ingredient.getImage() > 0) {
//            imageView.setImageResource(R.drawable.ic_cake_white_24dp);
            }
            if (ingredient.getQuantity() != null && ingredient.getQuantity() > 0) {
                String quantity = NumberFormat.getInstance().format((ingredient.getQuantity()));
                if (SUtils.notNullOrEmpty(ingredient.getQuantityUnit())) {
                    quantity += " " + ingredient.getQuantityUnit();
                }
                textViewQuantity.setText(quantity);
            } else {
                textViewQuantity.setText("");
            }
        }


    }


}
