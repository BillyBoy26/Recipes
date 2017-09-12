package com.example.benjamin.recettes.data;

import com.example.benjamin.recettes.utils.SUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable, HasName, HasSteps{



    public enum RecipeFiller{
        WITH_STEPS, WITH_ING, WITH_CAT
    }



    private ImageData mainImage = new ImageData();
    private String description;
    private String name;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Step> steps = new ArrayList<>();
    private Long id;
    private List<Category> categories = new ArrayList<>();
    private List<Tags> tags = new ArrayList<>();
    private String prepareTime;
    private String cookTime;
    private String totalTime;
    private String nbCovers;
    private boolean batchCooking;
    private String urlVideo;
    private ImageData image2 = new ImageData();
    private ImageData image3 = new ImageData();
    private ImageData image4 = new ImageData();
    private ImageData image5 = new ImageData();
    private Float rating;

    public Recipe() {
    }

    public Recipe(String name, String urlImage) {
        this.name = name;
        mainImage.setUrlImage(urlImage);
    }


    public ImageData getMainImage() {
        return mainImage;
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

    public ImageData getImage2() {
        return image2;
    }

    public void setImage2(ImageData image2) {
        this.image2 = image2;
    }

    public ImageData getImage3() {
        return image3;
    }

    public void setImage3(ImageData image3) {
        this.image3 = image3;
    }

    public ImageData getImage4() {
        return image4;
    }

    public void setImage4(ImageData image4) {
        this.image4 = image4;
    }

    public ImageData getImage5() {
        return image5;
    }

    public void setImage5(ImageData image5) {
        this.image5 = image5;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
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

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public class ImageData implements Serializable{
        public static final String PATH = "PATH";
        public static final String URL = "URL";
        private String urlImage;
        private String pathToImage;

        public String getUrlImage() {
            return urlImage;
        }

        public void setUrlImage(String urlImage) {
            this.urlImage = urlImage;
        }

        public String getPathToImage() {
            return pathToImage;
        }

        public void setPathToImage(String pathToImage) {
            this.pathToImage = pathToImage;
        }

        public String asStorableString() {
            StringBuilder builder = new StringBuilder();
            if (SUtils.notNullOrEmpty(urlImage)) {
                builder.append(URL);
                builder.append(urlImage);
            } else if (SUtils.notNullOrEmpty(pathToImage)) {
                builder.append(PATH);
                builder.append(pathToImage);
            }

            return builder.toString();
        }

        public void parseStorableData(String storeImageUrl) {
            if (SUtils.nullOrEmpty(storeImageUrl)) {
                return;
            }
            if (storeImageUrl.startsWith(URL)) {
                urlImage = storeImageUrl.replace(URL, "");
            } else if (storeImageUrl.startsWith(PATH)) {
                pathToImage = storeImageUrl.replace(PATH, "");
            } else {
                urlImage = storeImageUrl;
            }
        }

        public void clear() {
            urlImage = "";
            pathToImage = "";
        }
    }

}
