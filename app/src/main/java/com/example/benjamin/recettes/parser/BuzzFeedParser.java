package com.example.benjamin.recettes.parser;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.benjamin.recettes.data.Ingredient;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.Step;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.SUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuzzFeedParser {


    public  final String BF_PARSER = "BF_PARSER";
    private final Document document;
    private final URL url;

    public BuzzFeedParser(String html, URL url) {
        document = Jsoup.parse(html);
        this.url = url;
    }

    public List<Recipe> parse() {

        List<Recipe> recipes = new ArrayList<>();

        String urlVideo = parseUrlVideo(document);
        String mainUrlImage = parseImage(document);
        //select element with p or h3 with the exact text ingredients
        Elements ingredientsElem = document.body().select(
                "p:matches((?i)Ingredients$)" +
                ",h3:matches((?i)Ingredients$)" +
                ",h3:matches((?i)what you will need)");
        if (ingredientsElem.size() > 1) {
            //article with multiple article
            parseMultipleRecipe(recipes, ingredientsElem, urlVideo, mainUrlImage);
        } else {
            //article with one recipe and h3 formating
            Recipe recipe = new Recipe();

            parseName(document, recipe);
            recipe.setUrlVideo(urlVideo);
            recipe.setUrlImage(mainUrlImage);
            parseIngredients(document, recipe);
            parseSteps(document, recipe);
            addRecipe(recipes, recipe);
        }



        return recipes;
    }

    private  void parseMultipleRecipe(List<Recipe> recipes, Elements ingredientsElem, String urlVideo, String mainUrlImage) {
        for (Element currentElem : ingredientsElem) {
            Recipe recipe = new Recipe();

            //ingredients
            Element ingrElement = currentElem;
            Element stepElement = proceedIngredientElement(recipe,ingrElement);

            //searching img element and name element
            do {
                if (currentElem.previousElementSibling() != null) {
                    currentElem = currentElem.previousElementSibling();
                } else {
                    currentElem = currentElem.parent();
                }
                if (currentElem != null) {
                    Elements servingElem = currentElem.select("*:containsOwn(Serving)");
                    if (!servingElem.isEmpty()) {
                        parseNbCovers(recipe,servingElem.first().text());
                    }
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
            if (SUtils.nullOrEmpty(recipe.getUrlImage())) {
                recipe.setUrlImage(mainUrlImage);
            }

            //steps
            //searching stepElement
            if (stepElement == null || stepElement.select("*:matches((?i)PREPARATION)").isEmpty()) {
                stepElement = ingrElement.parent().nextElementSibling();
                Elements select = stepElement.select("h3:matches((?i)PREPARATION),h3:matches((?i)Directions)");
                if (select.isEmpty()) {
                    Log.w(BF_PARSER, "No steps found for the recipe");
                } else {
                    stepElement = select.first();
                }
            }
            if (stepElement != null) {
                stepElement.text(stepElement.text().replace("PREPARATION", ""));
                extractSteps(recipe, stepElement);
            }
            addRecipe(recipes, recipe);
        }
    }

    private  void addRecipe(List<Recipe> recipes, Recipe recipe) {
        recipes.add(recipe);
        Log.i(BF_PARSER, "recipe added from " + url.toString());
        if (SUtils.nullOrEmpty(recipe.getName())) {
            Log.w(BF_PARSER, "No name for the recipe from" + url.toString());
            recipe.setName("");
        }
        if (SUtils.nullOrEmpty(recipe.getUrlImage())) {
            Log.w(BF_PARSER, "No image for the recipe " + recipe.getName() + " from " + url.toString());
        }
        if (SUtils.nullOrEmpty(recipe.getUrlVideo())) {
            Log.w(BF_PARSER, "No video for the recipe " + recipe.getName() + " from " + url.toString());
        }
        if (CollectionUtils.nullOrEmpty(recipe.getIngredients())) {
            Log.w(BF_PARSER, "No ingredients for the recipe " + recipe.getName() + " from " + url.toString());
        }
        if (CollectionUtils.nullOrEmpty(recipe.getSteps())) {
            Log.w(BF_PARSER, "No steps for the recipe " + recipe.getName() + " from " + url.toString());
        }
    }

    private  void parseSteps(Document document, Recipe recipe) {
        Elements h3s = document.body().select("h3.subbuzz__title");
        Element h3Steps = null;
        for (Element h3 : h3s) {
            if (h3.children().is("span")) {
                for (Element el : h3.children()) {
                    String text = el.text().trim();
                    if (text.equalsIgnoreCase("PREPARATION") || text.contains("Directions") || text.contains("Here's what you'll do") || text.contains("Here's what you will do")) {
                        h3Steps = h3;
                        break;
                    }
                }
            }
        }
        if (h3Steps != null) {
            extractSteps(recipe, h3Steps.nextElementSibling());
        }
    }

    private  void extractSteps(Recipe recipe, Element firstStepElement) {
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

    private  String parseImage(Document document) {
        Element imageBalise = document.body().select("img.subbuzz__media-image").first();
        String urlImage = imageBalise.attr("data-src");
        return urlImage;

    }

    private  void parseName(Document document, Recipe recipe) {
        Element h1Element = document.body().select("h1.buzz-title").first();
        String name = h1Element.text();
        recipe.setName(name);
    }

    private  void parseIngredients(Document document, Recipe recipe) {
        Elements h3s = document.body().select("h3.subbuzz__title");
        Element h3Ingredients = null;
        for (Element h3 : h3s) {
            if (h3.children().is("span")) {
                for (Element el : h3.children()) {
                    String text= el.text().trim();
                    if (text.equalsIgnoreCase("INGREDIENTS") || text.contains("Here's what you will need") || text.contains("Here's what you'll need")) {
                        h3Ingredients = h3;
                        break;
                    }
                }
            }
        }

        proceedIngredientElement(recipe, h3Ingredients);
    }

    private  Element proceedIngredientElement(Recipe recipe, Element ingElementTitle) {
        if (ingElementTitle == null) {
            return null;
        }
        Element ingr = ingElementTitle.nextElementSibling();
        while (ingr != null && !ingr.text().contains("PREPARATION") && !ingr.is("div")) {
            if (ingr.is("script")) {
                ingr = ingr.nextElementSibling();
                continue;
            }
            String text = ingr.text();
            if (parseNbCovers(recipe, text)) {
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
            recipe.addIngredient(ingrObject);
            ingr = ingr.nextElementSibling();
        }
        return ingr;
    }

    private  boolean parseNbCovers(Recipe recipe, String text) {
        String textLowerCaser = text.toLowerCase();
        if (textLowerCaser.contains("serves") || textLowerCaser.contains("serving")) {
            textLowerCaser = textLowerCaser.replace("serves ", "");
            textLowerCaser = textLowerCaser.replace("servings", "");
            textLowerCaser = textLowerCaser.replace("serving", "");
            textLowerCaser = textLowerCaser.replace("yields", "");
            textLowerCaser = textLowerCaser.replace(":", "");
            recipe.setNbCovers(textLowerCaser.trim());
            return true;
        }
        return false;
    }

    @NonNull
    private  Ingredient extractIngredient(String text) {

        text = replaceSpecialNumberChar(text, "½", 0.5);
        text = replaceSpecialNumberChar(text, "¼", 0.25);
        text = replaceSpecialNumberChar(text, "¾", 0.75);
        text = replaceSpecialNumberChar(text, "⅓", 0.3);
        text = replaceSpecialNumberChar(text, "⅔", 0.6);
        text = text.replace("*", "");
        text = text.replace("#", "");

        float quantity = -1;
        //get decimal number
        Pattern regex = Pattern.compile("(\\d+(?:\\.\\d+)?)");
        Matcher matcher = regex.matcher(text);
        if (matcher.find()) {
            String qteStr = matcher.group();
            quantity = Float.valueOf(qteStr);
            text = text.replace(qteStr, "");
        }
        String nameIngr = text.trim();
        return new Ingredient(nameIngr, -1, quantity);
    }

    private  String replaceSpecialNumberChar(String text, String specialChar, double specialCharValue) {
        if (text.contains(specialChar)) {
            int index = text.indexOf(specialChar);
            double number = 0;
            if (index > 0) {
                if (Character.isDigit(text.charAt(index - 1))) {
                    try {
                        number += Double.parseDouble(text.substring(index-1,index))  ;
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Log.w(BF_PARSER, "NumberFormatException happend while extracting ingredient");
                    }
                }
            }
            number += specialCharValue;
            text = text.replaceAll("[1-9]?" + specialChar, Double.toString(number));
        }
        return text;
    }

    private  String parseUrlVideo(Document document) {
        Element aElement = document.body().select("a.subbuzz-youtube__thumb").first();
        if (aElement != null) {
            return aElement.attr("href");
        }
        Element videoElement = document.body().select("figure a[href*=https://www.facebook.com/video.php]").first();
        if (videoElement != null) {
            return videoElement.attr("href");
        }
        return "";
    }

}
