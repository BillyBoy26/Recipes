package com.example.benjamin.recettes.data;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String urlImage;
    private String description;
    private String name;
    private List<Ingredient> ingredients = new ArrayList<>();

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
}
