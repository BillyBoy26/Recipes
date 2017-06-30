package com.example.benjamin.recettes.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.task.DownloadImageTask;

public class ImageInputView extends AppCompatImageView {

    private String urlImage;

    public ImageInputView(Context context) {
        super(context);
        initImage(context);
    }

    public ImageInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initImage(context);
    }

    public ImageInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initImage(context);
    }

    private void initImage(final Context context) {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.empty_photo));

        final String[] alertChoices = new String[]{
                getContext().getString(R.string.importByUrl),
                getContext().getString(R.string.importBySdcard)
        } ;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMultiChoiceItems(alertChoices, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        String currentChoice = alertChoices[which];
                        if (currentChoice.equals(getContext().getString(R.string.importByUrl))) {
                            buildUrlDialog(context);
                        }
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void buildUrlDialog(Context context) {
        AlertDialog.Builder builderUrl = new AlertDialog.Builder(context);
        final EditText txtUrl = new EditText(getContext());
        builderUrl.setView(txtUrl);
        builderUrl.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = txtUrl.getText().toString();
                if (!url.trim().isEmpty()) {
                    new DownloadImageTask(ImageInputView.this).execute(url);
                } else {
                    dialog.cancel();
                }
            }
        });
        builderUrl.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builderUrl.show();
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
