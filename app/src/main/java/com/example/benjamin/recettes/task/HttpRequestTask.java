package com.example.benjamin.recettes.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.dao.RecipeDao;
import com.example.benjamin.recettes.parser.BuzzFeedParser;
import com.example.benjamin.recettes.recipes.RecipesList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
        InputStream inputStream;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                inputStream.close();
                String html = builder.toString();
                List<Recipe> recipes = BuzzFeedParser.parse(html);
                for (Recipe recipe : recipes) {
                    RecipeDao recipeDao = new RecipeDao(context);
                    recipeDao.open();
                    recipeDao.createOrUpdate(recipe);
                    recipeDao.close();
                }

                return recipes;

            }

        } catch (IOException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        super.onPostExecute(recipes);
        context.startActivity(new Intent(context,RecipesList.class));
    }
}
