package com.example.benjamin.recettes.parser;

import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.Step;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuzzFeedParser {


    public static Recipe parse(String html) {
        Document document = Jsoup.parse(html);
        Recipe recipe = new Recipe();

        parseName(document, recipe);
        parseImage(document, recipe);
        parseIngredients(document, recipe);
        parseSteps(document, recipe);

        return recipe;
    }

    private static void parseSteps(Document document, Recipe recipe) {
        Elements h3s = document.body().select("h3.subbuzz__title");
        Element h3Steps = null;
        for (Element h3 : h3s) {
            if (h3.children().is("span")) {
                for (Element el : h3.children()) {
                    if (el.text().trim().equalsIgnoreCase("PREPARATION")) {
                        h3Steps = h3;
                        break;
                    }
                }
            }
        }

        if (h3Steps != null) {
            int rank = 1;
            Element step = h3Steps.nextElementSibling();
            while (step != null && step.is("p")) {
                String text = step.text();
                //get decimal number
                recipe.getSteps().add(new Step(text,rank++));
                step = step.nextElementSibling();
            }
        }
    }

    private static void parseImage(Document document, Recipe recipe) {
        Element imageBalise = document.body().select("img.subbuzz__media-image").first();
        String urlImage = imageBalise.attr("data-src");
        recipe.setUrlImage(urlImage);
    }

    private static void parseName(Document document, Recipe recipe) {
        Element h1Element = document.body().select("h1.buzz-title").first();
        String name = h1Element.text();
        recipe.setName(name);
    }

    private static void parseIngredients(Document document, Recipe recipe) {
        Elements h3s = document.body().select("h3.subbuzz__title");
        Element h3Ingredients = null;
        for (Element h3 : h3s) {
            if (h3.children().is("span")) {
                for (Element el : h3.children()) {
                    if (el.text().trim().equalsIgnoreCase("INGREDIENTS")) {
                        h3Ingredients = h3;
                        break;
                    }
                }
            }
        }

        if (h3Ingredients != null) {
            Element ingr = h3Ingredients.nextElementSibling();
            while (ingr != null && ingr.is("p")) {
                String text = ingr.text();
                if (text.contains("Serves")) {
                    ingr = ingr.nextElementSibling();
                    continue;
                }
                text = text.replace("½", "0.5");
                text = text.replace("¼", "0.25");
                text = text.replace("¾", "0.75");
                text = text.replace("⅓", "0.3");
                text = text.replace("⅔", "0.6");

                float quantity = -1;
                //get decimal number
                Pattern regex = Pattern.compile("(\\d+(?:\\.\\d+)?)");
                Matcher matcher = regex.matcher(text);
                if (matcher.find()) {
                    String qteStr = matcher.group();
                    quantity = Float.valueOf(qteStr);
                    text = text.replace(qteStr, "");
                }
                String nameIngr = text;
                recipe.getIngredients().add(new Ingredient(nameIngr, -1, quantity));
                ingr = ingr.nextElementSibling();
            }
        }
    }

}
