package com.bodygoaltracker.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.bodygoaltracker.CursorAdapters.CursorAdapter2;
import com.bodygoaltracker.DB;
import com.bodygoaltracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DayBreakdown extends AppCompatActivity {

    private Cursor cursor;
    private CursorAdapter2 cursorAdapter2;
    public long id;
    ListView listview;
    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_day_breakdown);

        DB.init(this);
        initialiseViews();

        Log.d("TAG", "In DayBreakdown onCreate() ");

        Intent intent = getIntent();
        id = intent.getLongExtra(Log_Food_Details.ROW_ID, -1);

        if (id == -1) {
            id = intent.getLongExtra("ROW_ID", -1);
        }

        AddItemsToList();
    }

    public void initialiseViews() {

        //Toolbar:
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Day details");

        ab.setDisplayHomeAsUpEnabled(true);

        //FAB:
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayBreakdown.this, AddItemSeperately.class);
                intent.putExtra("EXTRA_ID", id);
                startActivity(intent);
            }
        });

        //ListView:
        listview = findViewById(R.id.listviewsofar);

    }


    private void AddItemsToList() {

        String query1 = "SELECT m._id, m.PROTEIN_COLUMN, m.FATS_COLUMN, m.CALORIE_COLUMN, m.FOOD_NAME_COLUMN, m.CARBS_COLUMN, " +
                "m.fk_id, d._id, d.DATE_COLUMN " +
                "FROM MacrosTable m JOIN " +
                "DateTable d " +
                "ON m.fk_id = d._id " +
                "WHERE m.fk_id=?;";

        String[] selectionArgs = new String[]{id + ""};

        cursor = DB.db.rawQuery(query1, selectionArgs);

        count = cursor.getCount();

        cursorAdapter2 = new CursorAdapter2(getApplicationContext(), cursor);

        listview.setAdapter(cursorAdapter2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (count != cursorAdapter2.getCount()) {
            finish();
        }
    }
}
