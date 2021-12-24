package com.bodygoaltracker.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.bodygoaltracker.Activities.Take_picture;
import com.bodygoaltracker.Adapters.GridImageAdapter;
import com.bodygoaltracker.DB;
import com.bodygoaltracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class Pictures_frag extends Fragment {

    public static int count = 0;
    private GridView gridView;
    private GridImageAdapter adapter;
    private ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();
    private ArrayList<String> imageNamesList = new ArrayList<String>();
    private ArrayList<String> imageDescriptions = new ArrayList<String>();
    private ArrayList<String> dateColumnsList = new ArrayList<String>();
    private ArrayList<Long> primaryKeys = new ArrayList<>();
    Cursor cursor;

    public Pictures_frag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pictures_frag, container, false);


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Take_picture.class);
                startActivity(intent);
            }
        });


        gridView = view.findViewById(R.id.gridview);
        registerForContextMenu(gridView);
        displayImage();

        return view;
    }

    public void displayImage() {

        imageList = new ArrayList<Bitmap>();
        imageNamesList = new ArrayList<String>();
        imageDescriptions = new ArrayList<String>();
        dateColumnsList = new ArrayList<String>();
        primaryKeys = new ArrayList<>();

        String qu = "SELECT IMAGE, _id, IMAGE_NAME, EXTRA_INFO_COLUMN, fk_id," +
                " DATE_COLUMN FROM ImagesTable ORDER BY date(ImagesTable.DATE_COLUMN)";

        cursor = DB.db.rawQuery(qu, null);
        count = cursor.getCount();


        byte[] bitmap = null;

        while (cursor.moveToNext()) {
            bitmap = cursor.getBlob(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_IMAGE));
            imageList.add(BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length));
            imageNamesList.add(cursor.getString(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_IMAGE_NAME)));
            dateColumnsList.add(cursor.getString(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_DATE_COLUMN)));
            imageDescriptions.add(cursor.getString(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_EXTRA_INFO_COLUMN)));
            primaryKeys.add(cursor.getLong(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_ID)));
        }

        cursor.close();
        adapter = new GridImageAdapter(getActivity(), imageNamesList, imageList, dateColumnsList, imageDescriptions, primaryKeys);
        gridView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (count != cursor.getCount()) {
            getActivity().finish();
        }

    }


}
