package com.example.benjamin.recettes.createForm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.utils.SUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentIngredients extends Fragment implements RecipeCreate.RecipeFiller{



    private List<Ingredient> ingredients = new ArrayList<>();
    private IngredientAdapter adapter;
    private Recipe recipe;
    private String currentIngName;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.recipe_create_ingredients, container, false);
        final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerIngredient);
        adapter = new IngredientAdapter(ingredients);
        recyclerView.setAdapter(adapter);


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        final SearchView searchView = (SearchView) layout.findViewById(R.id.searchView);

//        searchView.setSuggestionsAdapter(new CursorAdapter(getActivity(),cursor,0) {
//            @Override
//            public View newView(Context context, Cursor cursor, ViewGroup parent) {
//                return getActivity().getLayoutInflater().inflate(R.layout.ingredient_card,container,false);
//            }
//
//            @Override
//            public void bindView(View view, Context context, Cursor cursor) {
//                if(cursor.getString(0) !=null){
//                    TextView name = (TextView) view.findViewById(R.id.textView);
//                    name.setText(cursor.getString(0));
//                }
//        });
//            }
        final AlertDialog dialogQte = createDialogBox(searchView,inflater.inflate(R.layout.ingredient_dialog_quantity, null));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                currentIngName = query;
                if (query.isEmpty()) {
                    return false;
                }
                if (!dialogQte.isShowing()) {
                    dialogQte.show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return layout;
    }

    public AlertDialog createDialogBox(SearchView searchView, View dialogView) {

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
                adapter.addIngredient(ingredient);
                editTextQte.setText("");
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.notNow, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.addIngredient(new Ingredient(currentIngName));
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        this.ingredients = recipe.getIngredients();
    }

    @Override
    public void getRecipe() {
        recipe.setIngredients(ingredients);
    }
}
