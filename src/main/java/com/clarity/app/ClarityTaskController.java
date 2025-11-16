package com.clarity.app;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClarityTaskController extends BaseController {

    @FXML
    private VBox taskListContainer;

    @FXML
    private ComboBox<String> filterComboBox;

    private List<Task> currentTasks;

    @Override
    public void initialize() {
        currentTasks = new ArrayList<>();
        loadCurrentTasks();
        refreshTaskList();
        System.out.println("ClarityTaskController initialized");
    }

    private void loadCurrentTasks() {
        currentTasks.add(new Task(
                "Conduct keyword research for target pages",
                LocalDate.of(2024, 9, 13),
                "3h",
                TaskPriority.NORMAL,
                false
        ));

        currentTasks.add(new Task(
                "Identifying Target Keywords",
                LocalDate.of(2024, 9, 13),
                "30min",
                TaskPriority.NORMAL,
                false
        ));

        currentTasks.add(new Task(
                "Update keyword tracking list",
                LocalDate.of(2024, 9, 14),
                "1h",
                TaskPriority.LOW,
                false
        ));

        currentTasks.add(new Task(
                "Identify low-hanging fruit keywords",
                LocalDate.of(2024, 9, 14),
                "1h",
                TaskPriority.LOW,
                false
        ));

        currentTasks.add(new Task(
                "Analyze competitors' keyword strategy",
                LocalDate.of(2024, 9, 14),
                "2h",
                TaskPriority.LOW,
                false
        ));
    }

    private void refreshTaskList() {
        if (taskListContainer == null) {
            System.err.println("WARNING: taskListContainer is null!");
            return;
        }

        taskListContainer.getChildren().clear();

        for (Task task : currentTasks) {
            HBox taskItem = createTaskItem(task);
            taskListContainer.getChildren().add(taskItem);
        }

        if (currentTasks.isEmpty()) {
            Label noTasks = new Label("No current tasks. Great job!");
            noTasks.setStyle("-fx-font-size: 16px; -fx-text-fill: #9E9E9E; -fx-padding: 40px;");
            taskListContainer.getChildren().add(noTasks);
        }
    }

    private HBox createTaskItem(Task task) {
        HBox taskItem = new HBox(12);
        taskItem.getStyleClass().add("task-item");
        taskItem.setAlignment(Pos.CENTER_LEFT);
        taskItem.setStyle("-fx-padding: 15px; -fx-background-color: white; -fx-background-radius: 8px;");

        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("task-checkbox");
        checkBox.setSelected(task.isCompleted());
        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            task.setCompleted(newVal);
            if (newVal) {
                handleTaskCompletion(task);
            }
        });

        Label titleLabel = new Label(task.getTitle());
        titleLabel.getStyleClass().add("task-title");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        VBox dueDateBox = createDetailBox("Due Date", task.getDueDate().toString());
        VBox timeEstBox = createDetailBox("Time Est", task.getTimeEstimate());
        VBox priorityBox = createPriorityBox(task.getPriority());

        Button statusButton = new Button("Current");
        statusButton.getStyleClass().add("task-status-button-current");
        statusButton.setStyle("-fx-background-color: #798BFF; -fx-text-fill: white; " +
                "-fx-background-radius: 15px; -fx-padding: 8px 20px;");
        statusButton.setOnAction(e -> handleStatusChange(task));

        taskItem.getChildren().addAll(
                checkBox, titleLabel, dueDateBox, timeEstBox, priorityBox, statusButton
        );

        return taskItem;
    }

    private VBox createDetailBox(String label, String value) {
        VBox box = new VBox(2);
        box.getStyleClass().add("task-detail-group");
        box.setAlignment(Pos.CENTER_RIGHT);
        box.setPrefWidth(100);

        Label labelText = new Label(label);
        labelText.getStyleClass().add("task-detail-label");
        labelText.setStyle("-fx-font-size: 11px; -fx-text-fill: #999999;");

        Label valueText = new Label(value);
        valueText.getStyleClass().add("task-detail-value");
        valueText.setStyle("-fx-font-size: 13px; -fx-text-fill: #333333;");

        box.getChildren().addAll(labelText, valueText);
        return box;
    }

    private VBox createPriorityBox(TaskPriority priority) {
        VBox box = new VBox(2);
        box.getStyleClass().add("task-detail-group");
        box.setAlignment(Pos.CENTER_RIGHT);
        box.setPrefWidth(100);

        Label label = new Label("Priority");
        label.getStyleClass().add("task-detail-label");
        label.setStyle("-fx-font-size: 11px; -fx-text-fill: #999999;");

        Label priorityLabel = new Label(priority.toString());
        priorityLabel.getStyleClass().add("task-priority-" + priority.toString().toLowerCase());

        String color = switch (priority) {
            case URGENT -> "#FF3D41";
            case HIGH -> "#FF9800";
            case NORMAL -> "#4CAF50";
            case LOW -> "#9E9E9E";
        };

        priorityLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + color + ";");

        box.getChildren().addAll(label, priorityLabel);
        return box;
    }

    private void handleTaskCompletion(Task task) {
        if (showConfirmation("Complete Task",
                "Mark \"" + task.getTitle() + "\" as completed?")) {
            currentTasks.remove(task);
            refreshTaskList();
            showInfo("Task Completed", "Great job! Task has been completed.");
        } else {
            task.setCompleted(false);
        }
    }

    private void handleStatusChange(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Change Task Status");
        alert.setHeaderText("Where would you like to move this task?");
        alert.setContentText(task.getTitle());

        ButtonType completeButton = new ButtonType("Mark Complete");
        ButtonType overdueButton = new ButtonType("Move to Overdue");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(completeButton, overdueButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == completeButton) {
                task.setCompleted(true);
                currentTasks.remove(task);
                refreshTaskList();
                showInfo("Task Completed", "Task moved to Completed!");
            } else if (response == overdueButton) {
                currentTasks.remove(task);
                refreshTaskList();
                showInfo("Task Status Updated", "Task moved to Overdue.");
            }
        });
    }

    @FXML
    private void handleAddTask() {
        showInfo("Add Task", "Task creation dialog would open here.");
    }

    @FXML
    private void handleOverdueTasks() {
        sceneManager.switchTo(SceneManager.SceneType.OVERDUE_TASKS);
    }

    @FXML
    private void handleCompletedTasks() {
        sceneManager.switchTo(SceneManager.SceneType.COMPLETED_TASKS);
    }

    @FXML
    private void handleSettingsClick() {
        handleSettings();
    }

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