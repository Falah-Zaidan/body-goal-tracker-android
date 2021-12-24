package com.bodygoaltracker.Objects;

import android.graphics.Bitmap;

public class Image {

    private String date;
    private transient Bitmap bitmap;
    private String extraInfo;

    public Image(String date, Bitmap bitmap, String extraInfo) {
        this.date = date;
        this.bitmap = bitmap;
        this.extraInfo = extraInfo;
    }

    public String getDate() {
        return date;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

}
