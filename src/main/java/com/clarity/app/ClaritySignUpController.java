package com.clarity.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Controller for the Sign Up view in Clarity application
 */
public class ClaritySignUpController {

    // ================================================
    // FXML Injected Fields
    // ================================================

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> countryCodeCombo;

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button signUpButton;

    // ================================================
    // Instance Variables
    // ================================================

    private Stage stage;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Phone validation pattern (digits only)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");

    // ================================================
    // Initialization
    // ================================================

    /**
     * Initialize the controller
     */
    @FXML
    public void initialize() {
        // Set default country code
        if (countryCodeCombo != null && countryCodeCombo.getValue() == null) {
            countryCodeCombo.setValue("+63");
        }

        // Add real-time validation listeners
        setupValidation();

        System.out.println("ClaritySignUpController initialized");
    }

    /**
     * Setup real-time validation for input fields
     */
    private void setupValidation() {
        // Email validation
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (isValidEmail(newValue)) {
                    emailField.getStyleClass().removeAll("text-field-error");
                    emailField.getStyleClass().add("text-field-success");
                } else {
                    emailField.getStyleClass().removeAll("text-field-success");
                    emailField.getStyleClass().add("text-field-error");
                }
            } else {
                emailField.getStyleClass().removeAll("text-field-error", "text-field-success");
            }
        });

        // Phone validation
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Only allow digits
            if (!newValue.matches("\\d*")) {
                phoneField.setText(oldValue);
            }
        });

        // Password confirmation validation
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (newValue.equals(passwordField.getText())) {
                    confirmPasswordField.getStyleClass().removeAll("password-field-error");
                    confirmPasswordField.getStyleClass().add("password-field-success");
                } else {
                    confirmPasswordField.getStyleClass().removeAll("password-field-success");
                    confirmPasswordField.getStyleClass().add("password-field-error");
                }
            } else {
                confirmPasswordField.getStyleClass().removeAll("password-field-error", "password-field-success");
            }
        });
    }

    // ================================================
    // Action Handlers
    // ================================================

    /**
     * Handle sign up button click
     */
    @FXML
    private void handleSignUpClick() {
        // Validate all fields
        if (validateForm()) {
            // Get form data
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String countryCode = countryCodeCombo.getValue();
            String phone = phoneField.getText().trim();
            String password = passwordField.getText();

            // TODO: Implement actual sign up logic (API call, database, etc.)
            System.out.println("Sign Up Data:");
            System.out.println("Full Name: " + fullName);
            System.out.println("Email: " + email);
            System.out.println("Phone: " + countryCode + " " + phone);
            System.out.println("Password: [HIDDEN]");

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sign Up Successful");
            alert.setHeaderText("Welcome to Clarity!");
            alert.setContentText("Your account has been created successfully.\nYou can now login with your credentials.");
            alert.showAndWait();

            // Navigate to login page
            handleBackToLogin();
        }
    }

    /**
     * Handle back to login button
     */
    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("clarityLogin.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            if (stage == null) {
                stage = (Stage) signUpButton.getScene().getWindow();
            }

            stage.setScene(scene);
            stage.setTitle("Login - Clarity");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Could not load the login page.",
                    "Error: " + e.getMessage());
        }
    }

    // ================================================
    // Validation Methods
    // ================================================

    /**
     * Validate the entire form
     */
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        // Validate full name
        if (fullNameField.getText().trim().isEmpty()) {
            errors.append("• Full name is required\n");
            fullNameField.getStyleClass().add("text-field-error");
        } else {
            fullNameField.getStyleClass().removeAll("text-field-error");
        }

        // Validate email
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            errors.append("• Email is required\n");
            emailField.getStyleClass().add("text-field-error");
        } else if (!isValidEmail(email)) {
            errors.append("• Please enter a valid email address\n");
            emailField.getStyleClass().add("text-field-error");
        } else {
            emailField.getStyleClass().removeAll("text-field-error");
        }

        // Validate phone number
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            errors.append("• Phone number is required\n");
            phoneField.getStyleClass().add("text-field-error");
        } else if (!isValidPhone(phone)) {
            errors.append("• Please enter a valid phone number (10-15 digits)\n");
            phoneField.getStyleClass().add("text-field-error");
        } else {
            phoneField.getStyleClass().removeAll("text-field-error");
        }

        // Validate password
        String password = passwordField.getText();
        if (password.isEmpty()) {
            errors.append("• Password is required\n");
            passwordField.getStyleClass().add("password-field-error");
        } else if (password.length() < 6) {
            errors.append("• Password must be at least 6 characters\n");
            passwordField.getStyleClass().add("password-field-error");
        } else {
            passwordField.getStyleClass().removeAll("password-field-error");
        }

        // Validate confirm password
        String confirmPassword = confirmPasswordField.getText();
        if (confirmPassword.isEmpty()) {
            errors.append("• Please confirm your password\n");
            confirmPasswordField.getStyleClass().add("password-field-error");
        } else if (!confirmPassword.equals(password)) {
            errors.append("• Passwords do not match\n");
            confirmPasswordField.getStyleClass().add("password-field-error");
        } else {
            confirmPasswordField.getStyleClass().removeAll("password-field-error");
        }

        // Show errors if any
        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Please fix the following errors:",
                    errors.toString());
            return false;
        }

        return true;
    }

    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate phone number format
     */
    private boolean isValidPhone(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    // ================================================
    // Helper Methods
    // ================================================

    /**
     * Show an alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        if (content != null) {
            alert.setContentText(content);
        }
        alert.showAndWait();
    }

    /**
     * Set the stage for this controller
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Clear all form fields
     */
    public void clearForm() {
        fullNameField.clear();
        emailField.clear();
        phoneField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        countryCodeCombo.setValue("+63");

        // Remove validation styles
        fullNameField.getStyleClass().removeAll("text-field-error", "text-field-success");
        emailField.getStyleClass().removeAll("text-field-error", "text-field-success");
        phoneField.getStyleClass().removeAll("text-field-error", "text-field-success");
        passwordField.getStyleClass().removeAll("password-field-error", "password-field-success");
        confirmPasswordField.getStyleClass().removeAll("password-field-error", "password-field-success");
    }
}
