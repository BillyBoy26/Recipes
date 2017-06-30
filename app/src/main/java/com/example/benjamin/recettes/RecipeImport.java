package com.example.benjamin.recettes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.benjamin.recettes.task.HttpRequest;

public class RecipeImport extends DrawerActivity {

    private EditText txtUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNavigationView().setCheckedItem(R.id.nav_import);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.import_recipe_layout, contentFrameLayout);

        txtUrl = (EditText) findViewById(R.id.importUrl);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_done_white_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUrl.getText() != null) {
                    new HttpRequest(RecipeImport.this).execute(txtUrl.getText().toString());
                }
            }
        });
    }

}
