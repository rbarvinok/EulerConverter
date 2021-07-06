package ua.euler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import ua.euler.javaclass.GetSettings;
import ua.euler.javaclass.servisClass.AlertAndInform;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ResourceBundle;

import static ua.euler.controller.Controller.pressureNull;
import static ua.euler.javaclass.QuaternionToEulerAnglesConvectorNonNormalised.period;

public class SettingsController implements Initializable {
    private double newPressure;
    AlertAndInform inform = new AlertAndInform();
    GetSettings getSettings = new GetSettings();

    @FXML
    public TextField pressureInput;
    @FXML
    public TextField periodLabel;
    @FXML
    public Label labelPressureType;
    @FXML
    public Button SaveNewSettings, valueTip;

    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        getSettings.getSettings();
        pressureInput.setText(String.valueOf(pressureNull));
        periodLabel.setText(String.valueOf(period));
    }

    public void onClickNewSettings(ActionEvent event) throws IOException {
        if (labelPressureType.getText().equals("Па"))
            try {
                newPressure = Double.parseDouble(pressureInput.getText().replace(",", "."));
            } catch (NumberFormatException e) {
                inform.hd = "Помилка! ";
                inform.ct = "Невірний формат даних\n";
                inform.alert();
                pressureInput.setText("");
                return;
            }

        else if (labelPressureType.getText().equals("мм.рт.ст"))
            try {
                newPressure = Double.parseDouble(pressureInput.getText().replace(",", ".")) * 133.322;
            } catch (NumberFormatException e) {
                inform.hd = "Помилка! ";
                inform.ct = "Невірний формат даних\n";
                inform.alert();
                pressureInput.setText("");
                return;
            }

        pressureNull = newPressure;
        period = Integer.parseInt(periodLabel.getText());

        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("settings.txt", false), "Cp1251");
        osw.write("PressureNull=" + pressureNull);
        osw.write("\n");
        osw.write("Period=" + period);
        osw.close();

        Stage stage = (Stage) SaveNewSettings.getScene().getWindow();
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
        pressureInput.setText("101325.0");
        valueTip.setText("В міліметрах ртутного стовба");
        labelPressureType.setText("Па");
        periodLabel.setText("21");
    }
}
