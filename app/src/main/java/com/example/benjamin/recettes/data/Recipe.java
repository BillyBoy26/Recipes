package com.example.benjamin.recettes.data;

import com.example.benjamin.recettes.utils.SUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable{
    private String urlImage;
    private String description;
    private String name;
    private List<Ingredient> ingredients = new ArrayList<>();
    private Long id;

    public Recipe() {
    }

    public Recipe(String name, String urlImage) {
        this.name = name;
        this.urlImage = urlImage;
    }


    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getIngredientsAsString() {
        StringBuilder builder = new StringBuilder();

        if (ingredients != null) {
            for (Ingredient ingredient : ingredients) {
                builder.append(ingredient.toString());
            }
        }
        return builder.toString();
    }

    public void setIngredients(String ingredientsAsStr) {
        ingredients = new ArrayList<>();
        if (SUtils.notNullOrEmpty(ingredientsAsStr)) {
            String[] split = ingredientsAsStr.split(Ingredient.SEP_END);
            for (String ingre : split) {
                String[] attr = ingre.split(Ingredient.SEP_ATTRIBUTE);
                Ingredient ingr = new Ingredient(attr[0], Integer.parseInt(attr[1]), Integer.parseInt(attr[2]));
                if (attr.length > 3) {
                    ingr.setQuantityUnit(attr[3]);
                }
                ingredients.add(ingr);
            }
        }
    }
}
