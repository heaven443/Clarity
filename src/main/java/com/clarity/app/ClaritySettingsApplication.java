package com.clarity.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClaritySettingsApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClaritySettingsApplication.class.getResource("claritySettings.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);

        String cssPath = getClass().getResource("app.css").toExternalForm();
        scene.getStylesheets().add(cssPath);

        stage.setTitle("Clarity - Settings");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.show();
    }
}
