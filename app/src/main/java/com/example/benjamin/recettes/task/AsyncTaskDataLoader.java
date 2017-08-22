package com.example.benjamin.recettes.task;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AsyncTaskDataLoader<T> extends AsyncTaskLoader<T> {

    private static final AtomicInteger currentUniqueId = new AtomicInteger(0);
    private T data;
    private boolean hasResult = false;

    public static int getNewUniqueLoaderId() {
        return currentUniqueId.getAndIncrement();
    }

    public AsyncTaskDataLoader(Context context) {
        super(context);
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        } else if (hasResult) {
            deliverResult(data);
        }
    }

    @Override
    public void deliverResult(T data) {
        this.data = data;
        hasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (hasResult) {
            data = null;
            hasResult = false;
        }
    }


}
