package com.bodygoaltracker.Objects;

public class Item {

    private String mItemName;
    private int mCalories;
    private Float mCarbs;
    private Float mProtein;
    private Float mFats;


    public Item(String itemName, int calories, Float carbs, Float protein,
                Float fats) {

        this.mItemName = itemName;
        this.mCalories = calories;
        this.mCarbs = carbs;
        this.mProtein = protein;
        this.mFats = fats;
    }

    public String getItemName() {
        return mItemName;
    }

    public int getCalories() {
        return mCalories;
    }

    public Float getCarbs() {
        return mCarbs;
    }

    public Float getProtein() {
        return mProtein;
    }

    public Float getFats() {
        return mFats;
    }
}
