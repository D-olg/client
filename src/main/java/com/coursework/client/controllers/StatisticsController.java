package com.coursework.client.controllers;

import com.coursework.client.utils.ApiClient;
import com.coursework.client.models.StatisticsData;
import com.coursework.client.models.UserStats;
import com.coursework.client.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatisticsController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private Hyperlink usersCountLink;

    @FXML
    private Label activeTodayLabel;

    @FXML
    private Label totalCalculationsLabel;

    @FXML
    private Label yesterdayCalculationsLabel;

    @FXML
    private Label average7DaysLabel;

    @FXML
    private BarChart<String, Number> weeklyChart;

    @FXML
    private LineChart<String, Number> dailyChart;

    @FXML
    private NumberAxis weeklyChartYAxis;

    @FXML
    private NumberAxis dailyChartYAxis;

    private Timer refreshTimer;
    private ContextMenu usersMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupChartStyle();
        loadStatistics();
        setupUsersMenu();
        setupAutoRefresh();
    }

    private void setupChartStyle() {
        // Отключаем анимацию
        weeklyChart.setAnimated(false);
        dailyChart.setAnimated(false);

        // Настраиваем оси для целых чисел
        weeklyChartYAxis.setTickUnit(1);
        weeklyChartYAxis.setMinorTickVisible(false);
        dailyChartYAxis.setTickUnit(1);
        dailyChartYAxis.setMinorTickVisible(false);
    }

    private void setupUsersMenu() {
        usersMenu = new ContextMenu();
        usersCountLink.setOnMouseClicked(event -> {
            try {
                List<UserStats> usersStats = ApiClient.getUsersStats();
                usersMenu.getItems().clear();
                
                for (UserStats stats : usersStats) {
                    MenuItem item = new MenuItem(stats.toString());
                    usersMenu.getItems().add(item);
                }
                
                usersMenu.show(usersCountLink, event.getScreenX(), event.getScreenY());
            } catch (Exception e) {
                System.err.println("Error loading users statistics: " + e.getMessage());
            }
        });
    }

    private void setupAutoRefresh() {
        refreshTimer = new Timer(true);
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> loadStatistics());
            }
        }, 30000, 30000); // Обновление каждые 30 секунд
    }

    private void loadStatistics() {
        try {
            StatisticsData stats = ApiClient.getStatistics();
            updateUI(stats);
        } catch (Exception e) {
            System.err.println("Error loading statistics: " + e.getMessage());
        }
    }

    private void updateUI(StatisticsData stats) {
        usersCountLink.setText(String.valueOf(stats.getTotalUsers()));
        activeTodayLabel.setText(String.valueOf(stats.getActiveToday()));
        totalCalculationsLabel.setText(String.valueOf(stats.getTotalCalculations()));
        yesterdayCalculationsLabel.setText(String.valueOf(stats.getYesterdayCalculations()));
        average7DaysLabel.setText(String.format("%.1f", stats.getAverage7Days()));

        updateWeeklyChart(stats);
        updateDailyChart(stats);
    }

    private void updateWeeklyChart(StatisticsData stats) {
        weeklyChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        stats.getCalculationsByDay().forEach((day, count) -> {
            XYChart.Data<String, Number> data = new XYChart.Data<>(day, count);
            series.getData().add(data);
        });

        weeklyChart.getData().add(series);

        // Применяем стили к столбцам
        series.getData().forEach(data -> {
            data.getNode().setStyle("-fx-bar-fill: #4e9a06;");
        });
    }

    private void updateDailyChart(StatisticsData stats) {
        dailyChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Создаем данные за последние 7 дней
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
        LocalDate today = LocalDate.now();
        
        // Получаем данные по дням недели и преобразуем их в формат для графика
        Map<String, Integer> dailyData = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String formattedDate = date.format(formatter);
            dailyData.put(formattedDate, 0); // Инициализируем нулевыми значениями
        }

        // Заполняем реальными данными
        stats.getCalculationsByDay().forEach((day, count) -> {
            // Здесь мы используем данные из недельной статистики
            // В реальном приложении лучше получать отдельные данные по дням
            dailyData.put(today.minusDays(getDayOffset(day)).format(formatter), count);
        });

        // Добавляем данные в график
        dailyData.forEach((date, count) -> {
            XYChart.Data<String, Number> data = new XYChart.Data<>(date, count);
            series.getData().add(data);
        });

        dailyChart.getData().add(series);

        // Применяем стили к линии и точкам
        series.getNode().setStyle("-fx-stroke: #73d216; -fx-stroke-width: 2px;");
        series.getData().forEach(data -> {
            data.getNode().setStyle(
                "-fx-background-color: #4e9a06, white; " +
                "-fx-background-insets: 0, 2; " +
                "-fx-background-radius: 5px; " +
                "-fx-padding: 5px;"
            );
        });
    }

    private int getDayOffset(String dayName) {
        Map<String, Integer> dayOffsets = new HashMap<>();
        dayOffsets.put("Понедельник", 6);
        dayOffsets.put("Вторник", 5);
        dayOffsets.put("Среда", 4);
        dayOffsets.put("Четверг", 3);
        dayOffsets.put("Пятница", 2);
        dayOffsets.put("Суббота", 1);
        dayOffsets.put("Воскресенье", 0);
        return dayOffsets.getOrDefault(dayName, 0);
    }

    @FXML
    public void onBackToMenu(ActionEvent event) {
        SceneNavigator.switchScene((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "/com/coursework/client/adminActions.fxml");
    }
}
