package com.example.benjamin.recettes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.benjamin.recettes.data.Step;
import com.example.benjamin.recettes.db.table.TJRegSte;
import com.example.benjamin.recettes.db.table.TStep;
import com.example.benjamin.recettes.utils.CollectionUtils;
import com.example.benjamin.recettes.utils.CursorUtils;
import com.example.benjamin.recettes.utils.SUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepDao extends GenericDao {
    public StepDao(Context context) {
        super(context);
    }

    public StepDao(SQLiteDatabase sqLiteDatabase) {
        super(sqLiteDatabase);
    }

    public Step createOrUpdate(Step step) {
        if (step == null) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TStep.C_NAME,step.getName());
        contentValues.put(TStep.C_RANK,step.getRank());
        contentValues.put(TStep.C_ID_REC,step.getIdRec());

        fillUpdatedate(contentValues);
        if (step.getId() != null) {
            contentValues.put(TStep._ID, step.getId());
            db.update(TStep.T_STEP,contentValues,"_id=" + step.getId(),null);
        } else {
            Long newIdCategory = db.insert(TStep.T_STEP, null, contentValues);
            step.setId(newIdCategory);
        }
        return step;
    }

    public boolean delete(Step step) {
        if (step == null || step.getId() == null) {
            return false;
        }

        db.delete(TStep.T_STEP, TStep._ID + "= ?", new String[]{step.getId().toString()});
        return true;
    }

    public List<Step> fetchStepsByRecId(String recId) {
        if (SUtils.nullOrEmpty(recId)) {
            return new ArrayList<>();
        }
        Cursor cursor = db.rawQuery("select " +
                        TStep._ID + ", " +
                        TStep.C_NAME + ", " +
                        TStep.C_RANK +
                        " FROM " + TStep.T_STEP +
                        " WHERE " + TStep.C_ID_REC + " = ?" +
                        " ORDER BY " + TStep.C_RANK
                , new String[]{recId});

        List<Step> steps = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                steps.add(getStepFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        return steps;

    }

    private Step getStepFromCursor(Cursor cursor) {
        Step step = new Step();
        step.setId(cursor.getLong(cursor.getColumnIndex(TStep._ID)));
        step.setName(cursor.getString(cursor.getColumnIndex(TStep.C_NAME)));
        step.setRank(CursorUtils.getIntegerColumnOrNull(cursor,TStep.C_RANK));
        if (step.getRank() == null) {
            step.setRank(CursorUtils.getIntegerColumnOrNull(cursor,TJRegSte.C_RANK));
        }
        step.setIdRec(CursorUtils.getLongColumnOrNull(cursor,TStep.C_ID_REC));
        return step;
    }

    public void deleteStepsFromRecId(Long idRec,List<String> notInStepId) {
        if (idRec == null) {
            return;
        }

        String whereClause = "";
        if (CollectionUtils.notNullOrEmpty(notInStepId)) {
            whereClause = " " + TStep._ID + " NOT IN (" + makePlaceholders(notInStepId.size()) + ") AND ";
        }
        whereClause += TStep.C_ID_REC + "= ?";
        notInStepId.add(String.valueOf(idRec));
        db.delete(TStep.T_STEP, whereClause, notInStepId.toArray(new String[0]));
    }

    public void createOrUpdate(List<Step> steps, Long idRec) {
        if (CollectionUtils.nullOrEmpty(steps)) {
            return;
        }
        for (Step step : steps) {
            step.setIdRec(idRec);
            createOrUpdate(step);
        }
    }

    public @NonNull Map<Long, List<Step>> fetchStepsByRecId(List<String> idsRec) {
        if (CollectionUtils.nullOrEmpty(idsRec)) {
            return new HashMap<>();
        }

        Cursor cursor = db.rawQuery("select " +
                        TStep._ID  + "," +
                        TStep.C_ID_REC + ", " +
                        TStep.C_NAME + ", " +
                        TStep.C_RANK +
                        " FROM " + TStep.T_STEP +
                        " WHERE " + TStep.C_ID_REC + " IN (" + makePlaceholders(idsRec.size()) + ")" +
                        " ORDER BY " + TStep.C_ID_REC + ", " + TStep.C_RANK
                , idsRec.toArray(new String[0]));
        Map<Long, List<Step>> stepsByRecId = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                Step step = getStepFromCursor(cursor);
                if (stepsByRecId.get(step.getIdRec()) == null) {
                    stepsByRecId.put(step.getIdRec(), new ArrayList<Step>());
                }
                stepsByRecId.get(step.getIdRec()).add(step);

            } while (cursor.moveToNext());
        }
        return stepsByRecId;
    }

    public List<Step> fetchStepByRegId(String regIdStr) {
        if (SUtils.nullOrEmpty(regIdStr)) {
            return new ArrayList<>();
        }
        Cursor cursor = db.rawQuery("select " +
                        TStep._ID + ", " +
                        TStep.C_NAME  + ", " +
                        TJRegSte.C_RANK  +
                        " FROM " + TJRegSte.TJ_REG_STE +
                        " INNER JOIN " + TStep.T_STEP + " ON " + TStep._ID + " = " + TJRegSte.C_ID_STEP +
                        " WHERE " + TJRegSte.C_ID_REG +  "= ?" +
                        " ORDER BY " + TJRegSte.C_RANK
                , new String[]{regIdStr});

        List<Step> steps = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                steps.add(getStepFromCursor(cursor));
            } while (cursor.moveToNext());
        }

        return steps;

    }
}
