package com.example.benjamin.recettes.recipes.createForm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.benjamin.recettes.R;
import com.example.benjamin.recettes.data.HasSteps;
import com.example.benjamin.recettes.data.Step;
import com.example.benjamin.recettes.utils.SUtils;
import com.example.benjamin.recettes.views.NameAdapter;
import com.example.benjamin.recettes.views.RecyclerViewClickListener;
import com.example.benjamin.recettes.views.SimpleItemDividerDecoration;

import java.util.List;

public class FragmentSteps extends Fragment implements RecyclerViewClickListener {


    public static final String CAN_ADD = "CAN_ADD";
    private NameAdapter<Step> adapter;
    private HasSteps dataWithSteps;
    private List<Step> steps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.recipe_create_steps, container, false);
        final RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerSteps);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //check si on affiche le panel pour ajouter des étapes.
        if (getArguments() != null && getArguments().get(CAN_ADD) != null && !(Boolean) getArguments().get(CAN_ADD)) {
            LinearLayout searchView = (LinearLayout) layout.findViewById(R.id.pnlSearchAndAdd);
            searchView.setVisibility(View.GONE);
        } else {
            final EditText editTxtStep = (EditText) layout.findViewById(R.id.editTxtStep);
            final ImageView iconAddStep = (ImageView) layout.findViewById(R.id.iconAddStep);
            iconAddStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SUtils.nullOrEmpty(editTxtStep.getText().toString())) {
                        return;
                    }
                    Step step = new Step();
                    step.setName(editTxtStep.getText().toString());
                    adapter.addItem(step);
                }
            });
        }

        fillView();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleItemDividerDecoration(getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP );
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                adapter.moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);


        return layout;
    }

    private void fillView() {
        if (adapter == null) {
            adapter = new NameAdapter<>(true, this);
        }
        adapter.setDatas(steps);
    }

    public void setData(HasSteps data) {
        this.dataWithSteps = data;
        this.steps = data != null ? data.getSteps() : null;
        fillView();
    }

    public void getData() {
        if (steps != null) {
            for (Step step : steps) {
                step.setRank(steps.indexOf(step) + 1);
            }
        }
        if (dataWithSteps != null) {
            dataWithSteps.setSteps(steps);
        }

    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        fillView();
    }

    @Override
    public void onItemClick(View view, int position) {
        final Step step = adapter.getItem(position);
        final EditText textView = new EditText(getContext());
        textView.setText(step.getName());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.edit_step)
                .setView(textView);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                step.setName(textView.getText().toString());
                adapter.setDatas(steps);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                steps.remove(step);
                adapter.setDatas(steps);
            }
        });
        builder.show();
    }

}
