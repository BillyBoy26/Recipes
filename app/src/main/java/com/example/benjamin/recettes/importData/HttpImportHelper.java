package com.example.benjamin.recettes.importData;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.db.dao.RecipeDao;
import com.example.benjamin.recettes.parser.BuzzFeedParser;
import com.example.benjamin.recettes.utils.CollectionUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class HttpImportHelper {

    public static final List<String> HOST_ALLOWED = Arrays.asList("buzzfeed.com");
    public static final String HTTP_IMPORT_HELPER = "HTTP_IMPORT_HELPER";

    public static String getDomainName(String host) {
        return host.startsWith("www.") ? host.substring(4) : host;
    }

    @Nullable
    public static List<Recipe> requestUrlAndCreateRecipes(URL url, Context context) {
        InputStream inputStream;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            //redirect error code
            if (responseCode != HttpURLConnection.HTTP_OK) {
                if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                        || responseCode == HttpURLConnection.HTTP_MOVED_PERM
                        || responseCode == HttpURLConnection.HTTP_SEE_OTHER){
                    URL newUrl = new URL(connection.getHeaderField("Location"));
                    connection = (HttpURLConnection) newUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    responseCode = connection.getResponseCode();
                }
            }
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
                List<Recipe> recipes = new BuzzFeedParser(html,url).parse();

                if (CollectionUtils.notNullOrEmpty(recipes)) {
                    RecipeDao recipeDao = new RecipeDao(context);
                    recipeDao.open();
                    for (Recipe recipe : recipes) {
                        try {
                            recipeDao.createOrUpdate(recipe);
                        }catch (Exception e) {
                            Log.e(HTTP_IMPORT_HELPER, "Error while creating recipe (" + recipe.getName() + ")" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    recipeDao.close();

                }
                return recipes;
            }

        } catch (Exception e) {
            Log.e(HTTP_IMPORT_HELPER, "Error while importing recipe " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
