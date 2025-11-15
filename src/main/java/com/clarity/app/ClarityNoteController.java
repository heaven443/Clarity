package com.clarity.app;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ClarityNoteController extends BaseController {

    @FXML
    private VBox noteListContainer;

    @Override
    public void initialize() {
        System.out.println("Clarity Note screen initialized.");
    }

    @FXML
    private void handleNewNote() {
        System.out.println("New Note button clicked.");
        sceneManager.switchTo(SceneManager.SceneType.CREATE_NOTE);
    }

}