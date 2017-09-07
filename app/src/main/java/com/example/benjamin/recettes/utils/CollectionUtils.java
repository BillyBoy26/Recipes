package com.example.benjamin.recettes.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {

    public static boolean notNullOrEmpty(Collection collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean nullOrEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static String toWhereClause(List<String> strings) {
        if (CollectionUtils.nullOrEmpty(strings)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String str : strings) {
            builder.append(str + ",");
        }
        return builder.deleteCharAt(builder.length()-1).toString();
    }

    public static<T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        if (nullOrEmpty(collection)) {
            return new ArrayList<>();
        }
        List<T> filtredList = new ArrayList<>();
        for (T elem : collection) {
            if (predicate.apply(elem)) {
                filtredList.add(elem);
            }
        }
        return filtredList;
    }

    public static <T> ArrayList<T> asArrayList(T... args) {
        ArrayList<T> list = new ArrayList<>();
        for (T arg : args) {
            list.add(arg);
        }
        return list;
    }
}
