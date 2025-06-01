package com.coursework.client.dto;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ValuationDTO {
    private BigDecimal valuation;
    private String recommendation;
    private LocalDateTime createdAt;
    private ScenarioDTO scenario;
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public ValuationDTO() {
    }

    public ValuationDTO(BigDecimal valuation, String recommendation, LocalDateTime createdAt, ScenarioDTO scenario) {
        this.valuation = valuation;
        this.recommendation = recommendation;
        this.createdAt = createdAt;
        this.scenario = scenario;
    }

    public BigDecimal getValuation() {
        return valuation;
    }

    public void setValuation(BigDecimal valuation) {
        this.valuation = valuation;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ScenarioDTO getScenario() {
        return scenario;
    }

    public void setScenario(ScenarioDTO scenario) {
        this.scenario = scenario;
    }

    // Метод для получения и установки значения чекбокса
    public BooleanProperty selectedProperty() {
        return selected;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public String getCreatedAtFormatted() {
        return createdAt != null ? createdAt.toString() : "Неизвестно";
    }

    public String getValuationFormatted() {
        return valuation != null ? valuation.setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "Неизвестно";
    }
}
