package com.coursework.client.dto;

import javafx.beans.property.*;

public class CombinedFinancialRow {
    private final IntegerProperty year = new SimpleIntegerProperty();
    private final DoubleProperty equity = new SimpleDoubleProperty();
    private final DoubleProperty netIncome = new SimpleDoubleProperty();
    private final DoubleProperty discountRate = new SimpleDoubleProperty();

    public CombinedFinancialRow() {}

    public IntegerProperty yearProperty() { return year; }
    public DoubleProperty equityProperty() { return equity; }
    public DoubleProperty netIncomeProperty() { return netIncome; }
    public DoubleProperty discountRateProperty() { return discountRate; }

    public int getYear() { return year.get(); }
    public void setYear(int value) { year.set(value); }

    public double getEquity() { return equity.get(); }
    public void setEquity(double value) { equity.set(value); }

    public double getNetIncome() { return netIncome.get(); }
    public void setNetIncome(double value) { netIncome.set(value); }

    public double getDiscountRate() { return discountRate.get(); }
    public void setDiscountRate(double value) { discountRate.set(value); }
}
