package com.example.benjamin.recettes.utils;

public interface CommandWithParam<T> {
    void execute(T param);
}
