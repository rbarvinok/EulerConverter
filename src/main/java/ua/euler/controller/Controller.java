package ua.euler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ua.euler.javaclass.*;
import ua.euler.javaclass.domain.Quaternion;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    Dovidka pb = new Dovidka();
    public Calculate calc = new Calculate();
    SaveToWord stw = new SaveToWord();
    OpenStage os = new OpenStage();
    Clear clear = new Clear();


    @FXML
    public TextArea textArea;

    @FXML
    public ImageView imgView;

    public void onClick(ActionEvent actionEvent) {
        try {

            //calc.f = Double.parseDouble(frequency.getText().replace(",", "."));
            //calc.wave = Double.parseDouble(wavelength.getText().replace(",", "."));
            //calc.epr = parseDouble(epr.getText().replace(",", "."));
            //crlength.setText(Double.toString(calc.roundLength()).replace(".", ","));

        } catch (NumberFormatException e) {
            pb.alert();
        }
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

    public void onClickOpenFile(ActionEvent actionEvent) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Euler. Відкриття файлу");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("*.csv", "*.csv"),
                new FileChooser.ExtensionFilter(".txt", "*.txt"),
                new FileChooser.ExtensionFilter("*.*", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            //desktop.open(selectedFile);

            FileReader fileReader = new FileReader(ExampleApp.class.getResource(fileChooser.getInitialFileName()).getFile());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<Quaternion> quaternionList = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.replaceAll(",", ".").replaceAll(";", ",");
                String[] split = line.split(",");

                Quaternion quaternion = new Quaternion(Double.parseDouble(split[0]),
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[2]),
                        Double.parseDouble(split[3]));
                quaternionList.add(quaternion);
            }

           textArea.setText(quaternionList.toString());

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


    public void OnClickNew(ActionEvent actionEvent) {
    }
}


