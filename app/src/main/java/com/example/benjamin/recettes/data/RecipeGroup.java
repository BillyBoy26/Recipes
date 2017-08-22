package com.example.benjamin.recettes.data;

import java.io.Serializable;
import java.util.List;

public class RecipeGroup implements Serializable, HasName {

    private Long id;
    private String name;
    private List<Recipe> recipes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
