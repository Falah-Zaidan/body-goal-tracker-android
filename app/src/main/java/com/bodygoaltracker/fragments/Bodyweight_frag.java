package com.bodygoaltracker.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bodygoaltracker.Activities.BWInput;
import com.bodygoaltracker.Adapters.BWCursorAdapter;
import com.bodygoaltracker.DB;
import com.bodygoaltracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;


public class Bodyweight_frag extends Fragment {

    Cursor cursor;
    public static int count = 0;
    ListView listview;

    public Bodyweight_frag() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bodyweight_frag, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BWInput.class);
                startActivity(intent);
            }
        });

        listview = view.findViewById(R.id.listview);
        registerForContextMenu(listview);
        addItemsToList();

        return view;
    }


    public void addItemsToList() {

        String query1 = "SELECT _id, BODYWEIGHT_COLUMN, DATE_COLUMN " +
                "FROM BodyweightTable ORDER BY date(BodyweightTable.DATE_COLUMN);";

        cursor = DB.db.rawQuery(query1, null);
        BWCursorAdapter cursorAdapter = new BWCursorAdapter(getActivity(), cursor);

        count = cursor.getCount();

        listview.setAdapter(cursorAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (count != cursor.getCount()) {
            getActivity().finish();
        }
    }

}
