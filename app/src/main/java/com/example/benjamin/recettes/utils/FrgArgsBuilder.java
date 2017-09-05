package com.example.benjamin.recettes.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FrgArgsBuilder<T extends Fragment> {

    private final T fragment;
    private final Bundle bundle;

    public FrgArgsBuilder(T fragment) {
        this.fragment = fragment;
        bundle = new Bundle();
    }

    public FrgArgsBuilder putBoolean(String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public T create() {
        fragment.setArguments(bundle);
        return fragment;
    }
}
