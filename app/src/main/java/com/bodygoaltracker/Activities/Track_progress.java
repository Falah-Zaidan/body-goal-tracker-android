package com.bodygoaltracker.Activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bodygoaltracker.Adapters.ImageBodyweightAdapter;
import com.bodygoaltracker.DB;
import com.bodygoaltracker.Objects.AverageBodyweight;
import com.bodygoaltracker.Objects.BodyWeight;
import com.bodygoaltracker.Objects.Date;
import com.bodygoaltracker.Objects.Image;
import com.bodygoaltracker.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Track_progress extends AppCompatActivity implements View.OnClickListener {
    private TextView mFromDisplayDate;
    private TextView mToDisplayDate;
    private String date;
    ListView listview;
    ViewGroup header;
    LineChart lineChart;
    public byte[] imageByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_track_progress);

        DB.init(this);

        initialiseViews();

    }

    public void initialiseViews() {
        //Toolbar:
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Track Progress");
        ab.setDisplayHomeAsUpEnabled(true);

        //Set date:
        mFromDisplayDate =
                (TextView) findViewById(R.id.fromDateTextView);
        mToDisplayDate =
                (TextView) findViewById(R.id.toDateTextView);

        mFromDisplayDate.setOnClickListener(this);
        mToDisplayDate.setOnClickListener(this);

        //ListView:
        listview = findViewById(R.id.pictureAndBWListView);


    }

    public boolean setValues(String toDisplayText, String fromDisplayText) {

        String query = "SELECT * FROM " + DB.mySQLOpenLiteHelper.MACRO_TOTAL_TABLE +
                " WHERE " + DB.DBContract.KEY_DATE_COLUMN + " BETWEEN '" + fromDisplayText
                + "' AND '" + toDisplayText + "'";
        Log.d("TAG", "this is the query: " + query);


        Cursor cursor = DB.db.rawQuery(query, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No entries found for the selected time period",
                    Toast.LENGTH_LONG).show();
            //lineChart.invalidate();
            return false;
        }


        if (header == null) {
            LayoutInflater inflater = getLayoutInflater();
            header = (ViewGroup) inflater.inflate(R.layout.totalnutrientintake,
                    listview, false);
        }

        int totalCalories = 0;
        int totalCarbs = 0;
        int totalProtein = 0;
        int totalFats = 0;

        int numberOfDays = cursor.getCount();

        while (cursor.moveToNext()) {
            totalCalories += cursor.getInt(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_CALORIE_COLUMN));
            totalCarbs += cursor.getInt(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_CARBS_COLUMN));
            totalProtein += cursor.getInt(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_PROTEIN_COLUMN));
            totalFats += cursor.getInt(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_FATS_COLUMN));
        }
        cursor.close();

        lineChart = (LineChart) header.findViewById(R.id.lineChart);

        //Number all the days between the 2 dates the user selects
        //Get the corresponding BW values for each of the dates
        //Make a new Pair object with the numbered date and the BW value together


        ArrayList<Pair<Float, Float>> dateBWPairList = new ArrayList<>();


        String bwCursorQuery = "SELECT * FROM " + DB.mySQLOpenLiteHelper.BODYWEIGHT_TABLE +
                " WHERE " + DB.DBContract.KEY_DATE_COLUMN + " BETWEEN '" + fromDisplayText
                + "' AND '" + toDisplayText + "'";

        //String bwCursorQuery = "SELECT * FROM " + DB.mySQLOpenLiteHelper.BODYWEIGHT_TABLE + ";";

        Cursor bwCursor = DB.db.rawQuery(bwCursorQuery, null);

        for (int i = 0; i < bwCursor.getCount(); i++) {
            bwCursor.moveToNext();
            dateBWPairList.add(new Pair<Float, Float>(Float.parseFloat(i + "f"),
                    bwCursor.getFloat(bwCursor.getColumnIndexOrThrow(DB.DBContract.KEY_BODYWEIGHT_COLUMN))));
        }

        List<Entry> entries = new ArrayList<Entry>();

        for (int i = 0; i < dateBWPairList.size(); i++) {
            Pair<Float, Float> pair = dateBWPairList.get(i);
            entries.add(new Entry(pair.first, pair.second));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Bodyweight");

        LineData lineData = new LineData(dataSet);
        lineChart.getDescription().setText("");
        lineChart.setData(lineData);
        lineChart.invalidate();

        TextView averageCaloriesTextView = header.findViewById(R.id.caloriesTextView);
        TextView averageCarbsTextView = header.findViewById(R.id.carbsTextView);
        TextView averageProteinTextView = header.findViewById(R.id.proteinTextView);
        TextView averageFatsTextView = header.findViewById(R.id.fatsTextView);

        averageCaloriesTextView.setTypeface(null, Typeface.BOLD);
        averageCarbsTextView.setTypeface(null, Typeface.BOLD);
        averageProteinTextView.setTypeface(null, Typeface.BOLD);
        averageFatsTextView.setTypeface(null, Typeface.BOLD);

        averageCaloriesTextView.setText(totalCalories / numberOfDays + "Kcal");
        averageCarbsTextView.setText(totalCarbs / numberOfDays + "g");
        averageProteinTextView.setText(totalProtein / numberOfDays + "g");
        averageFatsTextView.setText(totalFats / numberOfDays + "g");

        if (listview.getHeaderViewsCount() < 1) {
            listview.addHeaderView(header);
        }

        return true;
    }

    public void getDBContents(String toDisplayText, String fromDisplayText) {

        String query = "SELECT * FROM " + DB.mySQLOpenLiteHelper.BODYWEIGHT_TABLE +
                " WHERE " + DB.DBContract.KEY_DATE_COLUMN + " BETWEEN '" + fromDisplayText
                + "' AND '" + toDisplayText + "'";

        Cursor cursor = DB.db.rawQuery(query, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No entries found for the selected time period",
                    Toast.LENGTH_LONG).show();
            return;
        }
        List<BodyWeight> bodyweightList = new ArrayList<>();

        double bw;
        String date;
        while (cursor.moveToNext()) {
            bw = cursor.getFloat(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_BODYWEIGHT_COLUMN));
            date = cursor.getString(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_DATE_COLUMN));
            bodyweightList.add(new BodyWeight(bw, date));
        }

        HashSet<Date> dateSet = new HashSet<>();

        for (int i = 0; i < bodyweightList.size(); i++) {
            dateSet.add(new Date(bodyweightList.get(i).getDate()));
        }

        ArrayList<AverageBodyweight> averageBodyweightList =
                new ArrayList<>();

        String dateObject;
        Iterator<Date> itr1 = dateSet.iterator();

        for (int i = 0; i < dateSet.size(); i++) {
            dateObject = itr1.next().getDate();
            double a = 0;
            int itemCount = 0;
            for (int j = 0; j < bodyweightList.size(); j++) {
                if (bodyweightList.get(j).getDate().equals(dateObject)) {
                    a += bodyweightList.get(j).getBodyweight();
                    itemCount += 1;
                }
            }
            averageBodyweightList.add(new AverageBodyweight(calculateAverage(a, itemCount), dateObject));
        }

        String query1 = "SELECT * FROM " + DB.mySQLOpenLiteHelper.IMAGES_TABLE +
                " WHERE " + DB.DBContract.KEY_DATE_COLUMN + " BETWEEN '" + fromDisplayText
                + "' AND '" + toDisplayText + "' ORDER BY date(ImagesTable.DATE_COLUMN)";

        //Make a list that stores unique images:
        Cursor cursor1 = DB.db.rawQuery(query1, null);
        ArrayList<Image> imageList = new ArrayList<>();


        Bitmap image;
        String imageDate;
        String extraInfo;
        while (cursor1.moveToNext()) {
            imageByte = cursor1.getBlob(cursor1.getColumnIndexOrThrow(DB.DBContract.KEY_IMAGE));
            extraInfo =
                    cursor1.getString(cursor1.getColumnIndexOrThrow(DB.DBContract.KEY_EXTRA_INFO_COLUMN));
            imageDate =
                    cursor1.getString(cursor1.getColumnIndexOrThrow(DB.DBContract.KEY_DATE_COLUMN));
            image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            imageList.add(new Image(imageDate, image, extraInfo));
        }

        HashSet<Date> sortedDateSet = new HashSet<>();

        for (int i = 0; i < imageList.size(); i++) {
            sortedDateSet.add(new Date(imageList.get(i).getDate()));
        }


        ImageBodyweightAdapter adapter = new ImageBodyweightAdapter(getApplicationContext(),
                averageBodyweightList, imageList);

        listview.setAdapter(adapter);

    }

    public double calculateAverage(double a, int itemCount) {
        return a / itemCount;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.toDateTextView:
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                final int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        if (month < 10 && dayOfMonth < 10) {
                            date = year + "-" + "0" + month + "-" + "0" + dayOfMonth;
                        }
                        if (month < 10 && dayOfMonth > 10) {
                            date = year + "-" + "0" + month + "-" + dayOfMonth;
                        }
                        if (month > 10 && dayOfMonth < 10) {
                            date = year + "-" + month + "-" + "0" + dayOfMonth;
                        }
                        if (month > 10 && dayOfMonth > 10) {
                            date = year + "-" + month + "-" + dayOfMonth;
                        }
                        mToDisplayDate.setText(date);
                        listenerMethod();
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(
                        Track_progress.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        listener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                break;
            case R.id.fromDateTextView:
                DatePickerDialog.OnDateSetListener listener2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        if (month < 10 && dayOfMonth < 10) {
                            date = year + "-" + "0" + month + "-" + "0" + dayOfMonth;
                        }
                        if (month < 10 && dayOfMonth > 10) {
                            date = year + "-" + "0" + month + "-" + dayOfMonth;
                        }
                        if (month > 10 && dayOfMonth < 10) {
                            date = year + "-" + month + "-" + "0" + dayOfMonth;
                        }
                        if (month > 10 && dayOfMonth > 10) {
                            date = year + "-" + month + "-" + dayOfMonth;
                        }
                        mFromDisplayDate.setText(date);
                        listenerMethod();
                    }
                };
                Calendar cal1 = Calendar.getInstance();
                int year1 = cal1.get(Calendar.YEAR);
                int month1 = cal1.get(Calendar.MONTH);
                int day1 = cal1.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog1 = new DatePickerDialog(
                        Track_progress.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        listener2,
                        year1, month1, day1);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();
                break;
            default:
                break;
        }
    }

    public void listenerMethod() {

        String toDisplayText = mToDisplayDate.getText().toString();
        String fromDisplayText = mFromDisplayDate.getText().toString();

        if (!toDisplayText.equals("Select To") && !fromDisplayText.equals("Select From")) {
            if (!setValues(toDisplayText, fromDisplayText)) {
                listview.setAdapter(null);
                listview.removeHeaderView(header);
                return;
            }
            getDBContents(toDisplayText, fromDisplayText);
        }
    }

}
