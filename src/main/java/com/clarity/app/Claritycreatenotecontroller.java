package com.clarity.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Controller for the Create/Edit Note view in Clarity application
 */
public class Claritycreatenotecontroller {

    // ================================================
    // FXML Injected Fields
    // ================================================

    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentArea;

    @FXML
    private HBox notesNavItem;

    // ================================================
    // Instance Variables
    // ================================================

    private Stage stage;
    private String noteId;
    private boolean isEditMode = false;
    private boolean hasUnsavedChanges = false;

    // ================================================
    // Initialization
    // ================================================

    /**
     * Initialize the controller
     * Called automatically after FXML loading
     */
    @FXML
    public void initialize() {
        setupTextFieldListeners();
        setupPlaceholders();

        // Set default title if creating new note
        if (titleField.getText().equals("TITLE")) {
            titleField.setText("");
            titleField.setPromptText("TITLE");
        }

        System.out.println("ClarityCreateNoteController initialized");
    }

    /**
     * Setup listeners to track changes
     */
    private void setupTextFieldListeners() {
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                hasUnsavedChanges = true;
            }
        });

        contentArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                hasUnsavedChanges = true;
            }
        });
    }

    /**
     * Setup placeholder text
     */
    private void setupPlaceholders() {
        if (titleField.getText().isEmpty()) {
            titleField.setPromptText("TITLE");
        }

        if (contentArea.getText().isEmpty()) {
            contentArea.setPromptText("Start typing your note...");
        }
    }

    // ================================================
    // Public Methods
    // ================================================

    /**
     * Set the stage for this controller
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Load an existing note for editing
     */
    public void loadNote(String noteId, String title, String content) {
        this.noteId = noteId;
        this.isEditMode = true;

        titleField.setText(title);
        contentArea.setText(content);

        hasUnsavedChanges = false;
    }

    /**
     * Create a new note
     */
    public void createNewNote() {
        this.noteId = generateNoteId();
        this.isEditMode = false;

        titleField.clear();
        contentArea.clear();

        hasUnsavedChanges = false;
    }

    // ================================================
    // Navigation Handlers
    // ================================================

    /**
     * Handle back button - return to notes list
     */
    @FXML
    private void handleBack() {
        if (hasUnsavedChanges) {
            Optional<ButtonType> result = showUnsavedChangesDialog();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                saveNote();
            } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                return; // Don't navigate away
            }
        }

        navigateToView("clarityNotes.fxml", "Notes - Clarity");
    }

    /**
     * Navigate to Dashboard
     */
    @FXML
    private void handleDashboard() {
        if (confirmNavigation()) {
            navigateToView("clarityDashboard.fxml", "Dashboard - Clarity");
        }
    }

    /**
     * Navigate to My Task
     */
    @FXML
    private void handleMyTask() {
        if (confirmNavigation()) {
            navigateToView("clarityMyTask.fxml", "My Task - Clarity");
        }
    }

    /**
     * Navigate to Notes
     */
    @FXML
    private void handleNotes() {
        if (confirmNavigation()) {
            navigateToView("clarityNotes.fxml", "Notes - Clarity");
        }
    }

    /**
     * Navigate to Schedule
     */
    @FXML
    private void handleSchedule() {
        if (confirmNavigation()) {
            navigateToView("claritySchedule.fxml", "Schedule - Clarity");
        }
    }

    /**
     * Navigate to Settings
     */
    @FXML
    private void handleSettings() {
        if (confirmNavigation()) {
            navigateToView("claritySettings.fxml", "Settings - Clarity");
        }
    }

    /**
     * Navigate to Help & Support
     */
    @FXML
    private void handleHelp() {
        if (confirmNavigation()) {
            navigateToView("clarityHelp.fxml", "Help & Support - Clarity");
        }
    }

    /**
     * Handle logout
     */
    @FXML
    private void handleLogout() {
        if (confirmNavigation()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Are you sure you want to logout?");
            alert.setContentText("Any unsaved changes will be lost.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                navigateToView("clarityLogin.fxml", "Login - Clarity");
            }
        }
    }

    // ================================================
    // Note Action Handlers
    // ================================================

    /**
     * Handle title edit button
     */
    @FXML
    private void handleEditTitle() {
        titleField.requestFocus();
        titleField.selectAll();
    }

    /**
     * Handle content edit button
     */
    @FXML
    private void handleEditContent() {
        contentArea.requestFocus();
        contentArea.selectAll();
    }

    /**
     * Handle delete button
     */
    @FXML
    private void handleDelete() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Note");
        alert.setHeaderText("Are you sure you want to delete this note?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteNote();
            navigateToView("clarityNotes.fxml", "Notes - Clarity");
        }
    }

    // ================================================
    // Helper Methods
    // ================================================

    /**
     * Save the current note
     */
    private void saveNote() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() && content.isEmpty()) {
            showAlert(AlertType.WARNING, "Empty Note",
                    "Cannot save an empty note.",
                    "Please add a title or content.");
            return;
        }

        // TODO: Implement actual save logic to database/file
        // For now, just simulate saving
        System.out.println("Saving note:");
        System.out.println("ID: " + noteId);
        System.out.println("Title: " + title);
        System.out.println("Content: " + content);

        hasUnsavedChanges = false;

        showAlert(AlertType.INFORMATION, "Note Saved",
                "Your note has been saved successfully.", null);
    }

    /**
     * Delete the current note
     */
    private void deleteNote() {
        // TODO: Implement actual delete logic from database/file
        System.out.println("Deleting note: " + noteId);

        showAlert(AlertType.INFORMATION, "Note Deleted",
                "Your note has been deleted successfully.", null);
    }

    /**
     * Confirm navigation if there are unsaved changes
     */
    private boolean confirmNavigation() {
        if (hasUnsavedChanges) {
            Optional<ButtonType> result = showUnsavedChangesDialog();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                saveNote();
                return true;
            } else if (result.isPresent() && result.get() == ButtonType.NO) {
                return true;
            } else {
                return false; // Cancel
            }
        }
        return true;
    }

    /**
     * Show unsaved changes dialog
     */
    private Optional<ButtonType> showUnsavedChangesDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Unsaved Changes");
        alert.setHeaderText("You have unsaved changes");
        alert.setContentText("Do you want to save your changes?");

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");
        ButtonType buttonCancel = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);

        return alert.showAndWait();
    }

    /**
     * Navigate to a different view
     */
    private void navigateToView(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            if (stage == null) {
                stage = (Stage) titleField.getScene().getWindow();
            }

            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Navigation Error",
                    "Could not load the requested view.",
                    "Error: " + e.getMessage());
        }
    }

    /**
     * Generate a unique note ID
     */
    private String generateNoteId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "NOTE_" + LocalDateTime.now().format(formatter);
    }

    /**
     * Show an alert dialog
     */
    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        if (content != null) {
            alert.setContentText(content);
        }
        alert.showAndWait();
    }

    /**
     * Get the note title
     */
    public String getNoteTitle() {
        return titleField.getText();
    }

    /**
     * Get the note content
     */
    public String getNoteContent() {
        return contentArea.getText();
    }

    /**
     * Get the note ID
     */
    public String getNoteId() {
        return noteId;
    }

    /**
     * Check if in edit mode
     */
    public boolean isEditMode() {
        return isEditMode;
    }

    /**
     * Check if there are unsaved changes
     */
    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }
}
