package com.example.benjamin.recettes.recipes.createForm;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;
import com.example.benjamin.recettes.views.SimpleItemDividerDecoration;

import java.util.ArrayList;
import java.util.List;

public class FragmentIngredients extends Fragment implements RecyclerViewClickListener{


    private OnIngredientListEditedListener listenerActivity;
    private EditText txtAddIngr;
    private EditText txtQteDialog;
    private EditText txtQteUnitDialog;
    private AlertDialog dialogIngredient;
    private Ingredient currentIngredient;

    public interface OnIngredientListEditedListener {
        void onIngredientCreated(Ingredient ingredient);
        void onIngredientModified(Ingredient ingredient);
        void onIngredientDeleted(Ingredient ingredient);
    }

    private List<Ingredient> ingredients = new ArrayList<>();
    private IngredientAdapter adapter;
    private Recipe recipe;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.ingredients_list, container, false);
        final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerIngredient);
        fillView();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new SimpleItemDividerDecoration(getContext()));

        txtAddIngr = (EditText) layout.findViewById(R.id.txtIngredient);
        ImageView btnAddIng = (ImageView) layout.findViewById(R.id.btnAddIngredient);
        btnAddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SUtils.nullOrEmpty(txtAddIngr.getText().toString())) {
                    return;
                }
                if (!dialogIngredient.isShowing()) {
                    dialogIngredient.show();
                }
                fillDialogView();
            }
        });

        View dialogView = inflater.inflate(R.layout.ingredient_dialog_quantity, null);
        txtQteDialog = (EditText) dialogView.findViewById(R.id.editTextQte);
        txtQteUnitDialog = (EditText) dialogView.findViewById(R.id.editTextUnit);

        dialogIngredient = createDialogBox(dialogView);

        return layout;
    }

    private void fillView() {
        if (adapter == null) {
            adapter = new IngredientAdapter(this);
        }
        adapter.setDatas(ingredients);
    }


    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        this.ingredients = recipe != null ? recipe.getIngredients() : null;
        fillView();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        fillView();
    }

    public void getRecipe() {
        recipe.setIngredients(ingredients);
    }


    @Override
    public void onItemClick(View view, int position) {
        currentIngredient = adapter.getItem(position);
        if (!dialogIngredient.isShowing()) {
            dialogIngredient.show();
        }
        fillDialogView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIngredientListEditedListener) {
            listenerActivity = (OnIngredientListEditedListener) context;
        } else {
            listenerActivity = null;
        }
    }


    public AlertDialog createDialogBox(View dialogView) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setTitle(R.string.enterQuantity);
        DialogInterface.OnClickListener clicklistener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createOrUpdateIngredient();
                txtQteDialog.setText("");
                txtQteUnitDialog.setText("");
                txtAddIngr.setText("");
            }
        };
        builder.setPositiveButton(R.string.ok, clicklistener);
        builder.setNegativeButton(R.string.later, clicklistener);
        builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteIngredient();
            }
        });
        return builder.create();
    }

    private void deleteIngredient() {
        if (currentIngredient != null) {
            adapter.removeItem(ingredients.indexOf(currentIngredient));
            if (listenerActivity != null) {
                listenerActivity.onIngredientDeleted(currentIngredient);
            }
        }
    }

    private void fillDialogView() {
        if (currentIngredient != null) {
            if (currentIngredient.getQuantity() != null) {
                txtQteDialog.setText(String.valueOf(currentIngredient.getQuantity()));
            }
            txtQteUnitDialog.setText(currentIngredient.getQuantityUnit());
            dialogIngredient.getButton(DialogInterface.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);
        } else {
            txtQteDialog.setText("");
            txtQteUnitDialog.setText("");
            dialogIngredient.getButton(DialogInterface.BUTTON_NEUTRAL).setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    private void createOrUpdateIngredient() {
        String quant = txtQteDialog.getText().toString();
        float quantity = 1;
        if (SUtils.notNullOrEmpty(quant)) {
            try {
                quantity = Float.valueOf(quant);
            } catch (NumberFormatException e) {
                Log.e("INGR_QUANTITY", e.getMessage());
                e.printStackTrace();
            }
        }
        boolean create = currentIngredient == null;
        if (create) {
            currentIngredient = new Ingredient(txtAddIngr.getText().toString());
            ingredients.add(currentIngredient);
        }
        currentIngredient.setQuantity(quantity);
        currentIngredient.setQuantityUnit(txtQteUnitDialog.getText().toString());
        adapter.setDatas(ingredients);
        if (listenerActivity != null) {
            if (create) {
                listenerActivity.onIngredientCreated(currentIngredient);
            } else {
                listenerActivity.onIngredientModified(currentIngredient);
            }
        }
        currentIngredient = null;
    }

}
