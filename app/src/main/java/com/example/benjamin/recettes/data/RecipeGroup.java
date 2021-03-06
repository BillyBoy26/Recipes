package com.example.benjamin.recettes.data;

import java.io.Serializable;
import java.util.List;

public class RecipeGroup implements Serializable, HasName,HasSteps {

    private Long id;
    private String name;
    private List<Recipe> recipes;
    private List<Step> steps;
    private boolean batchCooking;

    public boolean isBatchCooking() {
        return batchCooking;
    }

    public void setBatchCooking(boolean batchCooking) {
        this.batchCooking = batchCooking;
    }

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

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
