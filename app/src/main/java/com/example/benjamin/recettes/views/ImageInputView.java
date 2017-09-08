package com.example.benjamin.recettes.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.SUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageInputView extends AppCompatImageView {

    interface ImageActionListener{
        void onGaleryPickerListener(ImageInputView imageInputView);
    }

    private String urlImage;
    private String path;
    private boolean hasImage;

    private ImageActionListener actionListener = null;

    public ImageInputView(Context context) {
        super(context);
        initImage();
    }

    public ImageInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initImage();
    }

    public ImageInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initImage();
    }

    public ImageActionListener getActionListener() {
        return actionListener;
    }

    public void setActionListener(ImageActionListener actionListener) {
        this.actionListener = actionListener;
    }

    private void initImage() {
        hasImage = false;
        clearImage();

        final List<String> alertChoices = CollectionUtils.asArrayList(
                getContext().getString(R.string.importByUrl),
                getContext().getString(R.string.importBySdcard)
        ) ;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                alertChoices.remove(getContext().getString(R.string.delete_image));
                if (hasImage) {
                     alertChoices.add(getContext().getString(R.string.delete_image));
                }

                builder.setMultiChoiceItems(alertChoices.toArray(new String[0]), null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        String currentChoice = alertChoices.get(which);
                        if (currentChoice.equals(getContext().getString(R.string.importByUrl))) {
                            buildUrlDialog(getContext());
                        } else if (currentChoice.equals(getContext().getString(R.string.delete_image))) {
                            clearImage();
                        } else if (currentChoice.equals(getContext().getString(R.string.importBySdcard))) {
                            if (actionListener != null) {
                                actionListener.onGaleryPickerListener(ImageInputView.this);
                            }
                        }
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void clearImage() {
        setUrlImage("");
        setPath("");
        setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.empty_photo));
    }

    private void buildUrlDialog(Context context) {
        AlertDialog.Builder builderUrl = new AlertDialog.Builder(context);
        final EditText txtUrl = new EditText(getContext());
        txtUrl.setHint(R.string.url);
        TextInputLayout txtLayout = new TextInputLayout(context);
        txtLayout.addView(txtUrl);
        builderUrl.setView(txtLayout);
        builderUrl.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = txtUrl.getText().toString();
                if (URLUtil.isValidUrl(url)) {
                    Picasso.with(getContext()).load(url).centerCrop().fit().into(ImageInputView.this);
                    setUrlImage(url);
                }
            }
        });
        builderUrl.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builderUrl.show();
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
        hasImage = SUtils.notNullOrEmpty(urlImage);
        path = "";

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        hasImage = SUtils.notNullOrEmpty(path);
        urlImage = "";
    }

    public void fill(Recipe.ImageData mainImage) {
        mainImage.clear();
        if (SUtils.notNullOrEmpty(urlImage)) {
            mainImage.setUrlImage(urlImage);
        } else if (SUtils.notNullOrEmpty(path)) {
            mainImage.setPathToImage(path);
        }
    }
}
