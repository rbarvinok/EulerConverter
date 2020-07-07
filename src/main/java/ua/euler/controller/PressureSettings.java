package ua.euler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static ua.euler.javaclass.PressureToVelocityConvector.pressureNull;

public class PressureSettings {
    private double newPressure;

    @FXML
    public TextField PressureInput;
    @FXML
    public Label labelPressureType;
    @FXML
    public Button SaveNewPressure, valueTip;

    public void onClickNewPressure(ActionEvent event) {
        if (labelPressureType.getText().equals("Па"))

            newPressure = Double.parseDouble(PressureInput.getText());
        else if (labelPressureType.getText().equals("мм.рт.ст"))
            newPressure = Double.parseDouble(PressureInput.getText()) * 133.322;

        pressureNull = newPressure;

    }

    public void onClickValueTip(ActionEvent event) {
        if (labelPressureType.getText().equals("мм.рт.ст")) {
            valueTip.setText("В міліметрах ртутного стовба");
            labelPressureType.setText("Па");
        } else {
            labelPressureType.setText("мм.рт.ст");
            valueTip.setText("В Паскалях");
        }
    }

}
