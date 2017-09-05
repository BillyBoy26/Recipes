package com.example.benjamin.recettes.recipes.createForm;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.utils.SUtils;

public class IngredientWidgetBuilder {

    private final AlertDialog alertDialog;
    private final IngredientAdapter adapter;
    private String currentIngName;
    private FragmentIngredients.OnIngredientListEditedListener listener;

    public IngredientWidgetBuilder(SearchView searchView, View dialogView,IngredientAdapter adapter) {
        this(searchView, dialogView, adapter, null);
    }

    public IngredientWidgetBuilder(SearchView searchView, View dialogView, IngredientAdapter adapter, FragmentIngredients.OnIngredientListEditedListener listener) {
        alertDialog = createDialogBox(searchView, dialogView);
        this.adapter = adapter;
        this.listener = listener;
    }

    public SearchView.OnQueryTextListener createQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                currentIngName = query;
                if (query.isEmpty()) {
                    return false;
                }
                if (!alertDialog.isShowing()) {
                    alertDialog.show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
    }

    public AlertDialog createDialogBox(final SearchView searchView, View dialogView) {

        final EditText editTextQte = (EditText) dialogView.findViewById(R.id.editTextQte);
        final EditText editTextUnit = (EditText) dialogView.findViewById(R.id.editTextUnit);

        final AlertDialog.Builder builder = new AlertDialog.Builder(searchView.getContext());

        builder.setView(dialogView);
        builder.setTitle(R.string.enterQuantity);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String quant = editTextQte.getText().toString();
                float quantity = 1;
                if (SUtils.notNullOrEmpty(quant)) {
                    try {
                        quantity = Float.valueOf(quant);
                    } catch (NumberFormatException e) {
                        Log.e("INGR_QUANTITY", e.getMessage());
                        e.printStackTrace();
                    }
                }
                Ingredient ingredient = new Ingredient(currentIngName, -1, quantity);
                ingredient.setQuantityUnit(editTextUnit.getText().toString());
                adapter.addItem(ingredient);
                editTextQte.setText("");
                searchView.setQuery("",false);
                dialog.cancel();
                if (listener != null) {
                    listener.onIngredientSelected(ingredient);
                }
            }
        });
        builder.setNegativeButton(R.string.notNow, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Ingredient ingredient = new Ingredient(currentIngName);
                adapter.addItem(ingredient);
                dialog.cancel();
                if (listener != null) {
                    listener.onIngredientSelected(ingredient);
                }
            }
        });
        return builder.create();
    }
}
