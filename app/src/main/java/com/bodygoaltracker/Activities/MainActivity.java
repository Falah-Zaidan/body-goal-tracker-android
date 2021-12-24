package com.bodygoaltracker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bodygoaltracker.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseViews();
    }

    public void initialiseViews() {
        //Toolbar:
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Macro-Nutrient Tracker");

        //Card View 1:
        CardView cardView = findViewById(R.id.card_view);
        cardView.setOnClickListener(this);

        //Card View 2:
        CardView cardView2 = findViewById(R.id.card_view2);
        cardView2.setOnClickListener(this);

        //Card View 3:
        CardView cardView3 = findViewById(R.id.card_view3);
        cardView3.setOnClickListener(this);

        //PieChart:
        PieChart chart = cardView.findViewById(R.id.chart);
        chart.setTouchEnabled(false);

        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(50, "Carbs"));
        entries.add(new PieEntry(30, "Protein"));
        entries.add(new PieEntry(15, "Fats"));


        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(new int[]{android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_red_dark},
                getApplicationContext());
        PieData data = new PieData(set);
        chart.getDescription().setText("");
        chart.setData(data);
        chart.invalidate();

        //LineChart:
        LineChart lineChart = (LineChart) cardView3.findViewById(R.id.lineChart);
        lineChart.setTouchEnabled(false);

        int[] dataObjects = new int[]{1, 2, 3, 4, 5, 6};

        List<Entry> entries2 = new ArrayList<Entry>();

        entries2.add(new Entry(1, 2));
        entries2.add(new Entry(2, 3));
        entries2.add(new Entry(3, 4));
        entries2.add(new Entry(4, 5));
        entries2.add(new Entry(5, 6));
        entries2.add(new Entry(6, 7));

        Legend legend1 = lineChart.getLegend();
        legend1.setEnabled(false);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        LineDataSet dataSet = new LineDataSet(entries2, "");

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_view:
                Intent intent1 = new Intent(MainActivity.this, Log_Food_Details.class);
                startActivity(intent1);
                break;
            case R.id.card_view2:
                Intent intent2 = new Intent(MainActivity.this, Log_Progress.class);
                startActivity(intent2);
                break;
            case R.id.card_view3:
                Intent intent3 = new Intent(MainActivity.this, Track_progress.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
