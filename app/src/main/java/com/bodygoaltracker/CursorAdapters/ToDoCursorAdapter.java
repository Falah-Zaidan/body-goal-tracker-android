package com.bodygoaltracker.CursorAdapters;

import android.R.color;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.bodygoaltracker.DB;
import com.bodygoaltracker.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class ToDoCursorAdapter extends CursorAdapter {
    private LayoutInflater mInflater;

    public ToDoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.listview, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Float carbs = cursor.getFloat(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_CARBS_COLUMN));
        Float protein = cursor.getFloat(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_PROTEIN_COLUMN));
        Float fats = cursor.getFloat(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_FATS_COLUMN));
        int calories = cursor.getInt(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_CALORIE_COLUMN));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_DATE_COLUMN));

        TextView dateTextview = view.findViewById(R.id.datetextview);
        TextView calorieTextview = view.findViewById(R.id.calorieTextview);
        TextView carbsTextview = view.findViewById(R.id.carbsTextview);
        TextView proteinTextview = view.findViewById(R.id.proteinTextview);
        TextView fatTextview = view.findViewById(R.id.fatsTextview);

        carbsTextview.setTextColor(Color.rgb(255, 153, 51));
        proteinTextview.setTextColor(Color.rgb(51, 153, 255));
        fatTextview.setTextColor(Color.rgb(204, 0, 0));
        dateTextview.setTextColor(Color.rgb(0, 0, 0));
        calorieTextview.setTypeface(null, Typeface.BOLD);


        PieChart chart = (PieChart) view.findViewById(R.id.chart);
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(carbs, "Carbs"));
        entries.add(new PieEntry(protein, "Protein"));
        entries.add(new PieEntry(fats, "Fats"));

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(new int[]{color.holo_orange_light, color.holo_blue_light, color.holo_red_dark},
                context);
        PieData data = new PieData(set);
        chart.getDescription().setText("");
        chart.setData(data);
        chart.invalidate();


        calorieTextview.setText(calories + "Kcal");
        carbsTextview.setText(carbs + "g");
        proteinTextview.setText(protein + "g");
        fatTextview.setText(fats + "g");
        dateTextview.setText(date);

    }
}
