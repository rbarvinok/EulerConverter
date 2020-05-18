package ua.euler.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import ua.euler.javaclass.QuaternionToEulerAnglesConvectorNonNormalised;
import ua.euler.javaclass.domain.EulerAngles;
import ua.euler.javaclass.domain.Quaternion;
import ua.euler.javaclass.servisClass.Dovidka;
import ua.euler.javaclass.servisClass.FileChooserRun;
import ua.euler.javaclass.servisClass.OpenStage;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ua.euler.javaclass.servisClass.FileChooserRun.selectedOpenFile;

//        labelWiki.setText("The following wikipedia entries provide detailed discussion of quaternions:\n"+
//         "http://en.wikipedia.org/wiki/Quaternion,   "+"http://en.wikipedia.org/wiki/Quaternions_and_spatial_rotation\n"+
//         "http://en.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles");

@Slf4j
public class Controller {
    Dovidka pb = new Dovidka();
    OpenStage os = new OpenStage();
    FileChooserRun fileChooserRun = new FileChooserRun();

    public static String openFile;
    public static String openDirectory;
    public static String fileData;
    public static String fileTime;
    public static String hamModel;
    public static String hamNumber;
    public String lineCount;
    public static String headFile = "Час UTC, Курс, Крен, Тангаж \n";

    public static List<EulerAngles> eulerAngles = new ArrayList<>();
    public static List<Quaternion> quaternions = new ArrayList<>();

    @FXML
    public TextArea outputText;
    @FXML
    public ImageView imgView;
    @FXML
    public TextField statusBar, labelLineCount;
    @FXML
    public Label statusLabel, labelFileName, labelFileData, labelFileTime, labelHamModel, labelHamNumber;
    @FXML
    public ProgressIndicator progressIndicator;

    public void OpenData() throws Exception {

        ProgressIndicatorRun();

        fileChooserRun.openFileChooser();
        openFile = selectedOpenFile.getName();
        openDirectory = selectedOpenFile.getParent();

        FileReader fileReader = new FileReader(selectedOpenFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        new Thread(() -> {
            int lineNumber = 0;
            String line = null;
            while (true) {
                try {
                    if ((line = bufferedReader.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (lineNumber == 0) {
                    hamModel = line.split(",")[2] + line.split(",")[3];
                }
                if (lineNumber == 1) {
                    hamNumber = line.split(",")[4];
                }
                if (lineNumber == 2) {
                    line = line.replaceAll(";", ",");
                    fileData = line.split(",")[2];
                    fileTime = line.split(",")[3];
                }

                line = line.replaceAll(";", ",");

                String[] split = line.split(",");
                if (split.length <= 2 || lineNumber < 9) {
                    lineNumber++;
                    continue;
                }
                lineNumber++;

                Quaternion quaternion = new Quaternion(
                        QuaternionToEulerAnglesConvectorNonNormalised.timeFormatter(split[0]),
                        Double.parseDouble(split[7]),
                        Double.parseDouble(split[8]),
                        Double.parseDouble(split[9]),
                        Double.parseDouble(split[10]));
                quaternions.add(quaternion);
            }
            eulerAngles = QuaternionToEulerAnglesConvectorNonNormalised.quaternionToEulerAnglesBulk(quaternions);

            List<String> quaternionStrings = quaternions.stream().map(Quaternion::toString).collect(Collectors.toList());
            String textForTextArea = String.join("", quaternionStrings);
            outputText.setText(textForTextArea);

            lineCount = String.valueOf(lineNumber);
            labelLineCount.setText("Cтрок:  " + lineCount);

            progressIndicator.setVisible(false);
            statusBar.setText("Кватерніони (Час UTC, Qw, Qx, Qy, Qz)");
            statusLabel.setText("Кватерніони (Час UTC, Qw, Qx, Qy, Qz)");
            labelHamModel.setText(hamModel);
            labelHamNumber.setText(hamNumber);
            labelFileName.setText("Файл \n" + openFile);
            labelFileData.setText(" Дата \n" + fileData);
            labelFileTime.setText(" Час  \n" + fileTime);
        }).start();
    }

    public void onClickOpenFile(ActionEvent actionEvent) throws Exception {
        if (outputText.getText().equals("")) {
            OpenData();
            return;
        } else
            pb.hd = "Файл уже відкритий";
        pb.ct = " Повторне відкриття файлу призведе до втрати не збережених даних \n";
        pb.inform();
        return;
    }

    public void onClickCalculate(ActionEvent actionEvent) {
        if (outputText.getText().equals("")) {
            statusBar.setText("Помилка! Відсутні дані для рохрахунку");
            pb.hd = "Помилка! Відсутні дані для рохрахунку";
            pb.ct = " 1. Відкрити файл  даних 'НАМ'\n 2. Натиснути кнопку Розрахувати \n 3. Зберегти розраховані дані в вихідний файл\n";
            pb.inform();
            return;
        } else
            try {
                List<String> eulerAnglesStrings = eulerAngles.stream().map(EulerAngles::toString).collect(Collectors.toList());
                String textForTextArea = String.join("", eulerAnglesStrings);
                outputText.setText(textForTextArea);
            } catch (NumberFormatException e) {
                pb.alert();
            }
        statusBar.setText("Кути Ейлера (Час UTC, Курс, Крен, Тангаж)");
        statusLabel.setText("Кути Ейлера (Час UTC, Курс, Крен, Тангаж)");
    }

    public void onClickOpenFileInDesktop(ActionEvent actionEvent) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        fileChooserRun.openFileChooser();
        desktop.open(selectedOpenFile);
    }

    @SneakyThrows
    public void onClickSave(ActionEvent actionEvent) throws IOException {
        ProgressIndicatorRun();
        if (CollectionUtils.isEmpty(eulerAngles)) {
            log.warn("eulerAnges is empty");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Зберегти як...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("*.csv", "*.csv"),
                new FileChooser.ExtensionFilter(".txt", "*.txt"),
                new FileChooser.ExtensionFilter("*.*", "*.*"));
        fileChooser.setInitialFileName("EulerAngles_" + openFile);
        File userDirectory = new File(openDirectory);
        fileChooser.setInitialDirectory(userDirectory);

        File file = fileChooser.showSaveDialog((new Stage()));

        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write("Модель реєстратора,  " + hamModel + "\n");
        fileWriter.write("Серійний номер ,  " + hamNumber + "\n");
        fileWriter.write("Дата,  " + fileData + "\n");
        fileWriter.write("Час  ,  " + fileTime + "\n");
        fileWriter.write(headFile);

        for (EulerAngles eulerAngle : eulerAngles) {
            log.info(eulerAngle.toString());
            fileWriter.write(eulerAngle.toString());
        }
        fileWriter.close();
        statusBar.setText("Успішно записано в файл 'EulerAngles_" + openFile + "'");
        progressIndicator.setVisible(false);
    }

    public void onClickChart(ActionEvent actionEvent) throws IOException {
        progressIndicator.setVisible(true);
        if (statusLabel.getText().equals("Кути Ейлера (Час UTC, Курс, Крен, Тангаж)")) {
            os.viewURL = "/view/chartEuler.fxml";
            os.title = "Кути Ейлера   " + openFile;
            os.openStage();
            progressIndicator.setVisible(false);
        } else {
            if (statusLabel.getText().equals("Кватерніони (Час UTC, Qw, Qx, Qy, Qz)")) {
                os.viewURL = "/view/chartQuaternion.fxml";
                os.title = "Кватерніони   " + openFile;
                os.openStage();
                progressIndicator.setVisible(false);
            } else {
                statusBar.setText("Помилка! Відсутні дані для рохрахунку");
                pb.hd = "Помилка! Відсутні дані для рохрахунку";
                pb.ct = " 1. Відкрити підготовлений файл вихідних даних\n 2. Натиснути кнопку Розрахувати \n 3. Зберегти розраховані дані в вихідний файл\n";
                pb.alert();
            }
        }
    }

    public void onClickDovBtn(ActionEvent actionEvent) {
        pb.hd = "Конвертор кватерніонів в кути Ейлера";
        pb.ct = " 1. Відкрити файл вихідних даних\n 2. Натиснути кнопку Розрахувати \n 3. Зберегти розраховані дані в вихідний файл\n";
        pb.inform();
    }

    public void onClick_menuAbout(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/about.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void OnClickNew(ActionEvent e) {
        outputText.setText("");
        statusBar.setText("");
        statusLabel.setText("Конвертування кватерніонів в кути Ейлера");
        labelHamModel.setText(" ");
        labelHamNumber.setText(" ");
        labelFileName.setText(" ");
        labelFileData.setText(" ");
        labelFileTime.setText(" ");
        labelLineCount.setText(" ");
        quaternions.clear();
        progressIndicator.setVisible(false);
    }

    public void onClickCancelBtn(ActionEvent e) {
        System.exit(0);
    }

    public void ProgressIndicatorRun() throws Exception {
        new Thread(() -> {
            progressIndicator.setVisible(true);
            statusBar.setText("Зачекайте...");
        }).start();
    }
}


