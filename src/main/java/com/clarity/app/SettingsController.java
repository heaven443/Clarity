package com.clarity.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public class SettingsController {

    @FXML
    private HBox settingsNavItem;

    @FXML
    private Button privacySecurityButton;

    @FXML
    private Button savePasswordButton;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;


    @FXML
    public void initialize() {
        newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePasswordFields();
        });

        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePasswordFields();
        });

        validatePasswordFields();
    }

    @FXML
    private void handleDashboard(MouseEvent event) {
        System.out.println("Navigate to Dashboard");
    }

    @FXML
    private void handleMyTask(MouseEvent event) {
        System.out.println("Navigate to My Task");
    }

    @FXML
    private void handleNotes(MouseEvent event) {
        System.out.println("Navigate to Notes");
    }

    @FXML
    private void handleSchedule(MouseEvent event) {
        System.out.println("Navigate to Schedule");
    }

    @FXML
    private void handleSettings(MouseEvent event) {
        System.out.println("Already on Settings");
    }

    @FXML
    private void handleHelp(MouseEvent event) {
        System.out.println("Navigate to Help & Support");
    }

    @FXML
    private void handleLogout(MouseEvent event) {
        System.out.println("Logout clicked");
    }

    @FXML
    private void showAccountSettings(ActionEvent event) {
        System.out.println("Show Account Settings");
        updateActiveSettingsButton((Button) event.getSource());
    }

    @FXML
    private void showPrivacySettings(ActionEvent event) {
        System.out.println("Show Privacy & Security Settings");
        updateActiveSettingsButton((Button) event.getSource());
    }

    @FXML
    private void showAboutSettings(ActionEvent event) {
        System.out.println("Show About Us");
        updateActiveSettingsButton((Button) event.getSource());
    }

    @FXML
    private void handleSavePassword(ActionEvent event) {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in both password fields");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        if (newPassword.length() < 8) {
            showError("Password must be at least 8 characters long");
            return;
        }

        savePasswordButton.setDisable(true);
        savePasswordButton.setText("Saving...");


        System.out.println("Saving new password...");

        savePasswordButton.setDisable(false);
        savePasswordButton.setText("Save");
        clearPasswordFields();
    }

    @FXML
    private void handleDiscardPassword(ActionEvent event) {
        clearPasswordFields();
        System.out.println("Password changes discarded");
    }

    private void validatePasswordFields() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        boolean isValid = !newPassword.isEmpty()
                && !confirmPassword.isEmpty()
                && newPassword.equals(confirmPassword)
                && newPassword.length() >= 8;

        savePasswordButton.setDisable(!isValid);
    }

    private void clearPasswordFields() {
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void updateActiveSettingsButton(Button activeButton) {
        privacySecurityButton.getStyleClass().remove("settings-nav-button-active");

        if (!activeButton.getStyleClass().contains("settings-nav-button-active")) {
            activeButton.getStyleClass().add("settings-nav-button-active");
        }
    }

    private void showError(String message) {
        System.err.println("Error: " + message);
    }

    private void showSuccess(String message) {
        System.out.println("Success: " + message);
    }
}