package com.example.benjamin.recettes.recipes.createForm;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.RatingBar;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.Category;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.recipes.CategoryFilterAdapter;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.ImageUtils;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.ImageInputView;
import com.example.benjamin.recettes.views.SimpleImageActionListener;
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
    private Button btnUrlVideo;
    private List<Category> allCategories;
    private Set<Category> selectCategories;
    private CategoryFilterAdapter adapterCategories;
    private SimpleImageActionListener imgActionListner;
    private RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View generalView = inflater.inflate(R.layout.recipe_create_general, container, false);


        txtName = (EditText) generalView.findViewById(R.id.name);
        imageView = (ImageInputView) generalView.findViewById(R.id.image1);
        imageView.setActionListener(imgActionListner);
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
        btnUrlVideo = (Button) generalView.findViewById(R.id.btnUrlVideo);
        pnlCategories = (FlexboxLayout) generalView.findViewById(R.id.pnlCategories);
        ratingBar = (RatingBar) generalView.findViewById(R.id.ratingBar);


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
                if (URLUtil.isValidUrl(url)) {
                    btnUrlVideo.setVisibility(View.VISIBLE);
                } else {
                    btnUrlVideo.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnUrlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = btnUrlVideo.getText().toString();
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
            btnUrlVideo.setVisibility(URLUtil.isValidUrl(recipe.getUrlVideo()) ? View.VISIBLE:View.GONE);
            ratingBar.setRating(recipe.getRating() != null ? recipe.getRating():0);

            ImageUtils.loadImage(recipe.getMainImage(), imageView, null);
            ImageUtils.loadImage(recipe.getImage2(), imageView2, null);
            ImageUtils.loadImage(recipe.getImage3(), imageView3, null);
            ImageUtils.loadImage(recipe.getImage4(), imageView4, null);
            ImageUtils.loadImage(recipe.getImage5(), imageView5, null);
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
        imageView.fill(recipe.getMainImage());
        imageView2.fill(recipe.getImage2());
        imageView3.fill(recipe.getImage3());
        imageView4.fill(recipe.getImage4());
        imageView5.fill(recipe.getImage5());
        recipe.setPrepareTime(txtTimePrepare.getText().toString());
        recipe.setCookTime(txtTimeCook.getText().toString());
        recipe.setTotalTime(txtTotalTime.getText().toString());
        recipe.setNbCovers(txtNbCovers.getText().toString());
        recipe.setUrlVideo(txtUrlVideo.getText().toString());
        recipe.setRating(ratingBar.getRating());
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

    public void setImgActionListner(SimpleImageActionListener imgActionListner) {
        this.imgActionListner = imgActionListner;
    }
}
