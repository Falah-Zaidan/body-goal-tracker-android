package com.bodygoaltracker.CursorAdapters;

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

public class CursorAdapter2 extends CursorAdapter {

    private LayoutInflater mInflater;

    public CursorAdapter2(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.listviewsofar, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Float carbs = cursor.getFloat(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_CARBS_COLUMN));
        Float protein = cursor.getFloat(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_PROTEIN_COLUMN));
        Float fats = cursor.getFloat(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_FATS_COLUMN));
        int calories = cursor.getInt(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_CALORIE_COLUMN));
        String foodItemName = cursor.getString(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_FOOD_NAME_COLUMN));

        TextView macrosTextView = view.findViewById(R.id.macros);
        TextView itemNameTextView = view.findViewById(R.id.itemName);
        TextView caloriesTextView = view.findViewById(R.id.calories);

        caloriesTextView.setTypeface(null, Typeface.BOLD);
        macrosTextView.setTextColor(Color.rgb(0, 0, 0));
        itemNameTextView.setTextColor(Color.rgb(0, 0, 0));

        macrosTextView.setText(carbs + "C/" + protein + "P/" + fats + "F");
        itemNameTextView.setText(foodItemName);
        caloriesTextView.setText(calories + "kcal");


    }
}
