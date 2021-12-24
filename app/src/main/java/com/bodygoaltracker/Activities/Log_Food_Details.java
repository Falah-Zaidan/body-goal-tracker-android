package com.bodygoaltracker.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bodygoaltracker.CursorAdapters.ToDoCursorAdapter;
import com.bodygoaltracker.DB;
import com.bodygoaltracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Log_Food_Details extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int SHOW_SUBACTIVITY = 1;
    public static final String ROW_ID = "row_id";
    public static ToDoCursorAdapter todoAdapter;
    public Cursor todoCursor;
    public static int count = 0;
    ListView listview;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DB.init(this);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_log__food__details);

        initialiseViews();

        /*
        // ========== DB Util for exporting DB ==========//
        DBUtil dbUtil = new DBUtil(this);
        dbUtil.exportDatabase();
        */

        AddItemsToList();
    }

    public void initialiseViews() {
        //Toolbar:
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Day breakdown");
        ab.setDisplayHomeAsUpEnabled(true);

        //FAB:
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Log_Food_Details.this, AddItem.class);
                startActivityForResult(intent, SHOW_SUBACTIVITY);
            }
        });

        //ListView:
        listview = findViewById(R.id.listview);

        registerForContextMenu(listview);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        id = info.id;

        if (v.getId() == R.id.listview) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.longclickmenu, menu);
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                deleteRowItem();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void deleteRowItem() {

        String table = DB.MySQLOpenLiteHelper.MACRO_TOTAL_TABLE;
        String where = DB.DBContract.KEY_ID + "=?";
        String[] whereArgs = new String[]{id + ""};

        DB.db.delete(table, where, whereArgs);

        AddItemsToList();

    }

    public void AddItemsToList() {

        String query = "SELECT MacroTotalTable.CARBS_COLUMN, " +
                "DateTable._id, " +
                "DateTable.DATE_COLUMN, " +
                "MacroTotalTable.PROTEIN_COLUMN, " +
                "MacroTotalTable.FATS_COLUMN, " +
                "MacroTotalTable.CALORIE_COLUMN " +
                "FROM MacroTotalTable " +
                "INNER JOIN DateTable " +
                "ON MacroTotalTable._id = DateTable._id ORDER BY date(DateTable.DATE_COLUMN)";

        todoCursor = DB.db.rawQuery(query, null);
        count = todoCursor.getCount();
        todoAdapter = new ToDoCursorAdapter(this, todoCursor);
        //DB.mySQLOpenLiteHelper.onUpgrade(DB.db, 0, 1);
        todoAdapter.changeCursor(todoCursor);


        listview.setAdapter(todoAdapter);
        listview.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Log_Food_Details.this, DayBreakdown.class);
        intent.putExtra(ROW_ID, id);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (count != todoCursor.getCount()) {
            finish();
        }
        AddItemsToList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        todoCursor.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_show_items:
                return true;
            case R.id.clear_db:
                DB.mySQLOpenLiteHelper.onUpgrade(DB.db, 0, 1);
                Intent intent = new Intent(Log_Food_Details.this, Log_Food_Details.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
