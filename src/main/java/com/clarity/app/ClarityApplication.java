package com.clarity.app;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClarityApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.init(primaryStage);

        sceneManager.switchTo(SceneManager.SceneType.LOGIN);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        SessionManager.getInstance().logout();
        System.out.println("Clarity application closed");
    }
}