package com.clarity.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class ClaritySettingsController extends BaseController {

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button myTaskButton;

    @FXML
    private Button privacySecurityButton;

    @FXML
    private Button savePasswordButton;

    @Override
    public void initialize() {
        System.out.println("Settings Controller initialized");

        if (newPasswordField != null && confirmPasswordField != null) {
            setupPasswordValidation();
        }
    }

    private void setupPasswordValidation() {
        newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePasswordFields();
        });

        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePasswordFields();
        });

        validatePasswordFields();
    }

    @FXML
    private void handleMyTaskClick() {
        handleMyTask();
    }

    @FXML
    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Photo");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            showInfo("Photo Upload", "Photo selected: " + selectedFile.getName());
            System.out.println("Selected photo: " + selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleSave() {
        String fullName = fullNameField != null ? fullNameField.getText().trim() : "";

        if (fullName.isEmpty()) {
            showError("Validation Error", "Please enter your full name.");
            return;
        }

        showInfo("Settings Saved", "Your profile has been updated successfully!");
        System.out.println("Saving settings for: " + fullName);
    }

    @FXML
    private void showAccountSettings() {
        System.out.println("Show Account Settings");
        updateActiveSettingsButton(null);
    }

    @FXML
    private void showPrivacySettings() {
        System.out.println("Show Privacy & Security Settings");
        sceneManager.switchTo(SceneManager.SceneType.PRIVACY_SECURITY);
    }

    @FXML
    private void showAboutSettings() {
        System.out.println("Show About Us");
        sceneManager.switchTo(SceneManager.SceneType.ABOUT_US);
    }

    @FXML
    private void handleSavePassword() {
        if (newPasswordField == null || confirmPasswordField == null) {
            return;
        }

        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("Error", "Please fill in both password fields");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("Error", "Passwords do not match");
            return;
        }

        if (newPassword.length() < 8) {
            showError("Error", "Password must be at least 8 characters long");
            return;
        }

        if (savePasswordButton != null) {
            savePasswordButton.setDisable(true);
            savePasswordButton.setText("Saving...");
        }

        System.out.println("Saving new password...");

        if (savePasswordButton != null) {
            savePasswordButton.setDisable(false);
            savePasswordButton.setText("Save");
        }
        clearPasswordFields();

        showInfo("Success", "Password has been updated successfully!");
    }

    @FXML
    private void handleDiscardPassword() {
        clearPasswordFields();
        System.out.println("Password changes discarded");
    }

    private void validatePasswordFields() {
        if (newPasswordField == null || confirmPasswordField == null || savePasswordButton == null) {
            return;
        }

        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        boolean isValid = !newPassword.isEmpty()
                && !confirmPassword.isEmpty()
                && newPassword.equals(confirmPassword)
                && newPassword.length() >= 8;

        savePasswordButton.setDisable(!isValid);
    }

    private void clearPasswordFields() {
        if (newPasswordField != null) {
            newPasswordField.clear();
        }
        if (confirmPasswordField != null) {
            confirmPasswordField.clear();
        }
    }

    private void updateActiveSettingsButton(Button activeButton) {
        if (privacySecurityButton != null) {
            privacySecurityButton.getStyleClass().remove("settings-nav-button-active");
        }

        if (activeButton != null && !activeButton.getStyleClass().contains("settings-nav-button-active")) {
            activeButton.getStyleClass().add("settings-nav-button-active");
        }
    }
}