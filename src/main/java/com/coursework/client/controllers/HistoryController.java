package com.coursework.client.controllers;

import com.coursework.client.dto.ValuationDTO;
import com.coursework.client.models.DiscountRate;
import com.coursework.client.models.FinancialData;
import com.coursework.client.models.Valuation;
import com.coursework.client.utils.ApiClient;
import com.coursework.client.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Tooltip;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoryController {

    @FXML
    private LineChart<String, Number> valuationHistoryChart; // изменен на LineChart

    @FXML
    private LineChart<Number, Number> equityChart;

    @FXML
    private LineChart<Number, Number> netIncomeChart;

    @FXML
    private LineChart<Number, Number> discountRateChart;

    private final ApiClient apiClient = new ApiClient();

    @FXML
    public void initialize() {
        loadValuationHistory();
    }

    public void loadValuationHistory() {
        List<ValuationDTO> valuations = apiClient.getValuations(); // Метод с новым DTO
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("История оценки");

        for (ValuationDTO val : valuations) {
            String date = val.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            XYChart.Data<String, Number> point = new XYChart.Data<>(date, val.getValuation());
            series.getData().add(point);

            point.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    setupPointInteraction(newNode, val);
                }
            });
        }

        valuationHistoryChart.getData().clear();
        valuationHistoryChart.getData().add(series);
    }

    private void setupPointInteraction(Node node, ValuationDTO valuation) {
        Tooltip tooltip = new Tooltip("Оценка: " + valuation.getValuation() + "\n" + valuation.getCreatedAt());
        Tooltip.install(node, tooltip);

        node.setOnMouseClicked((MouseEvent event) -> {
            if (valuation.getScenario() != null) {
                System.out.println("Выбран сценарий с ID: " + valuation.getScenario().getId());
            }
            loadScenarioDetails(valuation.getScenario() != null ? valuation.getScenario().getId() : null);
        });
    }

    private void loadScenarioDetails(Integer scenarioId) {
        System.out.println("Загружаем сценарий: " + scenarioId);

        List<FinancialData> financialData = apiClient.getFinancialDataByScenario(scenarioId);
        List<DiscountRate> discountRates = apiClient.getDiscountRatesByScenario(scenarioId);

        System.out.println("Получено финансовых данных: " + financialData.size());
        System.out.println("Получено ставок дисконтирования: " + discountRates.size());

        equityChart.getData().clear();
        netIncomeChart.getData().clear();
        discountRateChart.getData().clear();

        XYChart.Series<Number, Number> equitySeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> incomeSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> rateSeries = new XYChart.Series<>();

        equitySeries.setName("Капитал");
        incomeSeries.setName("Прибыль");
        rateSeries.setName("Ставка %");

        // Подсказки для точек equity
        for (XYChart.Data<Number, Number> dataPoint : equitySeries.getData()) {
            Node node = dataPoint.getNode();
            if (node != null) {
                Tooltip tooltip = new Tooltip("Год: " + dataPoint.getXValue() + "\nКапитал: " + dataPoint.getYValue());
                Tooltip.install(node, tooltip);
            }
        }

        // Подсказки для точек netIncome
        for (XYChart.Data<Number, Number> dataPoint : incomeSeries.getData()) {
            Node node = dataPoint.getNode();
            if (node != null) {
                Tooltip tooltip = new Tooltip("Год: " + dataPoint.getXValue() + "\nПрибыль: " + dataPoint.getYValue());
                Tooltip.install(node, tooltip);
            }
        }

        // Подсказки для точек discountRate
        for (XYChart.Data<Number, Number> dataPoint : rateSeries.getData()) {
            Node node = dataPoint.getNode();
            if (node != null) {
                Tooltip tooltip = new Tooltip("Год: " + dataPoint.getXValue() + "\nСтавка: " + dataPoint.getYValue());
                Tooltip.install(node, tooltip);
            }
        }


        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (FinancialData data : financialData) {
            System.out.printf("Year: %d | Equity: %s | NetIncome: %s%n",
                    data.getYear(), data.getEquity(), data.getNetIncome());

            equitySeries.getData().add(new XYChart.Data<>(data.getYear(), data.getEquity()));
            incomeSeries.getData().add(new XYChart.Data<>(data.getYear(), data.getNetIncome()));

            minY = Math.min(minY, data.getEquity());
            maxY = Math.max(maxY, data.getEquity());
            minY = Math.min(minY, data.getNetIncome());
            maxY = Math.max(maxY, data.getNetIncome());
        }

        for (DiscountRate rate : discountRates) {
            System.out.printf("Year: %d | Rate: %s%n", rate.getYear(), rate.getRate());
            rateSeries.getData().add(new XYChart.Data<>(rate.getYear(), rate.getRate()));

            minY = Math.min(minY, rate.getRate());
            maxY = Math.max(maxY, rate.getRate());
        }

        // Уточняем минимальный и максимальный год
        int minYear = Integer.MAX_VALUE;
        int maxYear = Integer.MIN_VALUE;

        for (FinancialData data : financialData) {
            minYear = Math.min(minYear, data.getYear());
            maxYear = Math.max(maxYear, data.getYear());
        }
        for (DiscountRate rate : discountRates) {
            minYear = Math.min(minYear, rate.getYear());
            maxYear = Math.max(maxYear, rate.getYear());
        }

        // Настройка осей X
        for (LineChart<Number, Number> chart : List.of(equityChart, netIncomeChart, discountRateChart)) {
            NumberAxis xAxis = (NumberAxis) chart.getXAxis();
            xAxis.setAutoRanging(false);
            xAxis.setLowerBound(minYear - 1);       // немного до
            xAxis.setUpperBound(maxYear + 1);       // немного после
            xAxis.setTickUnit(1);                   // шаг = 1 год
        }

        equityChart.getData().add(equitySeries);
        netIncomeChart.getData().add(incomeSeries);
        discountRateChart.getData().add(rateSeries);
    }
    @FXML
    public void onBackToMenu(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/userActions.fxml");
    }
}
