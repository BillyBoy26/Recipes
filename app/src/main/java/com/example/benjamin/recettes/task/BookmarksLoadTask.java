package com.example.benjamin.recettes.task;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.importData.HttpImportHelper;
import com.example.benjamin.recettes.recipes.RecipesList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookmarksLoadTask extends AsyncTask<Uri,Void,List<Recipe>> {

    private final Context context;

    public BookmarksLoadTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Recipe> doInBackground(Uri... params) {
        List<Recipe> recipes = new ArrayList<>();
        Uri uri = params[0];
        Document document = parseDocumentFromUri(uri);
        if (document != null) {
            Elements aElement = document.select("A[href]");
            if (!aElement.isEmpty()) {
                for (Element element : aElement) {
                    try {
                        URL url = new URL(element.attr("href"));
                        if (HttpImportHelper.HOST_ALLOWED.contains(HttpImportHelper.getDomainName(url.getHost()))) {
                                List<Recipe> importedRecipes = HttpImportHelper.requestUrlAndCreateRecipes(url, context);
                                if (importedRecipes != null) {
                                    recipes.addAll(recipes);
                                }
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return recipes;
    }

    private Document parseDocumentFromUri(Uri uri) {
        Document document = null;
        InputStream in = null;
        if (uri != null && uri.getPath() != null) {
            try {
                in = context.getContentResolver().openInputStream(uri);
                document = Jsoup.parse(in,"UTF-8",uri.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        closeStream(in);
        return document;
    }

    private void closeStream(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        super.onPostExecute(recipes);
        context.startActivity(new Intent(context,RecipesList.class));
    }
}
