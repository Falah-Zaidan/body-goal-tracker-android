package com.bodygoaltracker.Utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.bodygoaltracker.Activities.Log_Food_Details;
import com.bodygoaltracker.DB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class DBUtil extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 123;
    //private final Context context;
    private final Log_Food_Details activity;

    public DBUtil(Log_Food_Details activity) { //MainActivity mainActivity
        this.activity = activity;
    }

    public void exportDatabase() {
        if (ActivityCompat
                .checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_REQUEST_CODE);
            return;
        }
        try {
            File data = Environment.getDataDirectory();
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + activity.getPackageName() + "//databases//" + DB.MySQLOpenLiteHelper.DB_NAME;
                String backupDBPath = "/soda/" + System.currentTimeMillis() + ".db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
                if (!backupDB.getParentFile().exists()) {
                    backupDB.getParentFile().mkdirs();
                }
                if (currentDB.exists()) {
                    FileChannel src = null;
                    FileChannel dst = null;
                    try {
                        src = new FileInputStream(currentDB).getChannel();
                        dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                    } finally {
                        if (src != null) {
                            src.close();
                        }
                        if (dst != null) {
                            dst.close();
                        }
                    }
                }
            } else {
                Log.e("TAG", "exportDatabase: ");
            }
        } catch (Exception exception) {
            Log.e("TAG", "exportDatabase: ");
        }
    }
}

