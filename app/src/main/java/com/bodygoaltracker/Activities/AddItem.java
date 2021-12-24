package com.bodygoaltracker.Activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bodygoaltracker.DB;
import com.bodygoaltracker.Objects.Day;
import com.bodygoaltracker.Objects.Item;
import com.bodygoaltracker.R;

import java.util.Calendar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddItem extends AppCompatActivity implements View.OnClickListener {

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_add_item);

        DB.init(this); //Initialise Database
        initialiseViews();

    }

    public void initialiseViews() {

        //Toolbar:
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Add your first Item");
        ab.setDisplayHomeAsUpEnabled(true);

        //Done button:
        Button b = findViewById(R.id.doneButton);
        b.setOnClickListener(this);

        //Display date:
        mDisplayDate =
                (TextView) findViewById(R.id.setDate);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddItem.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
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
                Log.d("TAG", "date from AddItem class: " + date);
                if (!checkForDuplicateDates(date)) {
                    mDisplayDate.setText(date);
                    //mDisplayDate.setTypeface(null, Typeface.NORMAL);
                    mDisplayDate.setTextColor(Color.rgb(0, 0, 0));
                }
                if (checkForDuplicateDates(date)) {
                    date = null;

                    mDisplayDate.setText(getResources().getString(R.string.set_date));
                    Toast.makeText(getApplicationContext(), "Please enter another date - the one you" +
                            " entered already exists ", Toast.LENGTH_LONG).show();
                }

            }
        };
    }

    public boolean checkForDuplicateDates(String date) {

        String query = "SELECT " + DB.DBContract.KEY_DATE_COLUMN + " FROM " +
                DB.MySQLOpenLiteHelper.MACRO_TOTAL_TABLE;

        Cursor cursor = DB.db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String existingDate =
                    cursor.getString(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_DATE_COLUMN));
            if (date.equals(existingDate)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        EditText foodItemEditText = findViewById(R.id.fooditemEditText);
        EditText proteinEditText = findViewById(R.id.proteinEditText);
        EditText fatsEditText = findViewById(R.id.fatEditText);
        EditText carbsEditText = findViewById(R.id.carbsEditText);

        //Prevent user from leaving fields empty:
        if (foodItemEditText.getText().toString().isEmpty() ||
                carbsEditText.getText().toString().isEmpty() ||
                proteinEditText.getText().toString().isEmpty() ||
                fatsEditText.getText().toString().isEmpty() ||
                date == null) {
            Toast.makeText(this, "Please do not leave fields empty", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        String itemName = foodItemEditText.getText().toString();
        Float carbs = Float.parseFloat(carbsEditText.getText().toString());
        Float protein = Float.parseFloat(proteinEditText.getText().toString());
        Float fats = Float.parseFloat(fatsEditText.getText().toString());

        int calories = Math.round((carbs * 4) + (protein * 4) + (fats * 9));

        Day day = new Day(date);

        day.addItem(new Item(itemName, calories, carbs, protein, fats));

        putIntoDatabase(day);

    }

    public void putIntoDatabase(Day day) {


        //Insert Day values into both MacroTable and MacroTotalTable
        //Insert Date into DateTable (referenced by MacroTable)
        ContentValues cv = new ContentValues();
        ContentValues cv2 = new ContentValues();
        ContentValues cv3 = new ContentValues();

        for (Item item : day.getItems()) {
            cv.put(DB.DBContract.KEY_CARBS_COLUMN, item.getCarbs());
            cv2.put(DB.DBContract.KEY_CARBS_COLUMN, item.getCarbs());
            cv.put(DB.DBContract.KEY_PROTEIN_COLUMN, item.getProtein());
            cv2.put(DB.DBContract.KEY_PROTEIN_COLUMN, item.getProtein());
            cv.put(DB.DBContract.KEY_FATS_COLUMN, item.getFats());
            cv2.put(DB.DBContract.KEY_FATS_COLUMN, item.getFats());
            cv.put(DB.DBContract.KEY_CALORIE_COLUMN, item.getCalories());
            cv2.put(DB.DBContract.KEY_CALORIE_COLUMN, item.getCalories());
            cv.put(DB.DBContract.KEY_FOOD_NAME_COLUMN, item.getItemName());
        }

        cv2.put(DB.DBContract.KEY_DATE_COLUMN, day.getDate());
        cv3.put(DB.DBContract.KEY_DATE_COLUMN, day.getDate());

        //query the DB to see what the largest foreign key value in MacrosTable is then add 1
        //to ensure we store a unique foreign key value for this new Item in MacroTable

        Cursor cursor = DB.db.rawQuery("SELECT " + DB.DBContract.KEY_ID_FK +
                " FROM " + DB.mySQLOpenLiteHelper.MACROS_TABLE, null);

        int highestSoFar = 0;
        int result;

        while (cursor.moveToNext()) {
            result = cursor.getInt(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_ID_FK));
            if (result > highestSoFar) {
                highestSoFar = result;
            }
        }

        int fkValue = highestSoFar + 1;

        cv.put(DB.DBContract.KEY_ID_FK, fkValue);

        DB.db.insert(DB.MySQLOpenLiteHelper.MACROS_TABLE, null, cv);
        DB.db.insert(DB.MySQLOpenLiteHelper.MACRO_TOTAL_TABLE, null, cv2);
        DB.db.insert(DB.MySQLOpenLiteHelper.DATE_TABLE, null, cv3);

        cursor.close();

        finish();
        Intent results = new Intent(AddItem.this, Log_Food_Details.class);
        startActivity(results);
    }
}
