package com.example.benjamin.recettes.importData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.task.BookmarksLoadTask;
import com.example.benjamin.recettes.task.HttpRequestTask;
import com.example.benjamin.recettes.utils.SUtils;

import java.net.MalformedURLException;
import java.net.URL;

import static com.example.benjamin.recettes.importData.HttpImportHelper.HOST_ALLOWED;

public class RecipeImport extends DrawerActivity {

    public static final int UPLOAD_FILE_CODE = 1;
    private EditText txtUrl;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNavigationView().setCheckedItem(R.id.nav_import);
        setContent(R.layout.recipe_import_layout);
        txtUrl = (EditText) findViewById(R.id.importUrl);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_done_white_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUrl.getText() == null || SUtils.nullOrEmpty(txtUrl.getText().toString())) {
                    Toast.makeText(RecipeImport.this, R.string.please_inform_url, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    URL url = new URL(txtUrl.getText().toString());
                    if (!HOST_ALLOWED.contains(HttpImportHelper.getDomainName(url.getHost()))) {
                        Toast.makeText(RecipeImport.this, R.string.site_not_allowed, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new HttpRequestTask(RecipeImport.this).execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Toast.makeText(RecipeImport.this, R.string.url_not_valid, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button button = (Button) findViewById(R.id.btnBookmarks);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, UPLOAD_FILE_CODE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPLOAD_FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                new BookmarksLoadTask(RecipeImport.this).execute(data.getData());
                ProgressDialog.show(this, getString(R.string.loading), getString(R.string.currently_loading_file));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
