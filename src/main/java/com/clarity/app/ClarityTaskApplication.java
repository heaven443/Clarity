package com.clarity.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClarityTaskApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClarityTaskApplication.class.getResource("clarityTaskView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);

        stage.setTitle("Clarity - Task Viewer");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.setMaximized(true);
        stage.show();
    }
}
