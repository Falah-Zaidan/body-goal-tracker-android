package com.bodygoaltracker.Objects;

import java.util.ArrayList;

public class Day {

    private ArrayList<Item> mItems;
    private String mDate;


    public Day(String date) {
        this.mDate = date;
        mItems = new ArrayList<>();
    }

    public String getDate() {

        return mDate;
    }

    public ArrayList<Item> getItems() {
        return mItems;
    }

    public void addItem(Item item) {
        mItems.add(item);
    }

}
