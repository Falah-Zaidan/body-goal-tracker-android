package com.bodygoaltracker.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bodygoaltracker.R;

import java.util.ArrayList;

public class GridImageAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> imageNameList;
    private ArrayList<Bitmap> images;
    private ArrayList<String> dateColumnsList;
    private ArrayList<String> imageDescriptions;
    private ArrayList<Long> primaryKeys;

    public GridImageAdapter(Context c, ArrayList<String> imageNameList, ArrayList<Bitmap> images,
                            ArrayList<String> dateColumnsList, ArrayList<String> imageDescriptions,
                            ArrayList<Long> primaryKeys) {
        context = c;
        this.imageNameList = imageNameList;
        this.images = images;
        this.dateColumnsList = dateColumnsList;
        this.imageDescriptions = imageDescriptions;
        this.primaryKeys = primaryKeys;
    }

    @Override
    public int getCount() {
        return imageNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.row_item, null);
        } else {
            view = convertView;
        }

        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);

        dateTextView.setTextColor(Color.rgb(0, 0, 0));


        descriptionTextView.setText(imageDescriptions.get(position));
        imageView.setImageBitmap(images.get(position));
        dateTextView.setText(dateColumnsList.get(position));

        view.setTag(primaryKeys.get(position));

        return view;
    }
}
