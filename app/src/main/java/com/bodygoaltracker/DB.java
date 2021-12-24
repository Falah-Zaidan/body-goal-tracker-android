package com.bodygoaltracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {

    public static DB.MySQLOpenLiteHelper mySQLOpenLiteHelper;
    public static SQLiteDatabase db;

    public static void init(Context context) {
        if (mySQLOpenLiteHelper == null) {
            mySQLOpenLiteHelper = new DB.MySQLOpenLiteHelper(context, DB.MySQLOpenLiteHelper.DB_NAME,
                    null, 1);
            db = mySQLOpenLiteHelper.getWritableDatabase();
        }
    }

    public static class DBContract {

        public static final String KEY_ID =
                "_id";
        public static final String KEY_CARBS_COLUMN =
                "CARBS_COLUMN";
        public static final String KEY_PROTEIN_COLUMN =
                "PROTEIN_COLUMN";
        public static final String KEY_CALORIE_COLUMN =
                "CALORIE_COLUMN";
        public static final String KEY_FOOD_NAME_COLUMN =
                "FOOD_NAME_COLUMN";
        public static final String KEY_FATS_COLUMN =
                "FATS_COLUMN";
        public static final String KEY_DATE_COLUMN =
                "DATE_COLUMN";
        public static final String KEY_DATE_ID =
                "_id";
        public static final String KEY_ID_FK =
                "fk_id";
        public static final String KEY_IMAGE_NAME =
                "IMAGE_NAME";
        public static final String KEY_IMAGE =
                "IMAGE";
        public static final String KEY_BODYWEIGHT_COLUMN =
                "BODYWEIGHT_COLUMN";
        public static final String KEY_EXTRA_INFO_COLUMN =
                "EXTRA_INFO_COLUMN";

    }

    public static class MySQLOpenLiteHelper extends SQLiteOpenHelper {

        public static final String DB_NAME = "Macros.db";
        public static final String MACROS_TABLE = "MacrosTable";
        public static final String DATE_TABLE = "DateTable";
        public static final String MACRO_TOTAL_TABLE = "MacroTotalTable";
        public static final String IMAGES_TABLE = "ImagesTable";
        public static final String BODYWEIGHT_TABLE = "BodyweightTable";
        public static final int DB_VERSION = 1;

        public static final String CREATE_FOOD_ITEM_TABLE =
                "create table " + MACROS_TABLE + "(" +
                        DBContract.KEY_ID + " integer primary key autoincrement, " +
                        DBContract.KEY_CARBS_COLUMN + " float, " +
                        DBContract.KEY_PROTEIN_COLUMN + " float, " +
                        DBContract.KEY_CALORIE_COLUMN + " integer, " +
                        DBContract.KEY_FATS_COLUMN + " float, " +
                        DBContract.KEY_FOOD_NAME_COLUMN + " text, " +
                        DBContract.KEY_ID_FK + " integer, " +
                        "FOREIGN KEY (fk_id) REFERENCES DateTable(_id));";

        public static final String CREATE_MACRO_TOTAL_TABLE =
                "create table " + MACRO_TOTAL_TABLE + "(" +
                        DBContract.KEY_ID + " integer primary key autoincrement, " +
                        DBContract.KEY_CARBS_COLUMN + " float, " +
                        DBContract.KEY_PROTEIN_COLUMN + " float, " +
                        DBContract.KEY_CALORIE_COLUMN + " integer, " +
                        DBContract.KEY_FATS_COLUMN + " float, " +
                        DBContract.KEY_DATE_COLUMN + " text);";

        public static final String CREATE_DATE_TABLE =
                "create table " + DATE_TABLE + "(" +
                        DBContract.KEY_DATE_ID + " integer primary key autoincrement, " +
                        DBContract.KEY_DATE_COLUMN + " text);";

        public static final String CREATE_IMAGE_TABLE =
                "create table " + IMAGES_TABLE + "(" +
                        DBContract.KEY_ID + " integer primary key autoincrement, " +
                        DBContract.KEY_IMAGE_NAME + " text, " +
                        DBContract.KEY_ID_FK + " integer, " +
                        DBContract.KEY_DATE_COLUMN + " text, " +
                        DBContract.KEY_EXTRA_INFO_COLUMN + " text, " +
                        DBContract.KEY_IMAGE + " blob);";

        public static final String CREATE_BODYWEIGHT_TABLE =
                "create table " + BODYWEIGHT_TABLE + "(" +
                        DBContract.KEY_ID + " integer primary key autoincrement, " +
                        DBContract.KEY_DATE_COLUMN + " text, " +
                        DBContract.KEY_BODYWEIGHT_COLUMN + " float);";

        public MySQLOpenLiteHelper(@androidx.annotation.Nullable Context context, @androidx.annotation.Nullable String name, @androidx.annotation.Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_FOOD_ITEM_TABLE);
            db.execSQL(CREATE_DATE_TABLE);
            db.execSQL(CREATE_MACRO_TOTAL_TABLE);
            db.execSQL(CREATE_IMAGE_TABLE);
            db.execSQL(CREATE_BODYWEIGHT_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +
                    MACROS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " +
                    DATE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " +
                    MACRO_TOTAL_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " +
                    IMAGES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " +
                    BODYWEIGHT_TABLE);
            onCreate(db);
        }


    }

}
