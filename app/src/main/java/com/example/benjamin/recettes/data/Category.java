package com.example.benjamin.recettes.data;

import java.io.Serializable;

public class Category implements Serializable {
    private Long id;
    private String name;



    public Category() {
    }

    public Category(String name) {
        this.name = name;
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
}
