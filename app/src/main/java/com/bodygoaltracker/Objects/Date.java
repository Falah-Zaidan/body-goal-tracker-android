package com.bodygoaltracker.Objects;

import java.util.Objects;

import androidx.annotation.Nullable;

public class Date {

    private String date;

    public Date(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Date dateObject = (Date) obj;
        return date.equals(dateObject.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
