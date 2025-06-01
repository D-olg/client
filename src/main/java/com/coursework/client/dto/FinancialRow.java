package com.coursework.client.dto;

import javafx.beans.property.*;

public class FinancialRow {
    private final IntegerProperty year = new SimpleIntegerProperty();
    private final DoubleProperty equity = new SimpleDoubleProperty();
    private final DoubleProperty netIncome = new SimpleDoubleProperty();

    public FinancialRow() {}

    public FinancialRow(int year, double equity, double netIncome) {
        this.year.set(year);
        this.equity.set(equity);
        this.netIncome.set(netIncome);
    }

    public int getYear() { return year.get(); }
    public void setYear(int value) { year.set(value); }
    public IntegerProperty yearProperty() { return year; }

    public double getEquity() { return equity.get(); }
    public void setEquity(double value) { equity.set(value); }
    public DoubleProperty equityProperty() { return equity; }

    public double getNetIncome() { return netIncome.get(); }
    public void setNetIncome(double value) { netIncome.set(value); }
    public DoubleProperty netIncomeProperty() { return netIncome; }
}
