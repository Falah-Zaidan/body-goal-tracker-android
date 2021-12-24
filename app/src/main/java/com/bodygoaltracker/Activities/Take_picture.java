package com.bodygoaltracker.Activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bodygoaltracker.DB;
import com.bodygoaltracker.R;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


public class Take_picture extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG";
    private static final int CAMERA_REQUEST_CODE = 3;
    TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String date;
    Button addPictureBtn;
    Button donePictureBtn;
    ImageView imgView;
    EditText extraInfo;
    String extraInformation;
    Bitmap picture;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_take_picture);

        DB.init(this);
        initialiseViews();

        if (checkPermissionsArray(Permissions.PERMISSIONS)) {
        } else {
            verifyPermissions(Permissions.PERMISSIONS);
        }
    }

    public void initialiseViews() {
        //Toolbar:
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Add picture");
        ab.setDisplayHomeAsUpEnabled(true);

        //Done button:
        donePictureBtn = findViewById(R.id.doneButton);
        donePictureBtn.setOnClickListener(this);

        //Add picture Button:
        addPictureBtn = findViewById(R.id.addPictureBtn);
        addPictureBtn.setOnClickListener(this);

        //Extra info:
        extraInfo = findViewById(R.id.extraNotesEditText);

        //Set date:
        mDisplayDate =
                (TextView) findViewById(R.id.setdatetextview);

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
                if (!checkForDuplicateDates(date)) {
                    mDisplayDate.setTextColor(Color.rgb(0, 0, 0));
                    mDisplayDate.setText(date);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addPictureBtn:
                takePictureIntent();
                break;
            case R.id.doneButton:
                clickedDone();
                break;
            case R.id.setdatetextview:
                setDate();
                break;
            default:
                break;
        }
    }

    public void clickedDone() {

        if (extraInfo.getText().toString().isEmpty() ||
                date == null ||
                picture == null) {
            Toast.makeText(this, "Please do not leave fields empty", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        extraInformation = extraInfo.getText().toString();
        //Put extra information into DB?

        insertImgAndText("picture1", picture, extraInformation);
        finish();
        Intent intent = new Intent(Take_picture.this, Log_Progress.class);
        intent.putExtra("Frag_Tab_Number", 1);
        startActivity(intent);

    }

    public boolean checkForDuplicateDates(String date) {

        //Query the DB for all dates
        String query = "SELECT " + DB.DBContract.KEY_DATE_COLUMN + " FROM " +
                DB.mySQLOpenLiteHelper.IMAGES_TABLE;
        Cursor cursor = DB.db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            if (date.equals(cursor.getString(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_DATE_COLUMN)))) {
                return true;
            }
        }

        return false;
    }

    public void setDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                Take_picture.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void takePictureIntent() {
        if (checkPermissions(Permissions.CAMERA_PERMISSION[0])) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Log.d(TAG, "takePictureIntent: in");
            Intent intent = new Intent(getApplicationContext(), Log_Progress.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {

            picture = (Bitmap) data.getExtras().get("data");
            displayImage();
        }
    }


    public void displayImage() {

        imgView = findViewById(R.id.imageView);
        imgView.setImageBitmap(picture);

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public void insertImgAndText(String name, Bitmap image, String extraInformation) {

        //TODO: query the DB to get the PK value for the same date row as imageTable then insert into that row
        int pkValue = findPkValue();

        byte[] data = getBitmapAsByteArray(image);

        ContentValues cv = new ContentValues();
        cv.put(DB.DBContract.KEY_IMAGE_NAME, name);
        cv.put(DB.DBContract.KEY_EXTRA_INFO_COLUMN, extraInformation);
        cv.put(DB.DBContract.KEY_DATE_COLUMN, date);
        //cv.put(DB.DBContract.KEY_ID_FK, pkValue);
        cv.put(DB.DBContract.KEY_IMAGE, data);
        DB.db.insert(DB.MySQLOpenLiteHelper.IMAGES_TABLE, null, cv);

    }

    public boolean checkPermissions(String permission) {
        Log.d(TAG, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(Take_picture.this, permission);

        Log.d(TAG, "permission request: " + permissionRequest);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }

    public int findPkValue() {

        String query = "SELECT DATE_COLUMN, _id FROM DateTable WHERE DATE_COLUMN=?;";
        Cursor cursor = DB.db.rawQuery(query, new String[]{date});

        int pkValue = -1;

        while (cursor.moveToNext()) {
            pkValue = cursor.getInt(cursor.getColumnIndexOrThrow(DB.DBContract.KEY_ID));
        }

        return pkValue;
    }

    public boolean checkPermissionsArray(String[] permissions) {
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for (int i = 0; i < permissions.length; i++) {
            String check = permissions[i];
            if (!checkPermissions(check)) {
                return false;
            }
        }
        return true;
    }

    public void verifyPermissions(String[] permissions) {
        Log.d(TAG, "verifyPermissions: verifying permissions");

        ActivityCompat.requestPermissions(
                Take_picture.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }
}

