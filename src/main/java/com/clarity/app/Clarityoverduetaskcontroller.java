package com.clarity.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the Overdue Tasks view in Clarity application
 */
public class Clarityoverduetaskcontroller {

    // ================================================
    // FXML Injected Fields
    // ================================================

    @FXML
    private MenuButton statusDropdown;

    @FXML
    private VBox taskListContainer;

    @FXML
    private HBox myTaskNavItem;

    // ================================================
    // Instance Variables
    // ================================================

    private Stage stage;
    private List<Task> overdueTasks;

    // ================================================
    // Initialization
    // ================================================

    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        overdueTasks = new ArrayList<>();

        // Load overdue tasks
        loadOverdueTasks();

        System.out.println("ClarityOverdueTaskController initialized");
    }

    /**
     * Load overdue tasks
     */
    private void loadOverdueTasks() {
        // Sample overdue tasks
        overdueTasks.add(new Task(
                "Boost Client's Feedback on Bing",
                LocalDate.of(2024, 9, 14),
                "2h",
                TaskPriority.URGENT,
                false
        ));

        overdueTasks.add(new Task(
                "Identifying Target Keywords",
                LocalDate.of(2024, 9, 14),
                "30min",
                TaskPriority.URGENT,
                false
        ));

        overdueTasks.add(new Task(
                "Apply Schema Markup",
                LocalDate.of(2024, 9, 14),
                "1h",
                TaskPriority.HIGH,
                false
        ));

        // Display tasks (FXML already has static tasks, but you can regenerate them dynamically)
        // refreshTaskList();
    }

    /**
     * Refresh the task list display
     */
    private void refreshTaskList() {
        taskListContainer.getChildren().clear();

        for (Task task : overdueTasks) {
            HBox taskItem = createTaskItem(task);
            taskListContainer.getChildren().add(taskItem);
        }

        if (overdueTasks.isEmpty()) {
            Label noTasks = new Label("No overdue tasks! 🎉");
            noTasks.setStyle("-fx-font-size: 16px; -fx-text-fill: #9E9E9E; -fx-padding: 40px;");
            taskListContainer.getChildren().add(noTasks);
        }
    }

    /**
     * Create a task item UI component
     */
    private HBox createTaskItem(Task task) {
        HBox taskItem = new HBox(12);
        taskItem.getStyleClass().add("task-item");
        taskItem.setAlignment(Pos.CENTER_LEFT);

        // Checkbox
        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("task-checkbox");
        checkBox.setSelected(task.isCompleted());
        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            task.setCompleted(newVal);
            handleTaskCompletion(task);
        });

        // Task Title
        Label titleLabel = new Label(task.getTitle());
        titleLabel.getStyleClass().add("task-title");
        HBox.setHgrow(titleLabel, javafx.scene.layout.Priority.ALWAYS);

        // Due Date
        VBox dueDateBox = createDetailBox("Due Date", task.getDueDate().toString());

        // Time Estimate
        VBox timeEstBox = createDetailBox("Time Est", task.getTimeEstimate());

        // Priority
        VBox priorityBox = createPriorityBox(task.getPriority());

        // Status Button
        Button statusButton = new Button("Overdue");
        statusButton.getStyleClass().add("task-status-button-overdue");
        statusButton.setOnAction(e -> handleChangeStatus(task));

        taskItem.getChildren().addAll(checkBox, titleLabel, dueDateBox, timeEstBox, priorityBox, statusButton);

        return taskItem;
    }

    /**
     * Create a detail box (for due date, time estimate)
     */
    private VBox createDetailBox(String label, String value) {
        VBox box = new VBox(2);
        box.getStyleClass().add("task-detail-group");
        box.setAlignment(Pos.CENTER_RIGHT);

        Label labelText = new Label(label);
        labelText.getStyleClass().add("task-detail-label");

        Label valueText = new Label(value);
        valueText.getStyleClass().add("task-detail-value");

        box.getChildren().addAll(labelText, valueText);
        return box;
    }

    /**
     * Create priority box with icon and label
     */
    private VBox createPriorityBox(TaskPriority priority) {
        VBox box = new VBox(2);
        box.getStyleClass().add("task-detail-group");
        box.setAlignment(Pos.CENTER_RIGHT);

        Label label = new Label("Priority");
        label.getStyleClass().add("task-detail-label");

        HBox priorityContent = new HBox(4);
        priorityContent.setAlignment(Pos.CENTER_RIGHT);

        // Priority Icon
        SVGPath icon = new SVGPath();
        if (priority == TaskPriority.URGENT) {
            icon.setContent("M14.4 6L14 4H5v17h2v-7h5.6l.4 2h7V6z");
            icon.getStyleClass().add("priority-icon-urgent");
        } else if (priority == TaskPriority.HIGH) {
            icon.setContent("M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z");
            icon.getStyleClass().add("priority-icon-high");
        } else if (priority == TaskPriority.NORMAL) {
            icon.setContent("M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z");
            icon.getStyleClass().add("priority-icon-normal");
        } else {
            icon.setContent("M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z");
            icon.getStyleClass().add("priority-icon-low");
        }

        // Priority Label
        Label priorityLabel = new Label(priority.toString());
        priorityLabel.getStyleClass().add("task-priority-" + priority.toString().toLowerCase());

        priorityContent.getChildren().addAll(icon, priorityLabel);
        box.getChildren().addAll(label, priorityContent);

        return box;
    }

    // ================================================
    // Task Actions
    // ================================================

    /**
     * Handle task completion
     */
    private void handleTaskCompletion(Task task) {
        if (task.isCompleted()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Task Completed");
            alert.setHeaderText("Great job!");
            alert.setContentText("You've completed: " + task.getTitle());
            alert.showAndWait();

            // Remove from overdue list after a delay
            overdueTasks.remove(task);
            refreshTaskList();
        }
    }

    /**
     * Handle status change
     */
    private void handleChangeStatus(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Change Status");
        alert.setHeaderText("Update task status");
        alert.setContentText("Move this task to Current status?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Remove from overdue and move to current
            overdueTasks.remove(task);
            refreshTaskList();

            showAlert(Alert.AlertType.INFORMATION, "Status Updated",
                    "Task moved to Current status.", null);
        }
    }

    /**
     * Handle add task button
     */
    @FXML
    private void handleAddTask() {
        // TODO: Open add task dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Task");
        alert.setHeaderText("Create New Task");
        alert.setContentText("Task creation dialog would open here.");
        alert.showAndWait();
    }

    // ================================================
    // Status Filter Actions
    // ================================================

    /**
     * Switch to Current tasks view
     */
    @FXML
    private void handleCurrentTasks() {
        navigateToView("clarityTask.fxml", "My Task - Clarity");
    }

    // ================================================
    // Navigation Handlers
    // ================================================

    @FXML
    private void handleDashboard() {
        navigateToView("clarityDashboard.fxml", "Dashboard - Clarity");
    }

    @FXML
    private void handleMyTask() {
        // Show menu to choose between Current and Overdue
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
                stage = (Stage) taskListContainer.getScene().getWindow();
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
     * Get overdue tasks list
     */
    public List<Task> getOverdueTasks() {
        return overdueTasks;
    }

    // ================================================
    // Inner Classes
    // ================================================

    /**
     * Task Priority Enum
     */
    public enum TaskPriority {
        URGENT("Urgent"),
        HIGH("High"),
        NORMAL("Normal"),
        LOW("Low");

        private String displayName;

        TaskPriority(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    /**
     * Task Model Class
     */
    public static class Task {
        private String title;
        private LocalDate dueDate;
        private String timeEstimate;
        private TaskPriority priority;
        private boolean completed;

        public Task(String title, LocalDate dueDate, String timeEstimate,
                    TaskPriority priority, boolean completed) {
            this.title = title;
            this.dueDate = dueDate;
            this.timeEstimate = timeEstimate;
            this.priority = priority;
            this.completed = completed;
        }

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public LocalDate getDueDate() { return dueDate; }
        public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

        public String getTimeEstimate() { return timeEstimate; }
        public void setTimeEstimate(String timeEstimate) { this.timeEstimate = timeEstimate; }

        public TaskPriority getPriority() { return priority; }
        public void setPriority(TaskPriority priority) { this.priority = priority; }

        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
    }
}
