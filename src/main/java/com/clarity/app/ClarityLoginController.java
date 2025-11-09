package com.clarity.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class ClarityLoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLoginClick(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.trim().isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Failed", "Please enter your email and password.");
            return;
        }

        if (!authenticate(email, password)) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password. Please try again.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("clarityTaskView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Clarity - Task View");

            String cssPath = getClass().getResource("style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);

            stage.setResizable(true);
            stage.setMaximized(true);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,
                    "Error Loading View",
                    "Could not load the task view. Please ensure 'clarityTaskView.fxml' and 'style.css' are in the 'com/clarity/app' resource package.");
        }
    }

    private boolean authenticate(String email, String password) {
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}