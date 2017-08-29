package com.example.benjamin.recettes.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.importData.HttpImportHelper;
import com.example.benjamin.recettes.recipes.RecipesList;

import java.net.URL;
import java.util.List;

public class HttpRequestTask extends AsyncTask<URL,Void,List<Recipe>> {


    private final Context context;

    public HttpRequestTask(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected List<Recipe> doInBackground(URL... urls) {
        URL url = urls[0];
        List<Recipe> recipes = HttpImportHelper.requestUrlAndCreateRecipes(url,context);
        return recipes;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        super.onPostExecute(recipes);
        context.startActivity(new Intent(context,RecipesList.class));
    }
}
