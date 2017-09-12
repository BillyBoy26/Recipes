package com.example.benjamin.recettes.data;

import java.io.Serializable;

public class Tags implements HasName, Serializable {

    private Long id;
    private String name;



    public Tags() {

    }
    public Tags(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
