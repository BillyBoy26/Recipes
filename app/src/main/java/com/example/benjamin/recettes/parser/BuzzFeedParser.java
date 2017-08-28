package com.example.benjamin.recettes.parser;

import android.support.annotation.NonNull;
import android.util.Log;

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
        //select element with p or h3 with the exact text ingredients
        Elements ingredientsElem = document.body().select("p:matches((?i)Ingredients$),h3:matches((?i)Ingredients$)");
        if (ingredientsElem.size() > 1) {
            //article with multiple article
            parseMultipleRecipe(recipes, urlVideo, ingredientsElem);
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

    private static void parseMultipleRecipe(List<Recipe> recipes, String urlVideo, Elements ingredientsElem) {
        for (Element currentElem : ingredientsElem) {
            Recipe recipe = new Recipe();

            //ingredients
            Element ingrElement = currentElem.nextElementSibling();
            Element stepElement = proceedIngredientElement(recipe,ingrElement);

            //steps
            //searching stepElement
            if (stepElement == null || stepElement.select("*:matches((?i)PREPARATION)").isEmpty()) {
                stepElement = ingrElement.parent().nextElementSibling();
                Elements select = stepElement.select("h3:matches((?i)PREPARATION)");
                if (select.isEmpty()) {
                    Log.w("BF_PARSER", "No steps found for the recipe");
                } else {
                    stepElement = select.first();
                }
            }
            if (stepElement != null) {
                stepElement.text(stepElement.text().replace("PREPARATION", ""));
                extractSteps(recipe, stepElement);
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
                        Elements h3 = currentElem.select("h3");
                        if (!h3.isEmpty()) {
                            currentElem = h3.first();
                        }
                    }
                }
            } while (currentElem != null && !currentElem.is("h3"));

            if (currentElem != null) {
                recipe.setName(currentElem.text());
            }
            recipe.setUrlVideo(urlVideo);
            recipes.add(recipe);


        }
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
                    recipe.getSteps().add(new Step(text.trim(),rank++));
                }

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

        proceedIngredientElement(recipe, h3Ingredients);
    }

    private static Element proceedIngredientElement(Recipe recipe, Element h3Ingredients) {
        if (h3Ingredients == null) {
            return null;
        }
        Element ingr = h3Ingredients.nextElementSibling();
        while (ingr != null && !ingr.text().contains("PREPARATION") && !ingr.is("div")) {
            if (ingr.is("script")) {
                ingr = ingr.nextElementSibling();
                continue;
            }
            String text = ingr.text();
            if (text.contains("Serves") || text.contains("Serving")) {
                text = text.replace("Serves ", "");
                text = text.replace("Servings", "");
                text = text.replace("Serving", "");
                text = text.replace(":", "");
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
        //TODO handle "1½"
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
