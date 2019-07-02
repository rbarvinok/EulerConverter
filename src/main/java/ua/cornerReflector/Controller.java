package ua.cornerReflector;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ua.cornerReflector.javaclass.OpenStage;
import ua.cornerReflector.javaclass.PushButton;
import ua.cornerReflector.javaclass.SaveToWord;
import ua.cornerReflector.javaclass.Width;

import java.io.IOException;

public class Controller {
    PushButton pb = new PushButton();
    public Width wd = new Width();
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
        pb.hd = "Розрахунок ширини променя випромінювання \nрадарної системи MFTR-2100/40 фірми Weibel";
        pb.ct = "Максимально допустимі значення згідно ТТХ:\n - дальність виявлення - 270 км;\n- кут по азимуту - 9,5 градусів;\n - кут підвищення - 8 градусів.";
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

}


