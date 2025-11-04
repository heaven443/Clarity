package com.example;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ClarityNoteController {

    @FXML
    private VBox noteListContainer;

    @FXML
    public void initialize() {
        System.out.println("Clarity Note screen initialized.");
    }

    @FXML
    private void handleNewNote() {
        System.out.println("New Note button clicked.");
    }

}