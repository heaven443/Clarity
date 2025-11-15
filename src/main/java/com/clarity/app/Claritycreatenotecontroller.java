package com.clarity.app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Claritycreatenotecontroller extends BaseController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea contentArea;

    @FXML
    private HBox notesNavItem;

    private String noteId;
    private boolean isEditMode = false;
    private boolean hasUnsavedChanges = false;

    @Override
    public void initialize() {
        setupTextFieldListeners();
        setupPlaceholders();

        if (titleField.getText().equals("TITLE")) {
            titleField.setText("");
            titleField.setPromptText("TITLE");
        }

        System.out.println("ClarityCreateNoteController initialized");
    }

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

    private void setupPlaceholders() {
        if (titleField.getText().isEmpty()) {
            titleField.setPromptText("TITLE");
        }

        if (contentArea.getText().isEmpty()) {
            contentArea.setPromptText("Start typing your note...");
        }
    }

    public void loadNote(String noteId, String title, String content) {
        this.noteId = noteId;
        this.isEditMode = true;

        titleField.setText(title);
        contentArea.setText(content);

        hasUnsavedChanges = false;
    }

    public void createNewNote() {
        this.noteId = generateNoteId();
        this.isEditMode = false;

        titleField.clear();
        contentArea.clear();

        hasUnsavedChanges = false;
    }

    @FXML
    private void handleBack() {
        if (hasUnsavedChanges) {
            Optional<ButtonType> result = showUnsavedChangesDialog();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                saveNote();
            } else if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                return;
            }
        }

        sceneManager.switchTo(SceneManager.SceneType.NOTES);
    }

    @Override
    protected void handleDashboard() {
        if (confirmNavigation()) {
            super.handleDashboard();
        }
    }

    @Override
    protected void handleMyTask() {
        if (confirmNavigation()) {
            super.handleMyTask();
        }
    }

    @Override
    protected void handleNotes() {
        if (confirmNavigation()) {
            super.handleNotes();
        }
    }

    @Override
    protected void handleSchedule() {
        if (confirmNavigation()) {
            super.handleSchedule();
        }
    }

    @Override
    protected void handleSettings() {
        if (confirmNavigation()) {
            super.handleSettings();
        }
    }

    @Override
    protected void handleHelp() {
        if (confirmNavigation()) {
            super.handleHelp();
        }
    }

    @Override
    protected void handleLogout() {
        if (confirmNavigation()) {
            if (showConfirmation("Logout",
                    "Are you sure you want to logout?\nAny unsaved changes will be lost.")) {
                sessionManager.logout();
                sceneManager.switchTo(SceneManager.SceneType.LOGIN);
            }
        }
    }

    @FXML
    private void handleEditTitle() {
        titleField.requestFocus();
        titleField.selectAll();
    }

    @FXML
    private void handleEditContent() {
        contentArea.requestFocus();
        contentArea.selectAll();
    }

    @FXML
    private void handleDelete() {
        if (showConfirmation("Delete Note",
                "Are you sure you want to delete this note?\nThis action cannot be undone.")) {
            deleteNote();
            sceneManager.switchTo(SceneManager.SceneType.NOTES);
        }
    }

    private void saveNote() {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();

        if (title.isEmpty() && content.isEmpty()) {
            showWarning("Empty Note", "Cannot save an empty note.\nPlease add a title or content.");
            return;
        }

        System.out.println("Saving note:");
        System.out.println("ID: " + noteId);
        System.out.println("Title: " + title);
        System.out.println("Content: " + content);

        hasUnsavedChanges = false;

        showInfo("Note Saved", "Your note has been saved successfully.");
    }

    private void deleteNote() {
        System.out.println("Deleting note: " + noteId);

        showInfo("Note Deleted", "Your note has been deleted successfully.");
    }

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

    private Optional<ButtonType> showUnsavedChangesDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unsaved Changes");
        alert.setHeaderText("You have unsaved changes");
        alert.setContentText("Do you want to save your changes?");

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");
        ButtonType buttonCancel = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);

        return alert.showAndWait();
    }

    private String generateNoteId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "NOTE_" + LocalDateTime.now().format(formatter);
    }

    public String getNoteTitle() {
        return titleField.getText();
    }

    public String getNoteContent() {
        return contentArea.getText();
    }

    public String getNoteId() {
        return noteId;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }
}