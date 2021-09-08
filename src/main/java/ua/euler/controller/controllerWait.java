package ua.euler.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

public class controllerWait {
    @FXML
    public Button button;
    public ProgressIndicator pr;

    public void closeWait() {
        //button.fire();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
