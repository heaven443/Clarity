package com.clarity.app;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {

    private static SceneManager instance;

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public enum SceneType {
        LOGIN("clarityLogin.fxml", "Login - Clarity", 900, 600, false),
        SIGNUP("claritySignUp.fxml", "Sign Up - Clarity", 900, 600, false),
        DASHBOARD("clarityDashboard.fxml", "Dashboard - Clarity", 900, 600, true),
        TASK_VIEW("clarityTaskView.fxml", "My Tasks - Clarity", 900, 600, true),
        COMPLETED_TASKS("clarityCompletedTask.fxml", "Completed Tasks - Clarity", 900, 600, true),
        OVERDUE_TASKS("clarityOverdueTask.fxml", "Overdue Tasks - Clarity", 900, 600, true),
        NOTES("clarityNotes.fxml", "Notes - Clarity", 900, 600, true),
        CREATE_NOTE("clarityCreateNote.fxml", "Create Note - Clarity", 900, 600, true),
        SCHEDULE("claritySchedule.fxml", "Schedule - Clarity", 900, 600, true),
        SETTINGS("claritySettings.fxml", "Settings - Clarity", 900, 600, true),
        PRIVACY_SECURITY("privacy-n-security.fxml", "Privacy & Security - Clarity", 900, 600, true),
        ABOUT_US("clarityAboutUS.fxml", "About Us - Clarity", 900, 600, true),
        HELP("clarityHelp.fxml", "Help & Support - Clarity", 900, 600, true);

        private final String fxmlFile;
        private final String title;
        private final int width;
        private final int height;
        private final boolean isMaximizable;

        SceneType(String fxmlFile, String title, int width, int height, boolean isMaximizable) {
            this.fxmlFile = fxmlFile;
            this.title = title;
            this.width = width;
            this.height = height;
            this.isMaximizable = isMaximizable;
        }

        public String getFxmlFile() { return fxmlFile; }
        public String getTitle() { return title; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }
        public boolean isMaximizable() { return isMaximizable; }
    }

    private Stage primaryStage;
    private Map<SceneType, Object> controllerCache; // Cache controllers instead of nodes
    private Map<SceneType, String> fxmlPathCache; // Cache FXML paths for performance
    private SceneType currentScene;
    private boolean enableTransitions = true;
    private boolean enableCaching = true;

    private SceneManager() {
        this.controllerCache = new HashMap<>();
        this.fxmlPathCache = new HashMap<>();
    }

    public void init(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setMinWidth(900);
        this.primaryStage.setMinHeight(600);
    }

    public void switchTo(SceneType sceneType) {
        try {
            Parent root = loadScene(sceneType); // This now always returns a NEW instance
            Scene newScene = new Scene(root, sceneType.getWidth(), sceneType.getHeight());

            if (enableTransitions && primaryStage.getScene() != null) {
                applyTransition(root, () -> setScene(newScene, sceneType));
            } else {
                setScene(newScene, sceneType);
            }

        } catch (IOException e) {
            System.err.println("Error loading scene: " + sceneType.getFxmlFile());
            e.printStackTrace();
            showErrorScene(sceneType, e);
        }
    }

    private Parent loadScene(SceneType sceneType) throws IOException {
        // ALWAYS create a new Parent instance - no node caching
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneType.getFxmlFile()));
        Parent root = loader.load();

        Object controller = loader.getController();
        injectDependencies(controller);

        // Cache the controller for state preservation, NOT the Parent node
        if (enableCaching && controller != null) {
            controllerCache.put(sceneType, controller);
        }

        return root;
    }

    private void setScene(Scene scene, SceneType sceneType) {
        // Set the scene first
        primaryStage.setScene(scene);
        primaryStage.setTitle(sceneType.getTitle());

        primaryStage.setResizable(sceneType.isMaximizable());

        // Force resize for maximizable scenes to ensure proper layout
        if (sceneType.isMaximizable()) {
            // Reset maximized state to force proper resizing
            primaryStage.setMaximized(false);
            primaryStage.setMaximized(true);
        } else {
            // Ensure non-maximizable scenes are not maximized
            primaryStage.setMaximized(false);
        }

        currentScene = sceneType;

        if (!primaryStage.isShowing()) {
            primaryStage.show();
        }
    }

    private void applyTransition(Parent root, Runnable onFinish) {
        root.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setOnFinished(e -> onFinish.run());

        fadeIn.play();
    }

    private void injectDependencies(Object controller) {
        // Inject Stage
        try {
            controller.getClass()
                    .getMethod("setStage", Stage.class)
                    .invoke(controller, primaryStage);
        } catch (Exception e) {
            // Method not found - ignore
        }

        // Inject SceneManager
        try {
            controller.getClass()
                    .getMethod("setSceneManager", SceneManager.class)
                    .invoke(controller, this);
        } catch (Exception e) {
            // Method not found - ignore
        }
    }

    private void showErrorScene(SceneType failedScene, Exception e) {
        try {
            javafx.scene.control.Label errorLabel = new javafx.scene.control.Label(
                    "Error loading " + failedScene.getTitle() + "\n\n" +
                            "Details: " + e.getMessage() + "\n\n" +
                            "Please check that the FXML file exists at:\n" +
                            failedScene.getFxmlFile()
            );
            errorLabel.setStyle(
                    "-fx-padding: 40; " +
                            "-fx-font-size: 14px; " +
                            "-fx-text-fill: #EF5350; " +
                            "-fx-background-color: #FFEBEE;"
            );
            errorLabel.setWrapText(true);

            Scene errorScene = new Scene(errorLabel, 800, 600);
            primaryStage.setScene(errorScene);
            primaryStage.setTitle("Error - Clarity");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get a controller from cache (if caching is enabled)
     */
    @SuppressWarnings("unchecked")
    public <T> T getController(SceneType sceneType) {
        return (T) controllerCache.get(sceneType);
    }

    /**
     * Clear all cached controllers
     */
    public void clearCache() {
        controllerCache.clear();
    }

    /**
     * Clear cached controller for a specific scene type
     */
    public void clearSceneCache(SceneType sceneType) {
        controllerCache.remove(sceneType);
    }

    /**
     * Preload scenes - this only caches controllers, not nodes
     */
    public void preloadScenes(SceneType... scenes) {
        for (SceneType scene : scenes) {
            try {
                loadScene(scene); // This will cache the controller if enabled
            } catch (IOException e) {
                System.err.println("Failed to preload: " + scene.getFxmlFile());
            }
        }
    }

    /**
     * Force refresh the current scene's layout (useful for resize issues)
     */
    public void refreshCurrentScene() {
        if (currentScene != null && currentScene.isMaximizable()) {
            primaryStage.setMaximized(false);
            primaryStage.setMaximized(true);
        }
    }

    /**
     * Switch to scene with forced layout refresh
     */
    public void switchToWithRefresh(SceneType sceneType) {
        // Force unmaximize before switch if currently maximized
        if (primaryStage.isMaximized()) {
            primaryStage.setMaximized(false);
        }

        switchTo(sceneType);
    }

    public void setEnableTransitions(boolean enable) {
        this.enableTransitions = enable;
    }

    public void setEnableCaching(boolean enable) {
        this.enableCaching = enable;
        if (!enable) {
            clearCache(); // Clear controllers when caching is disabled
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public SceneType getCurrentScene() {
        return currentScene;
    }

    public boolean isTransitionsEnabled() {
        return enableTransitions;
    }

    public boolean isCachingEnabled() {
        return enableCaching;
    }
}