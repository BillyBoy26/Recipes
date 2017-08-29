package com.example.benjamin.recettes.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable, HasName{



    public enum RecipeFiller{
        WITH_STEPS, WITH_ING
    }

    private String urlImage;
    private String description;
    private String name;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Step> steps = new ArrayList<>();
    private Long id;
    private List<Category> categories = new ArrayList<>();
    private String prepareTime;
    private String cookTime;
    private String totalTime;
    private String nbCovers;
    private boolean batchCooking;
    private String urlVideo;
    private String urlImage2;
    private String urlImage3;
    private String urlImage4;
    private String urlImage5;


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

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }


    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getPrepareTime() {
        return prepareTime;
    }

    public void setPrepareTime(String prepareTime) {
        this.prepareTime = prepareTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getNbCovers() {
        return nbCovers;
    }

    public void setNbCovers(String nbCovers) {
        this.nbCovers = nbCovers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;
        if (id != null && recipe.id != null) {
            return id.equals(recipe.id);
        }
        if (name != null && recipe.name != null) {
            return name.equals(recipe.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    public boolean isBatchCooking() {
        return batchCooking;
    }

    public void setBatchCooking(boolean batchCooking) {
        this.batchCooking = batchCooking;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getUrlImage2() {
        return urlImage2;
    }

    public void setUrlImage2(String urlImage2) {
        this.urlImage2 = urlImage2;
    }

    public String getUrlImage3() {
        return urlImage3;
    }

    public void setUrlImage3(String urlImage3) {
        this.urlImage3 = urlImage3;
    }

    public String getUrlImage4() {
        return urlImage4;
    }

    public void setUrlImage4(String urlImage4) {
        this.urlImage4 = urlImage4;
    }

    public String getUrlImage5() {
        return urlImage5;
    }

    public void setUrlImage5(String urlImage5) {
        this.urlImage5 = urlImage5;
    }

    public void addIngredient(Ingredient ingredient) {
        if (ingredients == null) {
            ingredients = new ArrayList<>();
        }
        if (ingredients.contains(ingredient)) {
            Ingredient firstIngredient = ingredients.get(ingredients.indexOf(ingredient));
            firstIngredient.mergeIngredient(ingredient);
        } else {
            ingredients.add(ingredient);
        }
    }


}
