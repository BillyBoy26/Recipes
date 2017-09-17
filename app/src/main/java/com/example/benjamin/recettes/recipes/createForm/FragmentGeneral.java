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
import android.util.Log;
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
import com.example.benjamin.recettes.data.HasName;
import com.example.benjamin.recettes.data.Recipe;
import com.example.benjamin.recettes.data.Tags;
import com.example.benjamin.recettes.recipes.FilterSelectAdapter;
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
    private FilterSelectAdapter filterAdapter;
    private SimpleImageActionListener imgActionListner;
    private RatingBar ratingBar;
    private FlexboxLayout pnlTags;
    private EditText txtTags;
    private CheckBox cbAllFilterDialog;
    private List<Tags> allTags;

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
        txtTags = (EditText) generalView.findViewById(R.id.tags);
        txtNbCovers = (EditText) generalView.findViewById(R.id.nb_covers);
        txtTimeCook = (EditText) generalView.findViewById(R.id.time_cook);
        txtUrlVideo = (EditText) generalView.findViewById(R.id.url_video);
        txtTimePrepare = (EditText) generalView.findViewById(R.id.time_prepare);
        txtTotalTime = (EditText) generalView.findViewById(R.id.time_total);
        btnUrlVideo = (Button) generalView.findViewById(R.id.btnUrlVideo);
        pnlCategories = (FlexboxLayout) generalView.findViewById(R.id.pnlCategories);
        pnlTags = (FlexboxLayout) generalView.findViewById(R.id.pnlTags);
        ratingBar = (RatingBar) generalView.findViewById(R.id.ratingBar);


        filterAdapter = new FilterSelectAdapter();
        View.OnClickListener clickHandlerFilter = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCategories = v == txtCategory;
                boolean hasData;
                if (isCategories) {
                    hasData = CollectionUtils.notNullOrEmpty(allCategories);
                    filterAdapter.setDatas(allCategories);
                    if (recipe != null && recipe.getCategories() != null) {
                        filterAdapter.setSelectedElements(new HashSet<>(recipe.getCategories()));
                    }
                } else {
                    hasData = CollectionUtils.notNullOrEmpty(allTags);
                    filterAdapter.setDatas(allTags);
                    if (recipe != null && recipe.getTags() != null) {
                        filterAdapter.setSelectedElements(new HashSet<>(recipe.getTags()));
                    }
                }
                try {
                    createDialogFilter(isCategories,hasData);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("SHOW_DIALOG", e.getMessage());
                }

            }
        };
        txtCategory.setOnClickListener(clickHandlerFilter);
        txtTags.setOnClickListener(clickHandlerFilter);
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
                String url = txtUrlVideo.getText().toString();
                if (SUtils.notNullOrEmpty(url) && URLUtil.isValidUrl(url)) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            }
        });
        return generalView;
    }

    private void addButtonToView(final String name, final List<? extends HasName> hasNames, final FlexboxLayout pnlButtons) {
        final Button btn = (Button) getActivity().getLayoutInflater().inflate(R.layout.btn_tag, null);
        btn.setText(name);
        pnlButtons.addView(btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (HasName hasName : hasNames) {
                    if (name.equalsIgnoreCase(hasName.getName())) {
                        hasNames.remove(hasName);
                        break;
                    }
                }
                pnlButtons.removeView(btn);
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
            fillTagsView();
        }

    }

    private void fillCategoriesView() {
        pnlCategories.removeAllViews();
        if (CollectionUtils.notNullOrEmpty(recipe.getCategories())) {
            for (Category category : recipe.getCategories()) {
                addButtonToView(category.getName(), recipe.getCategories(), pnlCategories);
            }
        }
    }

    private void fillTagsView() {
        pnlTags.removeAllViews();
        if (CollectionUtils.notNullOrEmpty(recipe.getTags())) {
            for (Tags tag : recipe.getTags()) {
                addButtonToView(tag.getName(), recipe.getTags(), pnlTags);
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
        //les tags sont gérés automatiquement
    }


    public void setDatas(Recipe recipe,List<Category> categories,List<Tags> tags) {
        this.recipe = recipe;
        this.allCategories = categories;
        this.allTags = tags;
        fillRecipeView();
    }


    private AlertDialog createDialogFilter(final boolean isCategories, boolean hasData) {
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.recipe_create_category_select, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.categories)
                .setView(dialogView);

        RecyclerView gridFilters = (RecyclerView) dialogView.findViewById(R.id.gridCategory);
        gridFilters.setAdapter(filterAdapter);
        gridFilters.setLayoutManager(new GridLayoutManager(getContext(),2));


        cbAllFilterDialog = (CheckBox) dialogView.findViewById(R.id.selectAllCat);
        cbAllFilterDialog.setVisibility(hasData ? View.VISIBLE:View.GONE);
        final CheckBox cbNewElement = (CheckBox) dialogView.findViewById(R.id.cbNewCat);
        final EditText txtNewElement = (EditText) dialogView.findViewById(R.id.txtNewCat);
        cbAllFilterDialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    filterAdapter.setSelectedElements(new HashSet<>(isCategories ? allCategories:allTags));
                } else {
                    filterAdapter.setSelectedElements(null);
                }
            }
        });
        txtNewElement.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cbNewElement.setChecked(true);
                }
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = txtNewElement.getText().toString();
                if (isCategories) {
                    Set<Category> selectedCategories = filterAdapter.getSelectedElements();
                    if (cbNewElement.isChecked() && SUtils.notNullOrEmpty(newName)) {
                        Category newCat = new Category(newName);
                        selectedCategories.add(newCat);
                        allCategories.add(newCat);
                    }
                    recipe.setCategories(new ArrayList<>(selectedCategories));
                    fillCategoriesView();
                } else {
                    Set<Tags> selectedElements = filterAdapter.getSelectedElements();
                    if (cbNewElement.isChecked() && SUtils.notNullOrEmpty(newName)) {
                        Tags newTag = new Tags(newName);
                        selectedElements.add(newTag);
                        allTags.add(newTag);
                    }
                    recipe.setTags(new ArrayList<>(selectedElements));
                    fillTagsView();
                }

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.show();
    }



    public void setImgActionListner(SimpleImageActionListener imgActionListner) {
        this.imgActionListner = imgActionListner;
    }
}
