package com.coursework.client.dto;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

public class DiscountRateRow {
    private IntegerProperty year;
    private DoubleProperty rate;

    public DiscountRateRow() {
    }

    public DiscountRateRow(IntegerProperty year, DoubleProperty rate) {
        this.year = year;
        this.rate = rate;
    }

    public int getYear() {
        return year.get();
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public double getRate() {
        return rate.get();
    }

    public DoubleProperty rateProperty() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate.set(rate);
    }
}
