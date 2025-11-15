package com.clarity.app;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ClarityLoginController extends BaseController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize() {
        System.out.println("ClarityLoginController initialized");
    }

    @FXML
    private void handleLoginClick() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showWarning("Login Failed", "Please enter your email and password.");
            return;
        }

        if (!authenticate(email, password)) {
            showError("Login Failed", "Invalid email or password. Please try again.");
            return;
        }

        sessionManager.login("user_123", email, email);

        sceneManager.switchTo(SceneManager.SceneType.DASHBOARD);
    }

    @FXML
    private void handleSignUpClick() {
        sceneManager.switchTo(SceneManager.SceneType.SIGNUP);
    }

    @FXML
    private void handleForgotPasswordClick() {
        showInfo("Forgot Password", "Password reset functionality coming soon!");
    }

    private boolean authenticate(String email, String password) {
        return true;
    }
}