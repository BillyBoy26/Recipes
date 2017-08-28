package com.example.benjamin.recettes.parser;

import android.support.annotation.NonNull;

import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.Step;
import com.example.benjamin.recettes.utils.SUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuzzFeedParser {


    public static List<Recipe> parse(String html) {
        Document document = Jsoup.parse(html);

        List<Recipe> recipes = new ArrayList<>();

        String urlVideo = parseUrlVideo(document);
        Elements ingredientsElem = document.body().select("p:matches((?i)Ingredients$)");
        if (!ingredientsElem.isEmpty()) {
            //article with multiple article and p formating

            for (Element currentElem : ingredientsElem) {
                Recipe recipe = new Recipe();

                //ingredients
                Element ingr = currentElem.nextElementSibling();
                ingr = proceedIngredientElement(recipe,ingr,true);

                //steps
                if (ingr != null) {
                    ingr.text(ingr.text().replace("PREPARATION", ""));
                    extractSteps(recipe, ingr);
                }

                do {
                    if (currentElem.previousElementSibling() != null) {
                        currentElem = currentElem.previousElementSibling();
                    } else {
                        currentElem = currentElem.parent();
                    }
                    if (currentElem != null) {
                        Elements img = currentElem.select("img[data-src]");
                        if (!img.isEmpty()) {
                            recipe.setUrlImage(img.first().attr("data-src"));
                        }
                    }
                } while (currentElem != null && !currentElem.is("h3"));

                if (currentElem != null) {
                    recipe.setName(currentElem.text());
                }
                recipe.setUrlVideo(urlVideo);
                recipes.add(recipe);


            }
        } else {
            //article with one recipe and h3 formating
            Recipe recipe = new Recipe();

            parseName(document, recipe);
            recipe.setUrlVideo(urlVideo);
            parseImage(document, recipe);
            parseIngredients(document, recipe);
            parseSteps(document, recipe);
            recipes.add(recipe);
        }



        return recipes;
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

        extractSteps(recipe, h3Steps.nextElementSibling());
    }

    private static void extractSteps(Recipe recipe, Element firstStepElement) {
        if (firstStepElement != null) {
            int rank = 1;
            Element step = firstStepElement;
            while (step != null && !step.is("div")) {
                if (step.is("script")) {
                    step = step.nextElementSibling();
                    continue;
                }
                //clear "1." ou "10."
                String text = step.text();
                if (SUtils.notNullOrEmpty(text)) {
                    while (!text.isEmpty() && (text.charAt(0) == '.' || Character.isDigit(text.charAt(0)))) {
                        text = text.substring(1);
                    }

                }
                recipe.getSteps().add(new Step(text.trim(),rank++));
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

        proceedIngredientElement(recipe, h3Ingredients,false);
    }

    private static Element proceedIngredientElement(Recipe recipe, Element h3Ingredients,boolean fromMultipleRecipes) {
        if (h3Ingredients == null) {
            return null;
        }
        Element ingr = h3Ingredients.nextElementSibling();
        while (ingr != null && (fromMultipleRecipes ? !ingr.text().contains("PREPARATION") : !ingr.is("div"))) {
            if (ingr.is("script")) {
                ingr = ingr.nextElementSibling();
                continue;
            }
            String text = ingr.text();
            if (text.contains("Serves")) {
                text = text.replace("Serves ", "");
                recipe.setNbCovers(text.trim());
                ingr = ingr.nextElementSibling();
                continue;
            }

            Element elemInIngr = ingr.children().first();
            if (elemInIngr != null && elemInIngr.is("b")) {
                //subSession
                ingr = ingr.nextElementSibling();
                continue;
            }
            Ingredient ingrObject = extractIngredient(text);
            recipe.getIngredients().add(ingrObject);
            ingr = ingr.nextElementSibling();
        }
        return ingr;
    }

    @NonNull
    private static Ingredient extractIngredient(String text) {
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
        return new Ingredient(nameIngr, -1, quantity);
    }

    private static String parseUrlVideo(Document document) {
        Element aElement = document.body().select("a.subbuzz-youtube__thumb").first();
        if (aElement != null) {
            return aElement.attr("href");
        }
        return "";
    }

}
