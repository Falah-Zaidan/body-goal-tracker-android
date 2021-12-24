package com.bodygoaltracker.Adapters;

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

public class BWCursorAdapter extends CursorAdapter {
    private LayoutInflater mInflater;

    public BWCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mInflater.inflate(R.layout.bodyweightlistview, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Float bodyWeight = cursor.getFloat(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_BODYWEIGHT_COLUMN));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_DATE_COLUMN));

        TextView bodyweightTextView = view.findViewById(R.id.bodyweightTextView);
        TextView dateTextView = view.findViewById(R.id.datetextview);

        bodyweightTextView.setTypeface(null, Typeface.BOLD);
        dateTextView.setTextColor(Color.rgb(0, 0, 0));

        dateTextView.setText(date);
        bodyweightTextView.setText(bodyWeight + "kg");

    }
}
