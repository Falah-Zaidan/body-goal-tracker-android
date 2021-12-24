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
import com.bodygoaltracker.R;

import java.util.Calendar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BWInput extends AppCompatActivity implements View.OnClickListener {

    TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String date;
    EditText editText;
    Float bodyWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_bwinput);

        DB.init(this);
        initialiseViews();


    }

    public void initialiseViews() {

        //Toolbar:
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Record Bodyweight");
        ab.setDisplayHomeAsUpEnabled(true);

        //Done button:
        Button doneButton = findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);

        //Set Date:
        mDisplayDate =
                (TextView) findViewById(R.id.dateTextView);

        mDisplayDate.setOnClickListener(this);

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
                Log.d("TAG", "This is the value of date from BWInput: " + date);
                if (!checkForDuplicateDates(date)) {
                    mDisplayDate.setText(date);
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

        //EditText for BW input
        editText = findViewById(R.id.inputBWEditText);

    }

    public boolean checkForDuplicateDates(String date) {

        //Query the DB for all dates
        String query = "SELECT " + DB.DBContract.KEY_DATE_COLUMN + " FROM " +
                DB.mySQLOpenLiteHelper.BODYWEIGHT_TABLE;
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

        switch (v.getId()) {
            case R.id.doneButton:
                if (editText.getText().toString().isEmpty() ||
                        date == null) {
                    Toast.makeText(this, "Please do not leave fields empty", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                bodyWeight = Float.parseFloat(editText.getText().toString());

                ContentValues cv = new ContentValues();

                date = mDisplayDate.getText().toString();

                cv.put(DB.DBContract.KEY_BODYWEIGHT_COLUMN, bodyWeight);
                cv.put(DB.DBContract.KEY_DATE_COLUMN, date);

                DB.db.insert(DB.MySQLOpenLiteHelper.BODYWEIGHT_TABLE, null, cv);

                finish();
                Intent intent = new Intent(BWInput.this, Log_Progress.class);
                startActivity(intent);
                break;
            case R.id.dateTextView:
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        BWInput.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            default:
                break;
        }

    }
}
