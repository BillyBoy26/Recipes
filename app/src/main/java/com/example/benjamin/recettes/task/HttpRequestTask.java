package com.example.benjamin.recettes.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.benjamin.recettes.RecipeCreate;
import com.example.benjamin.recettes.data.Recipe;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequestTask extends AsyncTask<String,Void,Recipe> {


    private final Context context;

    public HttpRequestTask(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected Recipe doInBackground(String... params) {
        String urlStr = params[0];

        InputStream inputStream;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                inputStream.close();
                String html = builder.toString();

                Document document = Jsoup.parse(html);
                Element h1Element = document.body().select("h1.buzz-title").first();
                String name = h1Element.text();

                Element imageBalise = document.body().select("img.subbuzz__media-image").first();
                String urlImage = imageBalise.attr("data-src");

                Recipe recipe = new Recipe();
                recipe.setName(name);
                recipe.setUrlImage(urlImage);

                return recipe;

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Recipe recipe) {
        super.onPostExecute(recipe);
        context.startActivity(new Intent(context,RecipeCreate.class).putExtra(RecipeCreate.CURRENT_RECIPE,recipe));
    }
}
