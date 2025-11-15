package com.clarity.app;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.regex.Pattern;

public class ClaritySignUpController extends BaseController {

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

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");

    @Override
    public void initialize() {
        if (countryCodeCombo != null && countryCodeCombo.getValue() == null) {
            countryCodeCombo.setValue("+63");
        }

        setupValidation();

        System.out.println("ClaritySignUpController initialized");
    }

    private void setupValidation() {
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

        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneField.setText(oldValue);
            }
        });

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

    @FXML
    private void handleSignUpClick() {
        if (validateForm()) {
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String countryCode = countryCodeCombo.getValue();
            String phone = phoneField.getText().trim();
            String password = passwordField.getText();

            System.out.println("Sign Up Data:");
            System.out.println("Full Name: " + fullName);
            System.out.println("Email: " + email);
            System.out.println("Phone: " + countryCode + " " + phone);

            showInfo("Sign Up Successful",
                    "Welcome to Clarity!\nYour account has been created successfully.\nYou can now login with your credentials.");

            handleBackToLogin();
        }
    }

    @FXML
    private void handleBackToLogin() {
        sceneManager.switchTo(SceneManager.SceneType.LOGIN);
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (fullNameField.getText().trim().isEmpty()) {
            errors.append("• Full name is required\n");
            fullNameField.getStyleClass().add("text-field-error");
        } else {
            fullNameField.getStyleClass().removeAll("text-field-error");
        }

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

        if (errors.length() > 0) {
            showError("Validation Error",
                    "Please fix the following errors:\n\n" + errors.toString());
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    public void clearForm() {
        fullNameField.clear();
        emailField.clear();
        phoneField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        countryCodeCombo.setValue("+63");

        fullNameField.getStyleClass().removeAll("text-field-error", "text-field-success");
        emailField.getStyleClass().removeAll("text-field-error", "text-field-success");
        phoneField.getStyleClass().removeAll("text-field-error", "text-field-success");
        passwordField.getStyleClass().removeAll("password-field-error", "password-field-success");
        confirmPasswordField.getStyleClass().removeAll("password-field-error", "password-field-success");
    }
}