package com.bodygoaltracker.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bodygoaltracker.DB;
import com.bodygoaltracker.R;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddItemSeperately extends AppCompatActivity implements View.OnClickListener {

    private long id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_addtem_seperately);

        DB.init(this);
        initialiseViews();

        Intent intent = getIntent();
        id = intent.getLongExtra("EXTRA_ID", -1);
    }

    public void initialiseViews() {

        //Toolbar:
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Add another item");
        ab.setDisplayHomeAsUpEnabled(true);

        //Done button:
        Button addBtn = findViewById(R.id.addbtn);
        addBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        EditText fooditemEditText = findViewById(R.id.fooditemEditText);
        EditText carbsEditText = findViewById(R.id.carbsEditText);
        EditText proteinEditText = findViewById(R.id.proteinEditText);
        EditText fatsEditText = findViewById(R.id.fatEditText);

        //Prevent user from leaving fields empty:
        if (fooditemEditText.getText().toString().isEmpty() ||
                carbsEditText.getText().toString().isEmpty() ||
                proteinEditText.getText().toString().isEmpty() ||
                fatsEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please do not leave fields empty", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        String itemName = fooditemEditText.getText().toString();
        Float carbs = Float.parseFloat(carbsEditText.getText().toString());
        Float protein = Float.parseFloat(proteinEditText.getText().toString());
        Float fats = Float.parseFloat(fatsEditText.getText().toString());

        int calories = Math.round((carbs * 4) + (protein * 4) + (fats * 9));

        //Query the DB for current column values, then add new ones, then update the table MacroTotalTable:

        String query1 = "SELECT m._id, m.PROTEIN_COLUMN, m.FATS_COLUMN, m.CALORIE_COLUMN, m.FOOD_NAME_COLUMN, m.CARBS_COLUMN, " +
                "m.fk_id, d._id, d.DATE_COLUMN " +
                "FROM MacrosTable m JOIN " +
                "DateTable d " +
                "ON m.fk_id = d._id " +
                "WHERE m.fk_id=?;";

        String[] selectionArgs = new String[]{id + ""};

        Cursor aCursor = DB.db.rawQuery(query1, selectionArgs);

        int calorieTotal = 0;
        Float carbsTotal = 0.0f;
        Float proteinTotal = 0.0f;
        Float fatsTotal = 0.0f;

        while (aCursor.moveToNext()) {
            calorieTotal += aCursor.getInt(aCursor.getColumnIndexOrThrow(DB.DBContract.KEY_CALORIE_COLUMN));
            carbsTotal += aCursor.getFloat(aCursor.getColumnIndexOrThrow(DB.DBContract.KEY_CARBS_COLUMN));
            proteinTotal += aCursor.getFloat(aCursor.getColumnIndexOrThrow(DB.DBContract.KEY_PROTEIN_COLUMN));
            fatsTotal += aCursor.getFloat(aCursor.getColumnIndexOrThrow(DB.DBContract.KEY_FATS_COLUMN));
        }
        aCursor.close();

        carbsTotal += carbs;
        proteinTotal += protein;
        fatsTotal += fats;
        calorieTotal += Math.round((carbs * 4) + (protein * 4) + (fats * 9));

        ContentValues cv4 = new ContentValues();

        cv4.put(DB.DBContract.KEY_CARBS_COLUMN, carbsTotal);
        cv4.put(DB.DBContract.KEY_PROTEIN_COLUMN, proteinTotal);
        cv4.put(DB.DBContract.KEY_FATS_COLUMN, fatsTotal);
        cv4.put(DB.DBContract.KEY_CALORIE_COLUMN, calorieTotal);

        String where = DB.DBContract.KEY_ID + "=?";
        String[] whereArgs = {"" + id};

        DB.db.update(DB.MySQLOpenLiteHelper.MACRO_TOTAL_TABLE, cv4, where, whereArgs);


        //Update MacroTable:

        ContentValues cv = new ContentValues();

        cv.put(DB.DBContract.KEY_FOOD_NAME_COLUMN, itemName);
        cv.put(DB.DBContract.KEY_CARBS_COLUMN, carbs);
        cv.put(DB.DBContract.KEY_PROTEIN_COLUMN, protein);
        cv.put(DB.DBContract.KEY_FATS_COLUMN, fats);
        cv.put(DB.DBContract.KEY_CALORIE_COLUMN, calories);
        cv.put(DB.DBContract.KEY_ID_FK, id);

        DB.db.insert(DB.MySQLOpenLiteHelper.MACROS_TABLE, null, cv);

        //AddItemsToList();

        finish();
        Intent intent = new Intent(AddItemSeperately.this, DayBreakdown.class);
        intent.putExtra("ROW_ID", id);
        startActivity(intent);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(AddItemSeperately.this, DayBreakdown.class);
                intent.putExtra("ROW_ID", id);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
