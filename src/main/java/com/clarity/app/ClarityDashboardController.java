package com.clarity.app;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClarityDashboardController extends BaseController {

    @FXML private Label welcomeLabel;
    @FXML private Label dateLabel;
    @FXML private Label newTaskCount;
    @FXML private Label overdueCount;
    @FXML private GridPane miniCalendar;
    @FXML private BarChart<String, Number> progressChart;
    @FXML private VBox overdueTaskList;
    @FXML private HBox dashboardNavItem;

    private String userName = "Christy";
    private int newTasks = 5;
    private int overdueTasks = 3;
    private List<DashboardTask> overdueTasksList;

    @Override
    public void initialize() {
        updateWelcomeMessage();
        updateDate();
        updateStats();
        generateMiniCalendar();
        loadProgressChart();
        loadOverdueTasks();

        System.out.println("ClarityDashboardController initialized");
    }

    private void updateWelcomeMessage() {
        welcomeLabel.setText("Hello There " + userName + "!");
    }

    private void updateDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        dateLabel.setText(today.format(formatter));
    }

    private void updateStats() {
        newTaskCount.setText(String.valueOf(newTasks));
        overdueCount.setText(String.valueOf(overdueTasks));
    }

    private void generateMiniCalendar() {
        miniCalendar.getChildren().clear();

        String[] dayHeaders = {"S", "M", "T", "W", "T", "F", "S"};
        for (int i = 0; i < dayHeaders.length; i++) {
            Label header = new Label(dayHeaders[i]);
            header.getStyleClass().add("cal-day-header");
            miniCalendar.add(header, i, 0);
        }

        int[] days = {
                0, 1, 2, 3, 4, 5, 6,
                7, 8, 9, 10, 11, 12, 13,
                14, 15, 16, 17, 18, 19, 20,
                21, 22, 23, 24, 25, 26, 27,
                28, 29, 30, 0, 0, 0, 0
        };

        List<Integer> eventDays = List.of(16, 25, 26);
        List<Integer> overdueDays = List.of(14, 21, 28, 29);

        int row = 1;
        int col = 0;

        for (int day : days) {
            if (day > 0) {
                Label dayLabel = new Label(String.valueOf(day));
                dayLabel.getStyleClass().add("cal-day");

                if (eventDays.contains(day)) {
                    dayLabel.getStyleClass().add("cal-day-event");
                } else if (overdueDays.contains(day)) {
                    dayLabel.getStyleClass().add("cal-day-overdue");
                }

                miniCalendar.add(dayLabel, col, row);
            }

            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }
    }

    private void loadProgressChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tasks Completed");

        series.getData().add(new XYChart.Data<>("12", 20));
        series.getData().add(new XYChart.Data<>("13", 23));
        series.getData().add(new XYChart.Data<>("14", 8));
        series.getData().add(new XYChart.Data<>("15", 4));
        series.getData().add(new XYChart.Data<>("16", 0));
        series.getData().add(new XYChart.Data<>("17", 0));
        series.getData().add(new XYChart.Data<>("18", 0));
        series.getData().add(new XYChart.Data<>("19", 2));
        series.getData().add(new XYChart.Data<>("20", 0));
        series.getData().add(new XYChart.Data<>("21", 0));
        series.getData().add(new XYChart.Data<>("22", 0));
        series.getData().add(new XYChart.Data<>("23", 0));
        series.getData().add(new XYChart.Data<>("24", 0));
        series.getData().add(new XYChart.Data<>("25", 5));
        series.getData().add(new XYChart.Data<>("26", 0));
        series.getData().add(new XYChart.Data<>("27", 0));
        series.getData().add(new XYChart.Data<>("28", 0));
        series.getData().add(new XYChart.Data<>("29", 0));

        progressChart.getData().add(series);
        progressChart.setLegendVisible(false);
    }

    private void loadOverdueTasks() {
        overdueTasksList = new ArrayList<>();

        overdueTasksList.add(new DashboardTask(
                "Boost Client's Feedback on Bing",
                "Sept 13",
                "3h",
                "Overdue"
        ));

        overdueTasksList.add(new DashboardTask(
                "Identifying Target Keywords",
                "Sept 13",
                "5h",
                "Overdue"
        ));

        overdueTasksList.add(new DashboardTask(
                "Apply Schema Markup",
                "Sept 14",
                "5h",
                "Overdue"
        ));

        displayOverdueTasks();
    }

    private void displayOverdueTasks() {
        overdueTaskList.getChildren().clear();

        for (DashboardTask task : overdueTasksList) {
            HBox taskItem = createTaskItem(task);
            overdueTaskList.getChildren().add(taskItem);
        }
    }

    private HBox createTaskItem(DashboardTask task) {
        HBox taskItem = new HBox(8);
        taskItem.getStyleClass().add("dashboard-task-item");
        taskItem.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        RadioButton radio = new RadioButton();
        radio.getStyleClass().add("task-radio");

        Label nameLabel = new Label(task.getName());
        nameLabel.getStyleClass().add("task-name-label");
        HBox.setHgrow(nameLabel, javafx.scene.layout.Priority.ALWAYS);

        Label dueLabel = new Label(task.getDueDate());
        dueLabel.getStyleClass().add("task-due-label");

        Label timeLabel = new Label(task.getTimeEstimate());
        timeLabel.getStyleClass().add("task-time-label");

        Label statusBadge = new Label(task.getStatus());
        statusBadge.getStyleClass().add("task-status-badge-overdue");

        taskItem.getChildren().addAll(radio, nameLabel, dueLabel, timeLabel, statusBadge);

        taskItem.setOnMouseClicked(e -> handleTaskClick(task));

        return taskItem;
    }

    private void handleTaskClick(DashboardTask task) {
        showInfo("Task Details",
                "Task: " + task.getName() + "\n" +
                        "Due: " + task.getDueDate() + "\n" +
                        "Time: " + task.getTimeEstimate() + "\n" +
                        "Status: " + task.getStatus());
    }

    public void setUserName(String userName) {
        this.userName = userName;
        updateWelcomeMessage();
    }

    public void updateTaskCounts(int newTasks, int overdueTasks) {
        this.newTasks = newTasks;
        this.overdueTasks = overdueTasks;
        updateStats();
    }

    public static class DashboardTask {
        private String name;
        private String dueDate;
        private String timeEstimate;
        private String status;

        public DashboardTask(String name, String dueDate, String timeEstimate, String status) {
            this.name = name;
            this.dueDate = dueDate;
            this.timeEstimate = timeEstimate;
            this.status = status;
        }

        public String getName() { return name; }
        public String getDueDate() { return dueDate; }
        public String getTimeEstimate() { return timeEstimate; }
        public String getStatus() { return status; }

        public void setName(String name) { this.name = name; }
        public void setDueDate(String dueDate) { this.dueDate = dueDate; }
        public void setTimeEstimate(String timeEstimate) { this.timeEstimate = timeEstimate; }
        public void setStatus(String status) { this.status = status; }
    }
}