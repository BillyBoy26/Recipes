package com.example.benjamin.recettes.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.importData.HttpImportHelper;
import com.example.benjamin.recettes.recipes.RecipesList;

import java.net.URL;
import java.util.List;

public class HttpRequestTask extends AsyncTask<URL,Void,List<Recipe>> {


    public static final String HTTP_REQUEST_TASK = "HTTP_REQUEST_TASK";
    private final Context context;

    public HttpRequestTask(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected List<Recipe> doInBackground(URL... urls) {
        URL url = urls[0];
        List<Recipe> recipes = HttpImportHelper.requestUrlAndCreateRecipes(url,context);
        Log.i(HTTP_REQUEST_TASK, recipes.size() + " recipes imported");
        return recipes;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        super.onPostExecute(recipes);
        Intent intent = new Intent(context, RecipesList.class);
        intent.putExtra(RecipesList.NB_RECIPES_IMPORTED, recipes.size());
        context.startActivity(intent);
    }
}
