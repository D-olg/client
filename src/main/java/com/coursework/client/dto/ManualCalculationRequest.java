package com.coursework.client.dto;

import java.util.List;

public class ManualCalculationRequest {
    private String description;
    private List<CombinedFinancialRow> data;

    public ManualCalculationRequest() {
    }

    public ManualCalculationRequest(String description, List<CombinedFinancialRow> data) {
        this.description = description;
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CombinedFinancialRow> getData() {
        return data;
    }

    public void setData(List<CombinedFinancialRow> data) {
        this.data = data;
    }
}
