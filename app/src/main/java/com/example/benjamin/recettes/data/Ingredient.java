package com.example.benjamin.recettes.data;

import android.util.Log;

import java.io.Serializable;

public class Ingredient implements Serializable, HasName{

    public Long id;
    private String name;
    private String quantityUnit = "";
    private int image = -1;
    private Float quantity = null;



    public Ingredient() {
    }

    public Ingredient(String name) {
        this.name = name;
    }

    public Ingredient(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public Ingredient(String name, int image, float quantity) {
        this(name, image, quantity, "");
    }

    public Ingredient(String name, int image, float quantity, String quantityUnit) {
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.quantityUnit = quantityUnit;
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

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantityUnit='" + quantityUnit + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    /**
     * @return true if merged
     */
    public boolean mergeIngredient(Ingredient ingredient) {
        if (ingredient.getQuantityUnit().equalsIgnoreCase(this.getQuantityUnit())) {
            Float otherIngQte = ingredient.getQuantity();
            if (otherIngQte != null && getQuantity() != null) {
                setQuantity(getQuantity() + otherIngQte);
            } else if (otherIngQte != null && getQuantity() == null) {
                setQuantity(otherIngQte);
            }
            return true;
        }
        Log.i("INGREDIENT", "Trying to merge two ingredient with different unit " + ingredient.toString() + ", " + this.toString());
        return false;
    }

}
