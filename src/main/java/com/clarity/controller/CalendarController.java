package com.clarity.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CalendarController implements Initializable {
    @FXML
    private Label monthLabel;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private VBox upcomingEventsBox;
    @FXML
    private Button prevBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button myTaskBtn;
    @FXML
    private Button notesBtn;
    @FXML
    private Button scheduleBtn;
    @FXML
    private Button settingsBtn;
    @FXML
    private Button helpBtn;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button addEventBtn;

    private YearMonth currentMonth = YearMonth.now();
    private Map<LocalDate, List<String>> events = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSampleEvents();
        setupEventHandlers();
        refreshCalendar();
    }

    private void setupEventHandlers() {
        prevBtn.setOnAction(e -> previousMonth());
        nextBtn.setOnAction(e -> nextMonth());
        addEventBtn.setOnAction(e -> openAddEventDialog());

        // Sidebar button handlers
        dashboardBtn.setOnAction(e -> handleNavigation("Dashboard"));
        myTaskBtn.setOnAction(e -> handleNavigation("My Task"));
        notesBtn.setOnAction(e -> handleNavigation("Notes"));
        scheduleBtn.setOnAction(e -> handleNavigation("Schedule"));
        settingsBtn.setOnAction(e -> handleNavigation("Settings"));
        helpBtn.setOnAction(e -> handleNavigation("Help & Support"));
        logoutBtn.setOnAction(e -> handleNavigation("Logout"));
    }

    private void previousMonth() {
        currentMonth = currentMonth.minusMonths(1);
        refreshCalendar();
    }

    private void nextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        refreshCalendar();
    }

    public void refreshCalendar() {
        // Update month label
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthLabel.setText(currentMonth.format(formatter));

        // Clear existing calendar cells
        calendarGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);

        // Calculate first day of month and days in month
        LocalDate firstOfMonth = currentMonth.atDay(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        int daysInMonth = currentMonth.lengthOfMonth();

        int row = 1;
        int col = firstDayOfWeek - 1;

        // Add day cells
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), day);
            VBox dayCell = createDayCell(date);
            calendarGrid.add(dayCell, col, row);

            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }

        updateUpcomingEvents();
    }

    private VBox createDayCell(LocalDate date) {
        VBox cell = new VBox(5);
        cell.getStyleClass().add("day-cell");
        cell.setPrefHeight(100);

        boolean isToday = date.equals(LocalDate.now());
        boolean isSelected = false;

        if (isToday) {
            cell.getStyleClass().add("day-cell-today");
        }

        Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dayLabel.getStyleClass().add("day-number");

        VBox eventsList = new VBox(3);
        eventsList.getStyleClass().add("events-list");

        if (events.containsKey(date)) {
            for (String event : events.get(date)) {
                Label eventLabel = new Label("• " + event);
                eventLabel.getStyleClass().add("event-item");
                eventLabel.setWrapText(true);
                eventsList.getChildren().add(eventLabel);
            }
        }

        cell.getChildren().addAll(dayLabel, eventsList);

        // Hover effects
        cell.setOnMouseEntered(e -> {
            if (!cell.getStyleClass().contains("day-cell-today")) {
                cell.setStyle("-fx-border-color: #999999; -fx-border-width: 2;");
            }
        });

        cell.setOnMouseExited(e -> {
            if (!cell.getStyleClass().contains("day-cell-today")) {
                cell.setStyle("-fx-border-color: #e0e0e0; -fx-border-width: 1;");
            }
        });

        cell.setOnMouseClicked(e -> onDaySelected(date));

        return cell;
    }

    private void onDaySelected(LocalDate date) {
        System.out.println("Selected date: " + date);
    }

    private void updateUpcomingEvents() {
        upcomingEventsBox.getChildren().clear();

        List<LocalDate> sortedDates = new ArrayList<>(events.keySet());
        sortedDates.sort(Comparator.naturalOrder());

        int eventCount = 0;
        for (LocalDate date : sortedDates) {
            if (date.isAfter(LocalDate.now().minusDays(1)) && eventCount < 5) {
                for (String event : events.get(date)) {
                    if (eventCount < 5) {
                        VBox eventItem = new VBox(3);
                        eventItem.getStyleClass().add("event-card");

                        Label dateLabel = new Label(String.valueOf(date.getDayOfMonth()));
                        dateLabel.getStyleClass().add("event-date");

                        Label eventLabel = new Label(event);
                        eventLabel.getStyleClass().add("event-title");
                        eventLabel.setWrapText(true);

                        eventItem.getChildren().addAll(dateLabel, eventLabel);
                        upcomingEventsBox.getChildren().add(eventItem);
                        eventCount++;
                    }
                }
            }
        }
    }

    private void openAddEventDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Event");
        dialog.setHeaderText("Create a new event");

        ButtonType okButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        VBox content = new VBox(10);
        content.setPadding(new javafx.geometry.Insets(20));

        TextField eventNameField = new TextField();
        eventNameField.setPromptText("Event name");

        DatePicker datePicker = new DatePicker(LocalDate.now());

        content.getChildren().addAll(
                new Label("Event Name:"),
                eventNameField,
                new Label("Date:"),
                datePicker
        );

        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButton) {
                String eventName = eventNameField.getText();
                LocalDate selectedDate = datePicker.getValue();

                if (!eventName.isEmpty()) {
                    events.computeIfAbsent(selectedDate, k -> new ArrayList<>()).add(eventName);
                    refreshCalendar();
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void handleNavigation(String section) {
        System.out.println("Navigating to: " + section);
        // Implement navigation logic here
    }

    private void initializeSampleEvents() {
        LocalDate sept3 = LocalDate.of(2025, 9, 3);
        LocalDate sept12 = LocalDate.of(2025, 9, 12);
        LocalDate sept16 = LocalDate.of(2025, 9, 16);
        LocalDate sept25 = LocalDate.of(2025, 9, 25);
        LocalDate sept26 = LocalDate.of(2025, 9, 26);
        LocalDate sept29 = LocalDate.of(2025, 9, 29);

        events.put(sept3, Arrays.asList("Team Meeting at 10am"));
        events.put(sept12, Arrays.asList("Client Presentation at 3pm"));
        events.put(sept16, Arrays.asList("Deadline for Project Proposal"));
        events.put(sept25, Arrays.asList("Quick Gym Session"));
        events.put(sept26, Arrays.asList("Networking Dinner"));
        events.put(sept29, Arrays.asList("Family Lunch"));
    }

    public Map<LocalDate, List<String>> getEvents() {
        return events;
    }

    public void addEvent(LocalDate date, String eventName) {
        events.computeIfAbsent(date, k -> new ArrayList<>()).add(eventName);
        refreshCalendar();
    }

    public void removeEvent(LocalDate date, String eventName) {
        if (events.containsKey(date)) {
            events.get(date).remove(eventName);
            if (events.get(date).isEmpty()) {
                events.remove(date);
            }
            refreshCalendar();
        }
    }
}