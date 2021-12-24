package com.bodygoaltracker.Objects;

public class BodyWeight {
    private double bodyweight;
    private String date;

    public BodyWeight(double bodyweight, String date) {
        this.bodyweight = bodyweight;
        this.date = date;
    }

    public double getBodyweight() {
        return bodyweight;
    }

    public String getDate() {
        return date;
    }

}
