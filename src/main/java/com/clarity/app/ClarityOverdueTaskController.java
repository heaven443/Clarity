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

public class ClarityOverdueTaskController extends BaseController {

    @FXML
    private MenuButton statusDropdown;

    @FXML
    private VBox taskListContainer;

    @FXML
    private HBox myTaskNavItem;

    private List<Task> overdueTasks;

    @Override
    public void initialize() {
        overdueTasks = new ArrayList<>();
        loadOverdueTasks();
        refreshTaskList(); // ✅ FIXED: Added this missing call
        System.out.println("ClarityOverdueTaskController initialized");
    }

    private void loadOverdueTasks() {
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
    }

    private void refreshTaskList() {
        if (taskListContainer == null) {
            System.err.println("WARNING: taskListContainer is null!");
            return;
        }

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

    private HBox createTaskItem(Task task) {
        HBox taskItem = new HBox(12);
        taskItem.getStyleClass().add("task-item");
        taskItem.setAlignment(Pos.CENTER_LEFT);

        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("task-checkbox");
        checkBox.setSelected(task.isCompleted());
        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            task.setCompleted(newVal);
            handleTaskCompletion(task);
        });

        Label titleLabel = new Label(task.getTitle());
        titleLabel.getStyleClass().add("task-title");
        HBox.setHgrow(titleLabel, javafx.scene.layout.Priority.ALWAYS);

        VBox dueDateBox = createDetailBox("Due Date", task.getDueDate().toString());
        VBox timeEstBox = createDetailBox("Time Est", task.getTimeEstimate());
        VBox priorityBox = createPriorityBox(task.getPriority());

        Button statusButton = new Button("Overdue");
        statusButton.getStyleClass().add("task-status-button-overdue");
        statusButton.setOnAction(e -> handleChangeStatus(task));

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

        Label priorityLabel = new Label(priority.toString());
        priorityLabel.getStyleClass().add("task-priority-" + priority.toString().toLowerCase());

        priorityContent.getChildren().addAll(icon, priorityLabel);
        box.getChildren().addAll(label, priorityContent);

        return box;
    }

    private void handleTaskCompletion(Task task) {
        if (task.isCompleted()) {
            showInfo("Task Completed", "Great job!\nYou've completed: " + task.getTitle());
            overdueTasks.remove(task);
            refreshTaskList();
        }
    }

    private void handleChangeStatus(Task task) {
        if (showConfirmation("Change Status", "Move this task to Current status?")) {
            overdueTasks.remove(task);
            refreshTaskList();
            showInfo("Status Updated", "Task moved to Current status.");
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
    private void handleCompletedTasks() {
        sceneManager.switchTo(SceneManager.SceneType.COMPLETED_TASKS);
    }

    public List<Task> getOverdueTasks() {
        return overdueTasks;
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