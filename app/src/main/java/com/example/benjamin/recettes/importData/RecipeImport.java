package com.example.benjamin.recettes.importData;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.benjamin.recettes.DrawerActivity;
import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.task.HttpRequestTask;
import com.example.benjamin.recettes.utils.SUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class RecipeImport extends DrawerActivity {

    private EditText txtUrl;
    private static final List<String> HOST_ALLOWED = Arrays.asList("buzzfeed.com");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNavigationView().setCheckedItem(R.id.nav_import);
        setContent(R.layout.import_recipe_layout);
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
                    if (!HOST_ALLOWED.contains(getDomainName(url.getHost()))) {
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
    }

    public static String getDomainName(String host) {
        return host.startsWith("www.") ? host.substring(4) : host;
    }

}
