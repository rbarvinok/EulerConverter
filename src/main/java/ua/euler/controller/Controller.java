package ua.euler.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ua.euler.javaclass.GetSettings;
import ua.euler.javaclass.domain.EulerAngles;
import ua.euler.javaclass.domain.Quaternion;
import ua.euler.javaclass.servisClass.AlertAndInform;
import ua.euler.javaclass.servisClass.FileChooserRun;
import ua.euler.javaclass.servisClass.InputDate;
import ua.euler.javaclass.servisClass.OpenStage;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ua.euler.javaclass.QuaternionToEulerAnglesConvectorNonNormalised.calculateAltVelocity;
import static ua.euler.javaclass.QuaternionToEulerAnglesConvectorNonNormalised.quaternionToEulerAnglesBulk;
import static ua.euler.javaclass.servisClass.FileChooserRun.selectedOpenFile;

@Slf4j
public class Controller {
    AlertAndInform inform = new AlertAndInform();
    OpenStage os = new OpenStage();
    FileChooserRun fileChooserRun = new FileChooserRun();
    GetSettings getSettings = new GetSettings();

    public static double pressureNull = 101325.0;
    public static String openFile = " ";
    public static String openDirectory;
    public static String fileData;
    public String fileTime;
    public String hamModel;
    public String hamNumber;
    public String lineCount;
    public String timeStart, timeStop;
    public String headFile = "Час,    Курс,    Крен,    Тангаж,    Висота,    Вертикальна швидкість \n";
    public String headQuaternion = "Кватерніони (Час, Qw, Qx, Qy, Qz)";
    public String headEuler = "Кути Ейлера (Час,  Курс,  Крен,  Тангаж,   Висота)";
    public String headVelocity = "Час,  Атмосферний тиск,  Висота,  Вертикальна швидкість";
    public Double allTime;
    public int colsInpDate = 0;
    public static List<EulerAngles> eulerAngles = new ArrayList<>();
    public static List<Quaternion> quaternions = new ArrayList<>();
    public ObservableList<InputDate> inputDatesList = FXCollections.observableArrayList();

    @FXML
    public TableView outputTable;
    @FXML
    public ImageView imgView;
    @FXML
    public TextField statusBar, labelLineCount;
    @FXML
    public Label statusLabel, labelFileName, labelFileData, labelFileTime;
    @FXML
    public Label labelHamModel, labelHamNumber, labelAllTime, labelPress, LabelCapPress;
    @FXML
    public Button tSave, tCalc, tVel, tChart;
    @FXML
    public ProgressIndicator progressIndicator;


    public void openData() throws Exception {

        FileReader fileReader = new FileReader(selectedOpenFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        int lineNumber = 0;
        String line;
        while ((line = bufferedReader.readLine()) != null) {

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
            if (lineNumber == 9) {
                timeStart = line.split(",")[0];
            }

            line = line.replaceAll(";", ",");

            String[] split = line.split(",");


//            if (line.split(",")[14]==null||line.split(",")[14].equals(""))
//                line.split(",")[14] = "0";
//            if (lineNumber == 12) {
//                System.out.println(line.split(",")[14]);
//            }


            if (split.length <= 14 || lineNumber < 9) {
                lineNumber++;
                continue;
            }
            lineNumber++;

            if (lineNumber == lineNumber) {
                timeStop = String.valueOf(line.split(",")[0]);
            }

            Quaternion quaternion = new Quaternion(
                    split[0],
                    Double.parseDouble(split[7]),
                    Double.parseDouble(split[8]),
                    Double.parseDouble(split[9]),
                    Double.parseDouble(split[10]),
                    Double.parseDouble(split[14])
            );

            quaternions.add(quaternion);
        }

        eulerAngles = quaternionToEulerAnglesBulk(quaternions);
        eulerAngles = calculateAltVelocity(eulerAngles);
        fileReader.close();

//        List<String> quaternionStrings = quaternions.stream().map(Quaternion::toString).collect(Collectors.toList());
//        String textForTextArea = String.join("", quaternionStrings);
//        outputText.setText(textForTextArea);

        //output to Table----------------------------------------
        inputDates(quaternions);
        TableColumn<InputDate, String> tTime = new TableColumn<>("Час");
        TableColumn<InputDate, String> tQw = new TableColumn<>("Qw");
        TableColumn<InputDate, String> tQx = new TableColumn<>("Qx");
        TableColumn<InputDate, String> tQy = new TableColumn<>("Qy");
        TableColumn<InputDate, String> tQz = new TableColumn<>("Qz");

        for (int i = 0; i <= colsInpDate - 5; i++) {
            final int indexColumn = i;
            tTime.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(0 + indexColumn)));
            tQw.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(1 + indexColumn)));
            tQx.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(2 + indexColumn)));
            tQy.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(3 + indexColumn)));
            tQz.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(4 + indexColumn)));
        }
        outputTable.getColumns().addAll(tTime, tQw, tQx, tQy, tQz);
        outputTable.setItems(inputDatesList);

        System.out.println(quaternions);
        //--------------------------------------------------------

        lineCount = String.valueOf(lineNumber);
        labelLineCount.setText("Cтрок:  " + lineCount);

        allTime = Double.parseDouble(timeStop) - Double.parseDouble(timeStart);

        getSettings.getPressureNull();

        statusLabel.setText(headQuaternion);
        statusBar.setText(headQuaternion);
        labelHamModel.setText(hamModel);
        labelHamNumber.setText(hamNumber);
        labelFileName.setText(" Файл \n" + openFile);
        labelFileData.setText(" Дата \n" + fileData);
        labelFileTime.setText(" Час  \n" + fileTime);
        labelAllTime.setText(" Час \n вимірювання \n" + allTime + " сек");
        LabelCapPress.setText("Тиск на рівні землі");
        labelPress.setText(pressureNull + "  Pa");

        tSave.setDisable(false);
        tCalc.setDisable(false);
        tVel.setDisable(false);
        tChart.setDisable(false);
    }

    public void onClickOpenFile(ActionEvent actionEvent) throws Exception {
        if (statusBar.getText().equals("")) {
            progressIndicatorRun();

            fileChooserRun.openFileChooser();
            openFile = selectedOpenFile.getName().substring(0, selectedOpenFile.getName().length() - 4);
            openDirectory = selectedOpenFile.getParent();

            openData();
            progressIndicator.setVisible(false);
            return;
        } else
            inform.hd = "Файл уже відкритий";
        inform.ct = " Повторне відкриття файлу призведе до втрати не збережених даних \n";
        inform.inform();
        return;
    }

    public void onClickCalculate(ActionEvent actionEvent) {
        if (statusLabel.getText().equals("")) {
            statusBar.setText("Помилка! Відсутні дані для рохрахунку");
            inform.hd = "Помилка! Відсутні дані для рохрахунку";
            inform.ct = " 1. Відкрити файл  даних 'НАМ'\n 2. Натиснути кнопку Розрахувати \n 3. Зберегти розраховані дані в вихідний файл\n";
            inform.alert();
            statusBar.setText("");
            return;
        } else
            try {
                List<String> eulerAnglesStrings = eulerAngles.stream().map(EulerAngles::toStringEuler).collect(Collectors.toList());
//                String textForTextArea = String.join("", eulerAnglesStrings);
//                outputText.setText(textForTextArea);

                //output to Table----------------------------------------
                inputDates(eulerAnglesStrings);
                TableColumn<InputDate, String> tTime = new TableColumn<>("Час");
                TableColumn<InputDate, String> tRoll = new TableColumn<>("Курс");
                TableColumn<InputDate, String> tPitch = new TableColumn<>("Крен");
                TableColumn<InputDate, String> tYaw = new TableColumn<>("Тангаж");
                TableColumn<InputDate, String> tAlt = new TableColumn<>("Висота");

                for (int i = 0; i <= colsInpDate - 5; i++) {
                    final int indexColumn = i;
                    tTime.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(0 + indexColumn)));
                    tRoll.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(1 + indexColumn)));
                    tPitch.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(2 + indexColumn)));
                    tYaw.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(3 + indexColumn)));
                    tAlt.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(4 + indexColumn)));
                }
                outputTable.getColumns().addAll(tTime, tRoll, tPitch, tYaw, tAlt);
                outputTable.setItems(inputDatesList);
                //--------------------------------------------------------

            } catch (NumberFormatException e) {
                inform.alert();
            }
        statusBar.setText(headEuler);
        statusLabel.setText("Кути Ейлера");
    }

    public void onClickVelocity(ActionEvent actionEvent) {
        if (statusLabel.getText().equals("")) {
            statusBar.setText("Помилка! Відсутні дані для рохрахунку");
            inform.hd = "Помилка! Відсутні дані для рохрахунку";
            inform.ct = " 1. Відкрити файл  даних 'НАМ'\n 2. Натиснути кнопку Розрахувати \n 3. Зберегти розраховані дані в вихідний файл\n";
            inform.alert();
            statusBar.setText("");
            return;
        } else {
            List<String> rateOfDeclinesStrings = eulerAngles.stream().map(EulerAngles::toStringVelocity).collect(Collectors.toList());
            //String textForTextArea = String.join("", rateOfDeclinesStrings);
            //outputText.setText(textForTextArea);

            //output to Table----------------------------------------
            inputDates(rateOfDeclinesStrings);
            TableColumn<InputDate, String> tTime = new TableColumn<>("Час");
            TableColumn<InputDate, String> tPress = new TableColumn<>("Атмосферний тиск");
            TableColumn<InputDate, String> tAlt = new TableColumn<>("Висота");
            TableColumn<InputDate, String> tVertVel = new TableColumn<>("Вертикальна швидкість");


            for (int i = 0; i <= colsInpDate - 4; i++) {
                final int indexColumn = i;
                tTime.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(0 + indexColumn)));
                tPress.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(1 + indexColumn)));
                tAlt.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(2 + indexColumn)));
                tVertVel.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(3 + indexColumn)));

            }
            outputTable.getColumns().addAll(tTime, tPress, tAlt, tVertVel);
            outputTable.setItems(inputDatesList);
            //--------------------------------------------------------
            statusBar.setText(headVelocity);
            statusLabel.setText("Вертикальна швидкість");
        }
    }

    public void onClickOpenFileInDesktop(ActionEvent actionEvent) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        fileChooserRun.openFileChooser();
        desktop.open(selectedOpenFile);
    }

    @SneakyThrows
    public void onClickSave(ActionEvent actionEvent) {
        //progressIndicatorRun();
        if (CollectionUtils.isEmpty(eulerAngles)) {
            log.warn("eulerAnges is empty");
            statusBar.setText("Помилка! Відсутні дані для збереження");
            inform.hd = "Помилка! Відсутні дані для збереження";
            inform.ct = " 1. Відкрити підготовлений файл вихідних даних\n 2. Натиснути кнопку Розрахувати \n 3. Зберегти розраховані дані в вихідний файл\n";
            inform.alert();
            progressIndicator.setVisible(false);
            statusBar.setText("");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Зберегти як...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("*.xlsx", "*.xlsx"),
                new FileChooser.ExtensionFilter("*.csv", "*.csv"),
                new FileChooser.ExtensionFilter("*.*", "*.*"));
        fileChooser.setInitialFileName(openFile + "_euler");
        File userDirectory = new File(openDirectory);
        fileChooser.setInitialDirectory(userDirectory);

        File file = fileChooser.showSaveDialog((new Stage()));
//---------------------------------------------------
        if (fileChooser.getSelectedExtensionFilter().getDescription().equals("*.xlsx")) {
            Workbook book = new XSSFWorkbook();
            Sheet sheet = book.createSheet(openFile + "_euler");

            DataFormat format = book.createDataFormat();
            CellStyle dateStyle = book.createCellStyle();
            dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));

            int rownum = 0;
            Cell cell;
            Row row = sheet.createRow(rownum);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Модель реєстратора");
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(hamModel);
            rownum++;
            row = sheet.createRow(rownum);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Серійний номер");
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(hamNumber);
            rownum++;
            row = sheet.createRow(rownum);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Дата");
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(fileData);
            rownum++;
            row = sheet.createRow(rownum);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Час");
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(fileTime);
            rownum++;
            row = sheet.createRow(rownum);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Час вимірювання");
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(allTime);
            rownum++;
            row = sheet.createRow(rownum);
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("Атмосферний тиск на рівні землі");
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(pressureNull + "  Па");
            rownum++;
            rownum++;
            row = sheet.createRow(rownum);

            int columnCount = StringUtils.countMatches(headFile, ",");
            for (int colnum = 0; colnum <= columnCount; colnum++) {
                cell = row.createCell(colnum, CellType.STRING);
                cell.setCellValue(headFile.split(",")[colnum]);
                sheet.autoSizeColumn(colnum);
            }
            rownum++;

            for (EulerAngles eulerAngles : eulerAngles) {
                row = sheet.createRow(rownum);
                for (int colnum = 0; colnum <= columnCount; colnum++) {
                    cell = row.createCell(colnum, CellType.STRING);
                    cell.setCellValue(eulerAngles.toString().split(",")[colnum]);
                    //sheet.autoSizeColumn(colnum);
                }
                rownum++;
            }
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            FileOutputStream outFile = new FileOutputStream(file);
            book.write(outFile);
            outFile.close();
            statusBar.setText("Успішно записано в файл " + openFile + "_euler.xlsx");
            inform.title = "Збереження файлу";
            inform.hd = null;
            inform.ct = "Успішно записано в файл '" + openFile + "_euler.xlsx'";
            inform.inform();
        }
//------------------------
        if (fileChooser.getSelectedExtensionFilter().getDescription().equals("*.csv")) {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file, true), "Cp1251");
            osw.write("Модель реєстратора  -  " + hamModel + "\n");
            osw.write("Серійний номер  -  " + hamNumber + "\n");
            osw.write("Дата  -  " + fileData + "\n");
            osw.write("Час  -  " + fileTime + "\n");
            osw.write("Час вимірювання  -  " + allTime + "\n");
            osw.write("Атмосферний тиск на рівні землі  -  " + pressureNull + "  Па \n\n");
            osw.write(headFile);
            for (EulerAngles eulerAngle : eulerAngles) {
                //log.info(eulerAngle.toString());
                osw.write(eulerAngle.toString());
            }
            osw.close();
            statusBar.setText("Успішно записано в файл " + openFile + "_euler.csv");
            inform.title = "Збереження файлу";
            inform.hd = null;
            inform.ct = "Успішно записано в файл '" + openFile + "_euler.csv'";
            inform.inform();
        }

        progressIndicator.setVisible(false);
    }

    public void onClickChart(ActionEvent actionEvent) throws IOException {
        progressIndicator.setVisible(true);
        if (statusLabel.getText().equals("Кути Ейлера")) {
            os.viewURL = "/view/chartEuler.fxml";
            os.title = "Кути Ейлера   " + openFile;
            os.openStage();
            progressIndicator.setVisible(false);
        } else {
            if (statusLabel.getText().equals(headQuaternion)) {
                os.viewURL = "/view/chartQuaternion.fxml";
                os.title = "Кватерніони   " + openFile;
                os.openStage();
                progressIndicator.setVisible(false);

            } else {
                if (statusLabel.getText().equals("Вертикальна швидкість")) {
                    os.viewURL = "/view/chartVelocity.fxml";
                    os.title = "Вертикальна швидкість   " + openFile;
                    os.openStage();
                    progressIndicator.setVisible(false);

                } else {
                    statusBar.setText("Помилка! Відсутні дані для рохрахунку");
                    inform.hd = "Помилка! Відсутні дані для відображення";
                    inform.ct = " Необхідно відкрити підготовлений файл вхідних даних\n ";
                    inform.alert();
                    statusBar.setText("");
                    progressIndicator.setVisible(false);
                    return;
                }
            }
        }
    }

    public void inputDates(List source) {
        outputTable.getColumns().clear();
        outputTable.getItems().clear();
        outputTable.setEditable(false);
        int rowsInpDate = 0;
        String line;
        String csvSplitBy = ",";
        for (int j = 0; j < source.size(); j++) {
            line = source.get(j).toString();
            line = line.replace("[", "").replace("]", "");
            rowsInpDate += 1;
            String[] fields = line.split(csvSplitBy, -1);
            colsInpDate = fields.length;
            InputDate inpd = new InputDate(fields);
            inputDatesList.add(inpd);
        }
    }

    @SneakyThrows
    public void onClickDovBtn(ActionEvent actionEvent) {
        if (Desktop.isDesktopSupported()) {
            File url = new File("userManual/UserManual_Euler.pdf");
            Desktop desktop = Desktop.getDesktop();
            System.out.println(url.getPath());
            desktop.open(url);
        }
    }

    @SneakyThrows
    public void onClickMenuHAM(ActionEvent actionEvent) {
        if (Desktop.isDesktopSupported()) {
            File url = new File("userManual/UserManual_HAM.pdf");
            Desktop desktop = Desktop.getDesktop();
            System.out.println(url.getPath());
            desktop.open(url);
        }
    }

    public void onClick_menuAbout(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/about.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onClickNew(ActionEvent e) {
        outputTable.getColumns().clear();
        outputTable.getItems().clear();
        statusBar.setText("");
        statusLabel.setText("Конвертування кватерніонів в кути Ейлера");
        labelHamModel.setText(" ");
        labelHamNumber.setText(" ");
        labelFileName.setText(" ");
        labelFileData.setText(" ");
        labelFileTime.setText(" ");
        labelLineCount.setText(" ");
        labelAllTime.setText(" ");
        labelPress.setText(" ");
        LabelCapPress.setText(" ");
        quaternions.clear();
        eulerAngles.clear();
        tSave.setDisable(true);
        tCalc.setDisable(true);
        tVel.setDisable(true);
        tChart.setDisable(true);
        progressIndicator.setVisible(false);
    }

    public void onClickPressureSettings(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/pressureSettings.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        stage.setTitle("Наземний тиск   " + openFile);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/HAM.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void progressIndicatorRun() {
        Platform.runLater(() -> {
//            @SneakyThrows
//            @Override
//            public void run() {
            progressIndicator.setVisible(true);
            statusBar.setText("Зачекайте...");
//            }
        });
    }

    public void onClickCancelBtn(ActionEvent e) {
        System.exit(0);
    }

}


