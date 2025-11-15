package com.clarity.app;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;

public abstract class BaseController {

    protected Stage stage;
    protected SceneManager sceneManager;
    protected SessionManager sessionManager;

    public BaseController() {
        this.sceneManager = SceneManager.getInstance();
        this.sessionManager = SessionManager.getInstance();
    }

    public void initialize() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    protected void handleDashboard() {
        sceneManager.switchTo(SceneManager.SceneType.DASHBOARD);
    }

    protected void handleMyTask() {
        sceneManager.switchTo(SceneManager.SceneType.TASK_VIEW);
    }

    protected void handleNotes() {
        sceneManager.switchTo(SceneManager.SceneType.NOTES);
    }

    protected void handleSchedule() {
        sceneManager.switchTo(SceneManager.SceneType.SCHEDULE);
    }

    protected void handleSettings() {
        sceneManager.switchTo(SceneManager.SceneType.SETTINGS);
    }

    protected void handleHelp() {
        sceneManager.switchTo(SceneManager.SceneType.HELP);
    }

    protected void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            sessionManager.logout();
            sceneManager.switchTo(SceneManager.SceneType.LOGIN);
        }
    }

    protected void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        if (content != null) {
            alert.setContentText(content);
        }
        alert.showAndWait();
    }

    protected void showError(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, null, message);
    }

    protected void showInfo(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, null, message);
    }

    protected void showWarning(String title, String message) {
        showAlert(Alert.AlertType.WARNING, title, null, message);
    }

    protected boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    protected SessionManager.User getCurrentUser() {
        return sessionManager.getCurrentUser();
    }

    protected boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    protected void requireLogin() {
        if (!isLoggedIn()) {
            showWarning("Authentication Required", "Please login to continue");
            sceneManager.switchTo(SceneManager.SceneType.LOGIN);
        }
    }
}