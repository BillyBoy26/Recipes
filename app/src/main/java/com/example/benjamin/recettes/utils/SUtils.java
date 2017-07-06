package com.example.benjamin.recettes.utils;

public class SUtils {


    public static boolean notNullOrEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static boolean nullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
