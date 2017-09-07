package com.example.benjamin.recettes.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.task.DownloadImageTask;
import com.example.benjamin.recettes.utils.CollectionUtils;

import java.util.List;

public class ImageInputView extends AppCompatImageView {

    private String urlImage;
    private boolean hasImage;

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
                        }
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void clearImage() {
        urlImage = "";
        setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.empty_photo));
    }

    private void buildUrlDialog(Context context) {
        AlertDialog.Builder builderUrl = new AlertDialog.Builder(context);
        final EditText txtUrl = new EditText(getContext());
        builderUrl.setView(txtUrl);
        builderUrl.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = txtUrl.getText().toString();
                if (URLUtil.isValidUrl(url)) {
                    new DownloadImageTask(ImageInputView.this).execute(url);
                    hasImage = true;
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
        hasImage = true;

    }
}
