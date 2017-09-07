package com.example.benjamin.recettes.recipes.createForm;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.recipes.CategoryFilterAdapter;
import com.example.benjamin.recettes.task.DownloadImageTask;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.ImageInputView;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentGeneral  extends Fragment {


    private EditText txtName;
    private EditText txtTimePrepare;
    private EditText txtTimeCook;
    private EditText txtTotalTime;
    private EditText txtNbCovers;
    private EditText txtUrlVideo;
    private ImageInputView imageView;
    private Recipe recipe;
    private EditText txtCategory;
    private FlexboxLayout pnlCategories;
    private ImageInputView imageView2;
    private ImageInputView imageView3;
    private ImageInputView imageView4;
    private ImageInputView imageView5;
    private TextView lblUrlVideo;
    private List<Category> allCategories;
    private Set<Category> selectCategories;
    private CategoryFilterAdapter adapterCategories;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View generalView = inflater.inflate(R.layout.recipe_create_general, container, false);
        txtName = (EditText) generalView.findViewById(R.id.name);
        imageView = (ImageInputView) generalView.findViewById(R.id.image1);
        imageView2 = (ImageInputView) generalView.findViewById(R.id.image2);
        imageView3 = (ImageInputView) generalView.findViewById(R.id.image3);
        imageView4 = (ImageInputView) generalView.findViewById(R.id.image4);
        imageView5 = (ImageInputView) generalView.findViewById(R.id.image5);
        txtCategory = (EditText) generalView.findViewById(R.id.category);
        txtNbCovers = (EditText) generalView.findViewById(R.id.nb_covers);
        txtTimeCook = (EditText) generalView.findViewById(R.id.time_cook);
        txtUrlVideo = (EditText) generalView.findViewById(R.id.url_video);
        txtTimePrepare = (EditText) generalView.findViewById(R.id.time_prepare);
        txtTotalTime = (EditText) generalView.findViewById(R.id.time_total);
        lblUrlVideo = (TextView) generalView.findViewById(R.id.lblUrlVideo);
        pnlCategories = (FlexboxLayout) generalView.findViewById(R.id.pnlCategories);


        adapterCategories = new CategoryFilterAdapter();
        View dialogSelectCatView = inflater.inflate(R.layout.recipe__create_category_select, null, false);
        final AlertDialog dialogCat = createDialogCategories(dialogSelectCatView);
        txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCategories.setDatas(allCategories);
                if (recipe != null && recipe.getCategories() != null) {
                    adapterCategories.setSelectedCategories(new HashSet<>(recipe.getCategories()));
                }

                if (!dialogCat.isShowing()) {
                    dialogCat.show();
                }
            }
        });
        fillRecipeView();

        txtUrlVideo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String url = txtUrlVideo.getText().toString();
                lblUrlVideo.setText(url);
                if (URLUtil.isValidUrl(url)) {
                    lblUrlVideo.setTextColor(Color.MAGENTA);
                } else {
                    lblUrlVideo.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lblUrlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = lblUrlVideo.getText().toString();
                if (SUtils.notNullOrEmpty(url) && URLUtil.isValidUrl(url)) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            }
        });
        return generalView;
    }

    private void addCatToView(final String catName, LayoutInflater inflater) {
        final Button btn = (Button) inflater.inflate(R.layout.btn_tag, null);
        btn.setText(catName);
        pnlCategories.addView(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Category category : recipe.getCategories()) {
                    if (catName.equalsIgnoreCase(category.getName())) {
                        recipe.getCategories().remove(category);
                        break;
                    }
                }
                pnlCategories.removeView(btn);
            }
        });
    }

    public void fillRecipeView() {
        if (recipe != null && txtName != null) {
            txtName.setText(SUtils.capitalize(recipe.getName()));
            txtTimeCook.setText(recipe.getCookTime());
            txtNbCovers.setText(recipe.getNbCovers());
            txtTotalTime.setText(recipe.getTotalTime());
            txtTimePrepare.setText(recipe.getPrepareTime());
            txtUrlVideo.setText(recipe.getUrlVideo());
            lblUrlVideo.setText(recipe.getUrlVideo());
            if (recipe.getUrlImage() != null) {
                new DownloadImageTask(imageView).execute(recipe.getUrlImage());
            }
            if (recipe.getUrlImage2() != null) {
                new DownloadImageTask(imageView2).execute(recipe.getUrlImage2());
            }
            if (recipe.getUrlImage3() != null) {
                new DownloadImageTask(imageView3).execute(recipe.getUrlImage3());
            }
            if (recipe.getUrlImage4() != null) {
                new DownloadImageTask(imageView4).execute(recipe.getUrlImage4());
            }
            if (recipe.getUrlImage5() != null) {
                new DownloadImageTask(imageView5).execute(recipe.getUrlImage5());
            }


            fillCategoriesView();
        }

    }

    private void fillCategoriesView() {
        pnlCategories.removeAllViews();
        if (CollectionUtils.notNullOrEmpty(recipe.getCategories())) {
            for (Category category : recipe.getCategories()) {
                addCatToView(category.getName(), getActivity().getLayoutInflater());
            }
        }


    }

    public void getRecipe() {
        recipe.setName( txtName.getText().toString());
        recipe.setUrlImage(imageView.getUrlImage());
        recipe.setUrlImage2(imageView2.getUrlImage());
        recipe.setUrlImage3(imageView3.getUrlImage());
        recipe.setUrlImage4(imageView4.getUrlImage());
        recipe.setUrlImage5(imageView5.getUrlImage());
        recipe.setPrepareTime(txtTimePrepare.getText().toString());
        recipe.setCookTime(txtTimeCook.getText().toString());
        recipe.setTotalTime(txtTotalTime.getText().toString());
        recipe.setNbCovers(txtNbCovers.getText().toString());
        recipe.setUrlVideo(txtUrlVideo.getText().toString());
        //les catégories sont gérés automatiquement
    }


    public void setDatas(Recipe recipe,List<Category> categories) {
        this.recipe = recipe;
        this.allCategories = categories;
        fillRecipeView();
    }


    private AlertDialog createDialogCategories(View dialogView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.categories)
                .setView(dialogView);

        RecyclerView gridCats = (RecyclerView) dialogView.findViewById(R.id.gridCategory);
        gridCats.setAdapter(adapterCategories);
        gridCats.setLayoutManager(new GridLayoutManager(getContext(),2));


        final CheckBox cbAllCat = (CheckBox) dialogView.findViewById(R.id.selectAllCat);
        final CheckBox cbNewCat = (CheckBox) dialogView.findViewById(R.id.cbNewCat);
        final EditText txtNewCat = (EditText) dialogView.findViewById(R.id.txtNewCat);
        cbAllCat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapterCategories.setSelectedCategories(new HashSet<>(allCategories));
                } else {
                    adapterCategories.setSelectedCategories(null);
                }
            }
        });
        txtNewCat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cbNewCat.setChecked(true);
                }
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Set<Category> selectedCategories = adapterCategories.getSelectedCategories();
                String newCatName = txtNewCat.getText().toString();
                if (cbNewCat.isChecked() && SUtils.notNullOrEmpty(newCatName)) {
                    selectedCategories.add(new Category(newCatName));
                }
                recipe.setCategories(new ArrayList<>(selectedCategories));
                fillCategoriesView();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
