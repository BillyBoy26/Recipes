package com.example.benjamin.recettes.utils;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.views.ImageInputView;
import com.squareup.picasso.Picasso;

public class ImageUtils {

    public static void loadImage(Recipe.ImageData mainImage, ImageView imageView, Integer default_image) {
        ImageInputView inputView = imageView instanceof ImageInputView ? (ImageInputView) imageView : null;
        if (SUtils.notNullOrEmpty(mainImage.getUrlImage())) {
            Picasso.with(imageView.getContext()).load(mainImage.getUrlImage()).centerCrop().fit().into(imageView);
            if (inputView != null) {
                inputView.setUrlImage(mainImage.getUrlImage());
            }
        } else if(SUtils.notNullOrEmpty(mainImage.getPathToImage())){
            imageView.setImageBitmap(BitmapFactory.decodeFile(mainImage.getPathToImage()));
            if (inputView != null) {
                inputView.setPath(mainImage.getPathToImage());
            }
        } else if(default_image != null){
            imageView.setImageResource(default_image);
        }
    }
}
