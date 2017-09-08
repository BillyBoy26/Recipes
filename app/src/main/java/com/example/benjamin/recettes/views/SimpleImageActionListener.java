package com.example.benjamin.recettes.views;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class SimpleImageActionListener implements ImageInputView.ImageActionListener {

    public static final int RESULT_LOAD_IMAGE = 500;
    private Activity activity;
    private ImageInputView imageInputView;

    public SimpleImageActionListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onGaleryPickerListener(ImageInputView imageInputView) {
        this.imageInputView = imageInputView;
        Intent intent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);

        activity.startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    public void handleIntent(Intent data) {
        if (data == null) {
            return;
        }
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        imageInputView.setPath(picturePath);
        imageInputView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }


}
