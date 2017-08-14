package com.example.benjamin.recettes.utils;

import java.util.Collection;
import java.util.List;

public class CUtils {

    public static boolean notNullOrEmpty(Collection collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean nullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static String toWhereClause(List<String> strings) {
        if (CUtils.nullOrEmpty(strings)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String str : strings) {
            builder.append(str + ",");
        }
        return builder.deleteCharAt(builder.length()-1).toString();
    }
}
