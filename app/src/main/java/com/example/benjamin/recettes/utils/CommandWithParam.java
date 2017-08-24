package com.example.benjamin.recettes.utils;

import java.io.Serializable;

public interface CommandWithParam<T> extends Serializable {
    void execute(T param);
}
