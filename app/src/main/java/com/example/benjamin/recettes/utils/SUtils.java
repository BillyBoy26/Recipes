package com.example.benjamin.recettes.utils;

public class SUtils {


    public static boolean notNullOrEmpty(String string) {
        return string != null && !string.isEmpty();
    }

    public static boolean nullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static String capitalize(final String line) {
        if (nullOrEmpty(line) || line.length() == 1) {
            return line;
        }
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
