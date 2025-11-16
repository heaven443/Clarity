package com.clarity.app;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

public class ClarityScheduleController extends BaseController {

    @FXML
    private GridPane mainCalendarGrid;

    @FXML
    private VBox upcomingEventsContainer;

    @FXML
    private Label monthYearLabel;

    @FXML
    private GridPane calendarGrid;

    private YearMonth currentYearMonth;
    private List<Event> events;

    @Override
    public void initialize() {
        currentYearMonth = YearMonth.now();
        events = new ArrayList<>();

        loadSampleEvents();

        updateCalendar();

        loadUpcomingEvents();

        System.out.println("Clarity Schedule screen initialized with calendar.");
    }

    private void loadSampleEvents() {
        LocalDate today = LocalDate.now();

        events.add(new Event("Team Meeting", today.plusDays(2), "10:00 AM"));
        events.add(new Event("Project Deadline", today.plusDays(5), "5:00 PM"));
        events.add(new Event("Client Call", today.plusDays(7), "2:00 PM"));
        events.add(new Event("Code Review", today.plusDays(10), "11:00 AM"));
        events.add(new Event("Sprint Planning", today.plusDays(14), "9:00 AM"));
    }

    private void updateCalendar() {
        if (calendarGrid == null) {
            System.err.println("WARNING: calendarGrid is null!");
            return;
        }

        if (monthYearLabel != null) {
            monthYearLabel.setText(currentYearMonth.getMonth().getDisplayName(
                    TextStyle.FULL, Locale.ENGLISH).toUpperCase() + " " +
                    currentYearMonth.getYear());
        }

        calendarGrid.getChildren().clear();

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int daysInMonth = currentYearMonth.lengthOfMonth();
        int startDayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        int dayCounter = 1;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 7; col++) {
                VBox dayCell = new VBox();
                dayCell.setAlignment(Pos.TOP_CENTER);
                dayCell.getStyleClass().add("calendar-day-cell");
                dayCell.setStyle("-fx-border-color: #E0E0E0; -fx-border-width: 0.5; " +
                        "-fx-padding: 8; -fx-background-color: white;");
                dayCell.setMinSize(100, 80);

                int position = row * 7 + col;
                int actualDay = position - (startDayOfWeek - 1) + 1;

                if (actualDay > 0 && actualDay <= daysInMonth) {
                    Label dayLabel = new Label(String.valueOf(actualDay));
                    dayLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                    LocalDate currentDate = currentYearMonth.atDay(actualDay);

                    if (currentDate.equals(LocalDate.now())) {
                        dayCell.setStyle("-fx-border-color: #798BFF; -fx-border-width: 2; " +
                                "-fx-padding: 8; -fx-background-color: #F0F3FF;");
                        dayLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; " +
                                "-fx-text-fill: #798BFF;");
                    }

                    if (hasEventOnDate(currentDate)) {
                        Label eventDot = new Label("•");
                        eventDot.setStyle("-fx-font-size: 20px; -fx-text-fill: #FF5722;");
                        dayCell.getChildren().addAll(dayLabel, eventDot);
                    } else {
                        dayCell.getChildren().add(dayLabel);
                    }

                    final LocalDate clickedDate = currentDate;
                    dayCell.setOnMouseClicked(e -> handleDayClick(clickedDate));
                    dayCell.setOnMouseEntered(e -> {
                        if (!clickedDate.equals(LocalDate.now())) {
                            dayCell.setStyle("-fx-border-color: #E0E0E0; -fx-border-width: 0.5; " +
                                    "-fx-padding: 8; -fx-background-color: #F5F5F5; -fx-cursor: hand;");
                        }
                    });
                    dayCell.setOnMouseExited(e -> {
                        if (!clickedDate.equals(LocalDate.now())) {
                            dayCell.setStyle("-fx-border-color: #E0E0E0; -fx-border-width: 0.5; " +
                                    "-fx-padding: 8; -fx-background-color: white;");
                        }
                    });
                }

                calendarGrid.add(dayCell, col, row);
            }
        }
    }

    private boolean hasEventOnDate(LocalDate date) {
        return events.stream().anyMatch(event -> event.getDate().equals(date));
    }

    private void handleDayClick(LocalDate date) {
        List<Event> dayEvents = events.stream()
                .filter(event -> event.getDate().equals(date))
                .toList();

        if (dayEvents.isEmpty()) {
            showInfo("No Events", "No events scheduled for " + date);
        } else {
            StringBuilder message = new StringBuilder("Events on " + date + ":\n\n");
            for (Event event : dayEvents) {
                message.append("• ").append(event.getTitle())
                        .append(" at ").append(event.getTime()).append("\n");
            }
            showInfo("Events", message.toString());
        }
    }

    private void loadUpcomingEvents() {
        if (upcomingEventsContainer == null) {
            System.err.println("WARNING: upcomingEventsContainer is null!");
            return;
        }

        upcomingEventsContainer.getChildren().clear();

        List<Event> sortedEvents = events.stream()
                .filter(e -> !e.getDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(Event::getDate))
                .limit(5)
                .toList();

        for (Event event : sortedEvents) {
            VBox eventItem = createEventItem(event);
            upcomingEventsContainer.getChildren().add(eventItem);
        }

        if (sortedEvents.isEmpty()) {
            Label noEvents = new Label("No upcoming events");
            noEvents.setStyle("-fx-font-size: 14px; -fx-text-fill: #999999; -fx-padding: 20px;");
            upcomingEventsContainer.getChildren().add(noEvents);
        }
    }

    private VBox createEventItem(Event event) {
        VBox eventBox = new VBox(5);
        eventBox.setStyle("-fx-background-color: white; -fx-padding: 12px; " +
                "-fx-background-radius: 8px; -fx-border-color: #E0E0E0; " +
                "-fx-border-radius: 8px; -fx-border-width: 1;");
        eventBox.setPadding(new Insets(12));

        Label titleLabel = new Label(event.getTitle());
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label dateLabel = new Label(event.getDate().toString() + " at " + event.getTime());
        dateLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");

        eventBox.getChildren().addAll(titleLabel, dateLabel);

        eventBox.setOnMouseClicked(e -> {
            showInfo("Event Details",
                    "Event: " + event.getTitle() + "\n" +
                            "Date: " + event.getDate() + "\n" +
                            "Time: " + event.getTime());
        });

        return eventBox;
    }

    @FXML
    private void handlePreviousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar();
        System.out.println("Previous month: " + currentYearMonth);
    }

    @FXML
    private void handleNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar();
        System.out.println("Next month: " + currentYearMonth);
    }

    @FXML
    private void handleAddEvent() {
        showInfo("Add Event", "Event creation dialog would open here.\n\n" +
                "You would be able to:\n" +
                "• Enter event title\n" +
                "• Select date and time\n" +
                "• Add location and description\n" +
                "• Set reminders");
    }

    // Event class
    public static class Event {
        private String title;
        private LocalDate date;
        private String time;

        public Event(String title, LocalDate date, String time) {
            this.title = title;
            this.date = date;
            this.time = time;
        }

        public String getTitle() { return title; }
        public LocalDate getDate() { return date; }
        public String getTime() { return time; }
    }
}