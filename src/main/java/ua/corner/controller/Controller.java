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
    public Calculate wd = new Calculate();
    SaveToWord stw = new SaveToWord();
    OpenStage os = new OpenStage();

    @FXML
    public TextField ang, dist, wid;

    public void onClick(ActionEvent actionEvent) {
        try {
            if (Double.parseDouble(ang.getText().replace(",", ".")) <= 9.5) {
                if (Double.parseDouble(dist.getText().replace(",", ".")) <= 270) {
                    wd.d = Double.parseDouble(dist.getText().replace(",", "."));
                    wd.a = Double.parseDouble(ang.getText().replace(",", "."));
                    wid.setText(Double.toString(wd.beam()).replace(".", ","));
                } else pb.alert();
            } else pb.alert();
        } catch (NumberFormatException e) {
            pb.alert();
        }
    }

    public void onClickDovBtn(ActionEvent actionEvent) {
        pb.hd = "Розрахунок розміру ребра \nкутового відбивача";
        pb.ct = "ВХІДНІ ДАНІ:\n Характеристика радіолокаційного сигналу\n   - частота,ГГц; \n   - довжина хвилі, м.\n(Вводится лише одне будь-яке значення)\nЕфективна площа розсіювання (ЕПР),м.кв\n Тип кутового відбивача";
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

    public void OnClickSave(ActionEvent actionEvent) {
    }

    public void OnClickNew(ActionEvent actionEvent) {
    }

    public void onClickOpenFile(ActionEvent actionEvent) {
    }


}


