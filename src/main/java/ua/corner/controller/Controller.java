package ua.corner.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ua.corner.javaclass.OpenStage;
import ua.corner.javaclass.PushButton;
import ua.corner.javaclass.SaveToWord;
import ua.corner.javaclass.Calculate;

import java.io.IOException;

public class Controller {
    PushButton pb = new PushButton();
    public Calculate calc = new Calculate();
    SaveToWord stw = new SaveToWord();
    OpenStage os = new OpenStage();


    @FXML
    public TextField wavelength, frequency, epr, crlength;

    public void onClick(ActionEvent actionEvent) {
        try {

                    calc.d = Double.parseDouble(wavelength.getText().replace(",", "."));
                    calc.a = Double.parseDouble(epr.getText().replace(",", "."));
            crlength.setText(Double.toString(calc.beam()).replace(".", ","));

        } catch (NumberFormatException e) {
            pb.alert();
        }
    }

    public void onClickDovBtn(ActionEvent actionEvent) {
        pb.hd = "Розрахунок довжини ребра кутового відбивача";
        pb.ct = "ВХІДНІ ДАНІ:\n \n Характеристика радіолокаційного сигналу:\n   - частота,ГГц; \n   - довжина хвилі, м.\n(Вводится лише одне будь-яке значення)\n\nЕфективна площа розсіювання (ЕПР),м.кв\n Тип кутового відбивача";
        pb.dovButton();
    }

    public void onClickCnclBtn(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void nClickChart(ActionEvent actionEvent) throws IOException {
        os.viewURL = "/view/chart.fxml";
        os.title = "Графік";
        os.openStage();
    }

    public void OnClickFile(ActionEvent actionEvent) throws IOException {
        stw.toWord();
    }

    public void onClick_menuExit(ActionEvent actionEvent) {
        System.exit(0);
    }


    public void OnClickNew(ActionEvent actionEvent) {
    }

    public void onClickOpenFile(ActionEvent actionEvent) {
    }


}


