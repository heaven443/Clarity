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
 * Controller for the Completed Tasks view in Clarity application
 */
public class Claritycompletedtaskcontroller {

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
    private List<Task> completedTasks;

    // ================================================
    // Initialization
    // ================================================

    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        completedTasks = new ArrayList<>();

        // Load completed tasks
        loadCompletedTasks();

        System.out.println("ClarityCompletedTaskController initialized");
    }

    /**
     * Load completed tasks
     */
    private void loadCompletedTasks() {
        // Sample completed tasks
        completedTasks.add(new Task(
                "Optimize meta titles and descriptions",
                LocalDate.of(2024, 9, 14),
                "2h",
                TaskPriority.LOW,
                true
        ));

        completedTasks.add(new Task(
                "Improve internal linking structure",
                LocalDate.of(2024, 9, 14),
                "30min",
                TaskPriority.LOW,
                true
        ));

        completedTasks.add(new Task(
                "Check and fix header tag hierarchy",
                LocalDate.of(2024, 9, 14),
                "1h",
                TaskPriority.LOW,
                true
        ));

        completedTasks.add(new Task(
                "Optimize images",
                LocalDate.of(2024, 9, 14),
                "1h",
                TaskPriority.LOW,
                true
        ));

        completedTasks.add(new Task(
                "Audit and improve URL structure",
                LocalDate.of(2024, 9, 14),
                "1h",
                TaskPriority.LOW,
                true
        ));

        completedTasks.add(new Task(
                "Run site speed test & implement improvements",
                LocalDate.of(2024, 9, 14),
                "1h",
                TaskPriority.LOW,
                true
        ));

        // Display tasks (FXML already has static tasks, but you can regenerate them dynamically)
        // refreshTaskList();
    }

    /**
     * Refresh the task list display
     */
    private void refreshTaskList() {
        taskListContainer.getChildren().clear();

        for (Task task : completedTasks) {
            HBox taskItem = createTaskItem(task);
            taskListContainer.getChildren().add(taskItem);
        }

        if (completedTasks.isEmpty()) {
            Label noTasks = new Label("No completed tasks yet");
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

        // Checkbox - Completed
        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("task-checkbox-completed");
        checkBox.setSelected(true);
        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                handleTaskUncomplete(task);
            }
        });

        // Task Title - Completed (grayed out)
        Label titleLabel = new Label(task.getTitle());
        titleLabel.getStyleClass().add("task-title-completed");
        HBox.setHgrow(titleLabel, javafx.scene.layout.Priority.ALWAYS);

        // Due Date
        VBox dueDateBox = createDetailBox("Due Date", task.getDueDate().toString());

        // Time Estimate
        VBox timeEstBox = createDetailBox("Time Est", task.getTimeEstimate());

        // Priority (always Low for completed)
        VBox priorityBox = createPriorityBox(TaskPriority.LOW);

        // Status Button
        Button statusButton = new Button("Completed");
        statusButton.getStyleClass().add("task-status-button-completed");
        statusButton.setOnAction(e -> handleViewTask(task));

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

        // Priority Icon (all low for completed)
        SVGPath icon = new SVGPath();
        icon.setContent("M14.4 6L14 4H5v17h2v-7h5.6l.4 2h7V6z");
        icon.getStyleClass().add("priority-icon-low");

        // Priority Label
        Label priorityLabel = new Label("Low");
        priorityLabel.getStyleClass().add("task-priority-low");

        priorityContent.getChildren().addAll(icon, priorityLabel);
        box.getChildren().addAll(label, priorityContent);

        return box;
    }

    // ================================================
    // Task Actions
    // ================================================

    /**
     * Handle task uncomplete (unchecking a completed task)
     */
    private void handleTaskUncomplete(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Uncomplete Task");
        alert.setHeaderText("Mark task as incomplete?");
        alert.setContentText("Move \"" + task.getTitle() + "\" back to Current tasks?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            task.setCompleted(false);
            completedTasks.remove(task);
            refreshTaskList();

            showAlert(Alert.AlertType.INFORMATION, "Task Moved",
                    "Task moved back to Current status.", null);
        }
    }

    /**
     * Handle view task details
     */
    private void handleViewTask(Task task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Details");
        alert.setHeaderText(task.getTitle());

        String content = "Status: Completed ✓\n";
        content += "Due Date: " + task.getDueDate() + "\n";
        content += "Time Estimate: " + task.getTimeEstimate() + "\n";
        content += "Priority: " + task.getPriority();

        alert.setContentText(content);

        // Add option to delete
        ButtonType deleteButton = new ButtonType("Delete Task");
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(deleteButton, closeButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == deleteButton) {
            handleDeleteTask(task);
        }
    }

    /**
     * Handle delete task
     */
    private void handleDeleteTask(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Task");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("This will permanently delete: " + task.getTitle());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            completedTasks.remove(task);
            refreshTaskList();

            showAlert(Alert.AlertType.INFORMATION, "Task Deleted",
                    "Task has been permanently deleted.", null);
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

    /**
     * Switch to Overdue tasks view
     */
    @FXML
    private void handleOverdueTasks() {
        navigateToView("clarityOverdueTask.fxml", "Overdue Tasks - Clarity");
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
        // Show menu to choose between Current, Overdue, and Completed
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
     * Get completed tasks list
     */
    public List<Task> getCompletedTasks() {
        return completedTasks;
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
