package com.clarity.app;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClarityCompletedTaskController extends BaseController {

    @FXML
    private MenuButton statusDropdown;

    @FXML
    private VBox taskListContainer;

    @FXML
    private HBox myTaskNavItem;

    private List<Task> completedTasks;

    @Override
    public void initialize() {
        completedTasks = new ArrayList<>();
        loadCompletedTasks();
        refreshTaskList();
        System.out.println("ClarityCompletedTaskController initialized");
    }

    private void loadCompletedTasks() {
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
    }

    private void refreshTaskList() {
        if (taskListContainer == null) {
            System.err.println("WARNING: taskListContainer is null!");
            return;
        }

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

    private HBox createTaskItem(Task task) {
        HBox taskItem = new HBox(12);
        taskItem.getStyleClass().add("task-item");
        taskItem.setAlignment(Pos.CENTER_LEFT);

        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("task-checkbox-completed");
        checkBox.setSelected(true);
        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                handleTaskUncomplete(task);
            }
        });

        Label titleLabel = new Label(task.getTitle());
        titleLabel.getStyleClass().add("task-title-completed");
        HBox.setHgrow(titleLabel, javafx.scene.layout.Priority.ALWAYS);

        VBox dueDateBox = createDetailBox("Due Date", task.getDueDate().toString());
        VBox timeEstBox = createDetailBox("Time Est", task.getTimeEstimate());
        VBox priorityBox = createPriorityBox(TaskPriority.LOW);

        Button statusButton = new Button("Completed");
        statusButton.getStyleClass().add("task-status-button-completed");
        statusButton.setOnAction(e -> handleViewTask(task));

        taskItem.getChildren().addAll(checkBox, titleLabel, dueDateBox, timeEstBox, priorityBox, statusButton);

        return taskItem;
    }

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

    private VBox createPriorityBox(TaskPriority priority) {
        VBox box = new VBox(2);
        box.getStyleClass().add("task-detail-group");
        box.setAlignment(Pos.CENTER_RIGHT);

        Label label = new Label("Priority");
        label.getStyleClass().add("task-detail-label");

        HBox priorityContent = new HBox(4);
        priorityContent.setAlignment(Pos.CENTER_RIGHT);

        SVGPath icon = new SVGPath();
        icon.setContent("M14.4 6L14 4H5v17h2v-7h5.6l.4 2h7V6z");
        icon.getStyleClass().add("priority-icon-low");

        Label priorityLabel = new Label("Low");
        priorityLabel.getStyleClass().add("task-priority-low");

        priorityContent.getChildren().addAll(icon, priorityLabel);
        box.getChildren().addAll(label, priorityContent);

        return box;
    }

    private void handleTaskUncomplete(Task task) {
        if (showConfirmation("Uncomplete Task",
                "Move \"" + task.getTitle() + "\" back to Current tasks?")) {
            task.setCompleted(false);
            completedTasks.remove(task);
            refreshTaskList();
            showInfo("Task Moved", "Task moved back to Current status.");
        }
    }

    private void handleViewTask(Task task) {
        String content = "Status: Completed ✓\n";
        content += "Due Date: " + task.getDueDate() + "\n";
        content += "Time Estimate: " + task.getTimeEstimate() + "\n";
        content += "Priority: " + task.getPriority();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Details");
        alert.setHeaderText(task.getTitle());
        alert.setContentText(content);

        ButtonType deleteButton = new ButtonType("Delete Task");
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(deleteButton, closeButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == deleteButton) {
            handleDeleteTask(task);
        }
    }

    private void handleDeleteTask(Task task) {
        if (showConfirmation("Delete Task",
                "This will permanently delete: " + task.getTitle())) {
            completedTasks.remove(task);
            refreshTaskList();
            showInfo("Task Deleted", "Task has been permanently deleted.");
        }
    }

    @FXML
    private void handleAddTask() {
        showInfo("Add Task", "Task creation dialog would open here.");
    }

    @FXML
    private void handleCurrentTasks() {
        sceneManager.switchTo(SceneManager.SceneType.TASK_VIEW);
    }

    @FXML
    private void handleOverdueTasks() {
        sceneManager.switchTo(SceneManager.SceneType.OVERDUE_TASKS);
    }

    @FXML
    private void handleCompletedTasks() {
        // Already on completed tasks page
        System.out.println("Already on Completed Tasks page");
    }

    public List<Task> getCompletedTasks() {
        return completedTasks;
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