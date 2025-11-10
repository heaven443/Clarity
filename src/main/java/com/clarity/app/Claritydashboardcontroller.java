package com.clarity.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the Dashboard view in Clarity application
 */
public class Claritydashboardcontroller {

    // ================================================
    // FXML Injected Fields
    // ================================================

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label newTaskCount;

    @FXML
    private Label overdueCount;

    @FXML
    private GridPane miniCalendar;

    @FXML
    private BarChart<String, Number> progressChart;

    @FXML
    private VBox overdueTaskList;

    @FXML
    private HBox dashboardNavItem;

    // ================================================
    // Instance Variables
    // ================================================

    private Stage stage;
    private String userName = "Christy";
    private int newTasks = 5;
    private int overdueTasks = 3;
    private List<DashboardTask> overdueTasksList;

    // ================================================
    // Initialization
    // ================================================

    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        // Set welcome message and date
        updateWelcomeMessage();
        updateDate();

        // Update stats
        updateStats();

        // Generate mini calendar
        generateMiniCalendar();

        // Load progress chart
        loadProgressChart();

        // Load overdue tasks
        loadOverdueTasks();

        System.out.println("ClarityDashboardController initialized");
    }

    /**
     * Update welcome message
     */
    private void updateWelcomeMessage() {
        welcomeLabel.setText("Hello There " + userName + "!");
    }

    /**
     * Update date label
     */
    private void updateDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        dateLabel.setText(today.format(formatter));
    }

    /**
     * Update stats counters
     */
    private void updateStats() {
        newTaskCount.setText(String.valueOf(newTasks));
        overdueCount.setText(String.valueOf(overdueTasks));
    }

    // ================================================
    // Mini Calendar Generation
    // ================================================

    /**
     * Generate mini calendar for current month
     */
    private void generateMiniCalendar() {
        miniCalendar.getChildren().clear();

        // Add day headers
        String[] dayHeaders = {"S", "M", "T", "W", "T", "F", "S"};
        for (int i = 0; i < dayHeaders.length; i++) {
            Label header = new Label(dayHeaders[i]);
            header.getStyleClass().add("cal-day-header");
            miniCalendar.add(header, i, 0);
        }

        // September 2025 calendar data
        // Starting day: Monday (1st = column 1)
        int[] days = {
                0, 1, 2, 3, 4, 5, 6,  // Week 1: blank, 1-6
                7, 8, 9, 10, 11, 12, 13,  // Week 2: 7-13
                14, 15, 16, 17, 18, 19, 20,  // Week 3: 14-20
                21, 22, 23, 24, 25, 26, 27,  // Week 4: 21-27
                28, 29, 30, 0, 0, 0, 0  // Week 5: 28-30
        };

        // Special days
        List<Integer> eventDays = List.of(16, 25, 26);
        List<Integer> overdueDays = List.of(14, 21, 28, 29);

        int row = 1;
        int col = 0;

        for (int day : days) {
            if (day > 0) {
                Label dayLabel = new Label(String.valueOf(day));
                dayLabel.getStyleClass().add("cal-day");

                // Add special styling
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

    // ================================================
    // Progress Chart
    // ================================================

    /**
     * Load progress chart data
     */
    private void loadProgressChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tasks Completed");

        // Sample data matching the image
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

    // ================================================
    // Overdue Tasks
    // ================================================

    /**
     * Load overdue tasks list
     */
    private void loadOverdueTasks() {
        overdueTasksList = new ArrayList<>();

        // Sample overdue tasks
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

        // Display tasks
        displayOverdueTasks();
    }

    /**
     * Display overdue tasks in the sidebar
     */
    private void displayOverdueTasks() {
        overdueTaskList.getChildren().clear();

        for (DashboardTask task : overdueTasksList) {
            HBox taskItem = createTaskItem(task);
            overdueTaskList.getChildren().add(taskItem);
        }
    }

    /**
     * Create a task item UI component
     */
    private HBox createTaskItem(DashboardTask task) {
        HBox taskItem = new HBox(8);
        taskItem.getStyleClass().add("dashboard-task-item");
        taskItem.setAlignment(Pos.CENTER_LEFT);

        // Radio button
        RadioButton radio = new RadioButton();
        radio.getStyleClass().add("task-radio");

        // Task name
        Label nameLabel = new Label(task.getName());
        nameLabel.getStyleClass().add("task-name-label");
        HBox.setHgrow(nameLabel, javafx.scene.layout.Priority.ALWAYS);

        // Due date
        Label dueLabel = new Label(task.getDueDate());
        dueLabel.getStyleClass().add("task-due-label");

        // Time estimate
        Label timeLabel = new Label(task.getTimeEstimate());
        timeLabel.getStyleClass().add("task-time-label");

        // Status badge
        Label statusBadge = new Label(task.getStatus());
        statusBadge.getStyleClass().add("task-status-badge-overdue");

        taskItem.getChildren().addAll(radio, nameLabel, dueLabel, timeLabel, statusBadge);

        // Click handler
        taskItem.setOnMouseClicked(e -> handleTaskClick(task));

        return taskItem;
    }

    /**
     * Handle task click
     */
    private void handleTaskClick(DashboardTask task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Details");
        alert.setHeaderText(task.getName());
        alert.setContentText("Due: " + task.getDueDate() + "\n" +
                "Time: " + task.getTimeEstimate() + "\n" +
                "Status: " + task.getStatus());
        alert.showAndWait();
    }

    // ================================================
    // Navigation Handlers
    // ================================================

    @FXML
    private void handleDashboard() {
        // Already on dashboard
    }

    @FXML
    private void handleMyTask() {
        navigateToView("clarityTask.fxml", "My Task - Clarity");
    }

    @FXML
    private void handleNotes() {
        navigateToView("clarityNotes.fxml", "Notes - Clarity");
    }

    @FXML
    private void handleSchedule() {
        navigateToView("claritySchedule.fxml", "Schedule - Clarity");
    }

    @FXML
    private void handleSettings() {
        navigateToView("claritySettings.fxml", "Settings - Clarity");
    }

    @FXML
    private void handleHelp() {
        navigateToView("clarityHelp.fxml", "Help & Support - Clarity");
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            navigateToView("clarityLogin.fxml", "Login - Clarity");
        }
    }

    // ================================================
    // Helper Methods
    // ================================================

    /**
     * Navigate to a different view
     */
    private void navigateToView(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            if (stage == null) {
                stage = (Stage) welcomeLabel.getScene().getWindow();
            }

            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Could not load the requested view.",
                    "Error: " + e.getMessage());
        }
    }

    /**
     * Show an alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        if (content != null) {
            alert.setContentText(content);
        }
        alert.showAndWait();
    }

    /**
     * Set the stage for this controller
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Set user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
        updateWelcomeMessage();
    }

    /**
     * Update task counts
     */
    public void updateTaskCounts(int newTasks, int overdueTasks) {
        this.newTasks = newTasks;
        this.overdueTasks = overdueTasks;
        updateStats();
    }

    // ================================================
    // Inner Class: Dashboard Task
    // ================================================

    /**
     * Simple task class for dashboard display
     */
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

        // Getters
        public String getName() { return name; }
        public String getDueDate() { return dueDate; }
        public String getTimeEstimate() { return timeEstimate; }
        public String getStatus() { return status; }

        // Setters
        public void setName(String name) { this.name = name; }
        public void setDueDate(String dueDate) { this.dueDate = dueDate; }
        public void setTimeEstimate(String timeEstimate) { this.timeEstimate = timeEstimate; }
        public void setStatus(String status) { this.status = status; }
    }
}
