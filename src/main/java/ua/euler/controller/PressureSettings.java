package ua.euler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ua.euler.javaclass.servisClass.AlertAndInform;

import static ua.euler.controller.Controller.pressureNull;

public class PressureSettings {
    private double newPressure;
    AlertAndInform pb = new AlertAndInform();

    @FXML
    public TextField PressureInput;
    @FXML
    public Label labelPressureType;
    @FXML
    public Button SaveNewPressure, valueTip;

    public void onClickNewPressure(ActionEvent event) {
        if (labelPressureType.getText().equals("Па"))
            try {
                newPressure = Double.parseDouble(PressureInput.getText().replace(",", "."));
            } catch (NumberFormatException e) {
                pb.hd = "Помилка! ";
                pb.ct = "Невірний формат даних\n";
                pb.alert();
                PressureInput.setText("");
                return; }

        else if (labelPressureType.getText().equals("мм.рт.ст"))
            try {
                newPressure = Double.parseDouble(PressureInput.getText().replace(",", ".")) * 133.322;
            } catch (NumberFormatException e) {
                pb.hd = "Помилка! ";
                pb.ct = "Невірний формат даних\n";
                pb.alert();
                PressureInput.setText("");
                return; }

        pressureNull = newPressure;

        Stage stage = (Stage) SaveNewPressure.getScene().getWindow();
        stage.close();
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

    public void onClickReset(ActionEvent actionEvent) {
        PressureInput.setText("101325.0");
        valueTip.setText("В міліметрах ртутного стовба");
        labelPressureType.setText("Па");
    }
}
