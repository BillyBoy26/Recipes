package com.example.benjamin.recettes.data;

import java.io.Serializable;

public class Ingredient implements Serializable{

    public static final String SEP_ATTRIBUTE = ";";
    public static final String SEP_END = "§";
    private String name;
    private int image = -1;
    private int quantity = 1;

    public Ingredient(String name) {
        this.name = name;
    }

    public Ingredient(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public Ingredient(String name, int image, int quantity) {
        this.name = name;
        this.image = image;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ingredient that = (Ingredient) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name + SEP_ATTRIBUTE);
        builder.append(image + SEP_ATTRIBUTE);
        builder.append(quantity);
        builder.append(SEP_END);
        return builder.toString();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
