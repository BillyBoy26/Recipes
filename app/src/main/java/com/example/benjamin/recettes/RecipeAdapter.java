package com.example.benjamin.recettes;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benjamin.recettes.db.table.TRecipe;
import com.squareup.picasso.Picasso;

class RecipeAdapter extends CursorAdapter {


    private final LayoutInflater cursorInflater;

    public RecipeAdapter(Context context,Cursor cursor,int flags) {
        super(context,cursor,flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.recipe_card,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String name = cursor.getString(cursor.getColumnIndex(TRecipe.C_NAME));
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(name);

        String urlImage = cursor.getString(cursor.getColumnIndex(TRecipe.C_URL_IMAGE));
        if (urlImage != null && !urlImage.isEmpty()) {
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            Picasso.with(imageView.getContext()).load(urlImage).centerCrop().fit().into(imageView);
        }
    }
}
