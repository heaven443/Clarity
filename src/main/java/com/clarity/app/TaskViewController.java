package com.clarity.app;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.net.URL;
import java.util.ResourceBundle;

public class askViewController implements Initializable {

    @FXML private VBox taskContainer;
    @FXML private ComboBox<String> filterComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTasks();
    }

    private void loadTasks() {
        taskContainer.getChildren().clear();

        // Sample tasks
        addTask("Conduct keyword research for target pages", "Sept 13", "3h", "Normal", "Current");
        addTask("Identifying Target Keywords", "Sept 13", "30min", "Normal", "Current");
        addTask("Update keyword tracking list", "Sept 14", "1h", "Low", "Current");
        addTask("Identify low-hanging fruit keywords", "Sept 14", "1h", "Low", "Current");
        addTask("Analyze competitors' keyword strategy", "Sept 14", "2h", "Low", "Current");
    }

    private void addTask(String title, String dueDate, String timeEst, String priority, String status) {
        HBox taskRow = new HBox(10);
        taskRow.getStyleClass().add("task-row");
        taskRow.setAlignment(Pos.CENTER_LEFT);
        taskRow.setPadding(new Insets(12, 20, 12, 20));

        // Checkbox
        CheckBox checkBox = new CheckBox();
        checkBox.getStyleClass().add("task-checkbox");
        checkBox.setPrefWidth(40);

        // Task Title
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("task-title");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        // Due Date
        VBox dueDateBox = new VBox(2);
        dueDateBox.setAlignment(Pos.CENTER);
        dueDateBox.setPrefWidth(100);
        Label dueDateLabel = new Label("Due Date");
        dueDateLabel.getStyleClass().add("task-label");
        Label dueDateValue = new Label(dueDate);
        dueDateValue.getStyleClass().add("task-value");
        dueDateBox.getChildren().addAll(dueDateLabel, dueDateValue);

        // Time Estimate
        VBox timeEstBox = new VBox(2);
        timeEstBox.setAlignment(Pos.CENTER);
        timeEstBox.setPrefWidth(80);
        Label timeEstLabel = new Label("Time Est");
        timeEstLabel.getStyleClass().add("task-label");
        Label timeEstValue = new Label(timeEst);
        timeEstValue.getStyleClass().add("task-value");
        timeEstBox.getChildren().addAll(timeEstLabel, timeEstValue);

        // Priority
        VBox priorityBox = new VBox(2);
        priorityBox.setAlignment(Pos.CENTER);
        priorityBox.setPrefWidth(100);
        Label priorityLabel = new Label("Priority");
        priorityLabel.getStyleClass().add("task-label");
        HBox priorityValue = new HBox(5);
        priorityValue.setAlignment(Pos.CENTER);
        Label flagIcon = new Label("🚩");
        flagIcon.getStyleClass().add("priority-icon");
        Label priorityText = new Label(priority);
        priorityText.getStyleClass().add("task-value");
        priorityValue.getChildren().addAll(flagIcon, priorityText);
        priorityBox.getChildren().addAll(priorityLabel, priorityValue);

        // Status
        VBox statusBox = new VBox(2);
        statusBox.setAlignment(Pos.CENTER);
        statusBox.setPrefWidth(120);
        Label statusLabel = new Label("Status");
        statusLabel.getStyleClass().add("task-label");
        Label statusBadge = new Label(status);
        statusBadge.getStyleClass().addAll("status-badge", "status-current");
        statusBox.getChildren().addAll(statusLabel, statusBadge);

        taskRow.getChildren().addAll(
                checkBox,
                titleLabel,
                dueDateBox,
                timeEstBox,
                priorityBox,
                statusBox
        );

        taskContainer.getChildren().add(taskRow);
    }

    @FXML
    private void handleAddTask() {
        System.out.println("Add Task clicked");
        // Add your task creation logic here
    }
}