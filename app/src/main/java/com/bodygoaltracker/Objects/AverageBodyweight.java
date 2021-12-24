package com.bodygoaltracker.Objects;

public class AverageBodyweight {
    private double bodyweight;
    private String date;

    public AverageBodyweight(double bodyweight, String date) {
        this.bodyweight = bodyweight;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public double getBodyweight() {
        return bodyweight;
    }
}
