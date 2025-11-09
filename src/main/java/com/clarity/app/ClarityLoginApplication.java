package com.clarity.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClarityLoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClarityLoginApplication.class.getResource("clarityLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        String cssPath = getClass().getResource("clarity-login.css").toExternalForm();
        scene.getStylesheets().add(cssPath);

        stage.setTitle("Clarity - Login");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setResizable(false);
        stage.show();
    }
}
