package com.bodygoaltracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bodygoaltracker.Objects.AverageBodyweight;
import com.bodygoaltracker.Objects.Image;
import com.bodygoaltracker.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageBodyweightAdapter extends BaseAdapter {

    private Context context;
    View view;
    private LayoutInflater inflater;
    private ArrayList<Image> images;
    private ArrayList<AverageBodyweight> averageBodyWeightList;

    private Map<String, AverageBodyweight> map = new HashMap<>();

    public ImageBodyweightAdapter(Context c, ArrayList<AverageBodyweight> averageBodyWeightList,
                                  ArrayList<Image> images) {
        context = c;
        this.averageBodyWeightList = averageBodyWeightList;
        for (AverageBodyweight weight : averageBodyWeightList) {
            map.put(weight.getDate(), weight);
        }
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
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

        view = new View(context);
        view = inflater.inflate(R.layout.imagebwlistview, null);

        TextView dateApproxTextView = (TextView) view.findViewById(R.id.bwApproxTextView);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        //set the images in the image view from the images list.
        imageView.setImageBitmap(images.get(position).getBitmap());

        //set the dates to the images depending on the date of the sortedImageDate set.

        AverageBodyweight averageBodyWeight = map.get(images.get(position).getDate());
        if (averageBodyWeight != null) {
            dateApproxTextView.setText(averageBodyWeight.getBodyweight() + "kg");
        } else {
            dateApproxTextView.setText("No Bodyweight record");
        }

        return view;
    }
}
