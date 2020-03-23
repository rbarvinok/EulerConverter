package ua.euler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ua.euler.javaclass.*;
import ua.euler.javaclass.Dovidka;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.lang.Double.parseDouble;

public class Controller {
    Dovidka pb = new Dovidka();
    public Calculate calc = new Calculate();
    SaveToWord stw = new SaveToWord();
    OpenStage os = new OpenStage();
    Clear clear = new Clear();


    @FXML
    public TextField wavelength, frequency, epr, crlength;
    @FXML
    public RadioButton freqBtn, waveBtn;
    @FXML
    public ImageView imgView;

    public void onClick(ActionEvent actionEvent) {
        try {

            //calc.f = Double.parseDouble(frequency.getText().replace(",", "."));
            //calc.wave = Double.parseDouble(wavelength.getText().replace(",", "."));
            calc.epr = parseDouble(epr.getText().replace(",", "."));
            crlength.setText(Double.toString(calc.roundLength()).replace(".", ","));

        } catch (NumberFormatException e) {
            pb.alert();
        }
    }

    public void onClickRadioButton() {
        //if (freqBtn.isSelected()) {
        freqBtn.setOnAction(event -> {
            wavelength.setEditable(false);
            frequency.setEditable(true);
            waveBtn.requestFocus();
            imgView.getClass().getResourceAsStream("/images/ang.png");
            calc.f = parseDouble(frequency.getText().replace(",", "."));
            wavelength.setText((Double.toString(calc.frequencyToWavelength()).replace(".", ",")));
        });

        //if ( waveBtn.isSelected()) {
        waveBtn.setOnAction(event -> {
            wavelength.setEditable(true);
            frequency.setEditable(false);
            waveBtn.requestFocus();
            imgView.getClass().getResourceAsStream("/images/ang.png");
            calc.wave = parseDouble(wavelength.getText().replace(",", "."));
            frequency.setText((Double.toString(calc.wavelengthToFrequency()).replace(".", ",")));

        });
    }


    public void nClickChart(ActionEvent actionEvent) throws IOException {
        os.viewURL = "/view/chart.fxml";
        os.title = "Графік";
        os.openStage();
    }

    public void onClick_menuAbaout(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/about.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void OnClickNew(ActionEvent actionEvent) {
        clear.clear(wavelength, frequency, epr, crlength);
    }

    public void onClickOpenFile(ActionEvent actionEvent) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Corner. Відкриття файлу");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("*.*", "*.*"),
                new FileChooser.ExtensionFilter(".txt", "*.txt"),
                new FileChooser.ExtensionFilter("*.doc", "*.doc"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            desktop.open(selectedFile);
        }
    }

    public void OnClickSave(ActionEvent actionEvent) throws IOException {
        stw.toWord();
    }

    public void onClickDovBtn(ActionEvent actionEvent) {
        pb.hd = "Розрахунок довжини ребра кутового відбивача";
        pb.ct = "ВХІДНІ ДАНІ:\n \n Характеристика радіолокаційного сигналу:\n   - частота,ГГц; \n   - довжина хвилі, м.\n(Вводится лише одне будь-яке значення)\n\nЕфективна площа розсіювання (ЕПР),м.кв\n Тип кутового відбивача";
        pb.dovButton();
    }

    public void onClickCnclBtn(ActionEvent actionEvent) {
        System.exit(0);
    }


}


