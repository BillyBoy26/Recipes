package com.example.benjamin.recettes.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.benjamin.recettes.views.ImageInputView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String,Void,Bitmap> {

    ImageView imageView;
    private String urlImage;

    public DownloadImageTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        urlImage = params[0];
        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            inputStream = new URL(urlImage).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
        if (imageView instanceof ImageInputView) {
            ((ImageInputView) imageView).setUrlImage(urlImage);
        }
    }
}
