package ua.euler.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ua.euler.javaclass.domain.Acceleration;
import ua.euler.javaclass.domain.EulerAngles;
import ua.euler.javaclass.domain.Quaternion;
import ua.euler.javaclass.domain.Result;
import ua.euler.javaclass.servisClass.*;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;
import static ua.euler.controller.CutController.endCut;
import static ua.euler.controller.CutController.startCut;
import static ua.euler.javaclass.AccelerationCalculate.calculateAccelerateBulk;
import static ua.euler.javaclass.CalculateResult.calculateAltVel;
import static ua.euler.javaclass.CalculateResult.resultsBulk;
import static ua.euler.javaclass.QuaternionToEulerAnglesConvectorNonNormalised.calculateAltVelocity;
import static ua.euler.javaclass.QuaternionToEulerAnglesConvectorNonNormalised.quaternionToEulerAnglesBulk;
import static ua.euler.javaclass.servisClass.FileChooserRun.selectedOpenFile;

@Slf4j
public class Controller implements Initializable {
    AlertAndInform inform = new AlertAndInform();
    OpenStage os = new OpenStage();
    FileChooserRun fileChooserRun = new FileChooserRun();
    GetSettings getSettings = new GetSettings();

    public static double pressureNull = 101325.0;
    public static String openFile = " ";
    public static String openDirectory;
    public static String fileData;
    public Date data;
    public String fileTime;
    public String hamModel;
    public String hamNumber;
    public int lineCount;
    public String time;
    public String headFile = "Час, nx, ny, nz, n abs, Курс, Крен, Тангаж, Висота, Вертикальна швидкість, Тиск, Температура \n";
    public static double allTime, timeStart, timeStop;
    public int colsInpDate = 0;
    public static List<EulerAngles> eulerAngles = new ArrayList<>();
    public static List<Quaternion> quaternions = new ArrayList<>();
    public static List<Acceleration> accelerations = new ArrayList<>();
    public static List<Result> results = new ArrayList<>();
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
    public Label labelHamModel, labelHamNumber, labelAllTime, labelPress;
    @FXML
    public Button tSave, tCalc, tVel, tChart, tAcc, tCut;

    @FXML
    //public ProgressIndicator progressIndicator;

    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        getSettings.getSettings();

    }

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
                fileData = line.split(",")[2].trim();
                fileTime = line.split(",")[3];
            }
            if (lineNumber == 9) {
                timeStart = Double.parseDouble(line.split(",")[0]);
                //startCut = timeStart;
            }

            line = line.replaceAll(";", ",");
            String[] split = line.split(",");

            if (split.length <= 15 || lineNumber < 9) {
                lineNumber++;
                continue;
            }
            time = valueOf(line.split(",")[0]);
            timeStop = Double.parseDouble(time);
            lineNumber++;

            if (startCut <= 0 || endCut <= 0) {
                lineCount++;
                Quaternion quaternion = new Quaternion(
                        split[0],
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[2]),
                        Double.parseDouble(split[3]),
                        Double.parseDouble(split[7]),
                        Double.parseDouble(split[8]),
                        Double.parseDouble(split[9]),
                        Double.parseDouble(split[10]),
                        Double.parseDouble(split[14]),
                        Double.parseDouble(split[15])
                );
                quaternions.add(quaternion);
            } else if (Double.parseDouble(time) >= startCut && Double.parseDouble(time) <= endCut) {
                lineCount++;
                Quaternion quaternion = new Quaternion(
                        split[0],
                        Double.parseDouble(split[1]),
                        Double.parseDouble(split[2]),
                        Double.parseDouble(split[3]),
                        Double.parseDouble(split[7]),
                        Double.parseDouble(split[8]),
                        Double.parseDouble(split[9]),
                        Double.parseDouble(split[10]),
                        Double.parseDouble(split[14]),
                        Double.parseDouble(split[15])
                );
                quaternions.add(quaternion);
            }
        }
        fileReader.close();
        startCut = timeStart;
        endCut = timeStop;

        inputDates(quaternions);
        TableColumn<InputDate, String> tTime = new TableColumn<>("Час");
        TableColumn<InputDate, String> tAx = new TableColumn<>("Ax");
        TableColumn<InputDate, String> tAy = new TableColumn<>("Ay");
        TableColumn<InputDate, String> tAz = new TableColumn<>("Az");
        TableColumn<InputDate, String> tQw = new TableColumn<>("Qw");
        TableColumn<InputDate, String> tQx = new TableColumn<>("Qx");
        TableColumn<InputDate, String> tQy = new TableColumn<>("Qy");
        TableColumn<InputDate, String> tQz = new TableColumn<>("Qz");
        TableColumn<InputDate, String> tPr = new TableColumn<>("Тиск");
        TableColumn<InputDate, String> tT = new TableColumn<>("Температура");

        for (int i = 0; i <= colsInpDate - 10; i++) {
            final int indexColumn = i;
            tTime.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(0 + indexColumn)));
            tAx.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(1 + indexColumn)));
            tAy.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(2 + indexColumn)));
            tAz.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(3 + indexColumn)));
            tQw.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(4 + indexColumn)));
            tQx.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(5 + indexColumn)));
            tQy.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(6 + indexColumn)));
            tQz.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(7 + indexColumn)));
            tPr.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(8 + indexColumn)));
            tT.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(9 + indexColumn)));
        }
        outputTable.getColumns().addAll(tTime, tAx, tAy, tAz, tQw, tQx, tQy, tQz, tPr, tT);
        outputTable.setItems(inputDatesList);
        //--------------------------------------------------------
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        data = sdf.parse(fileData);
        sdf.applyPattern("dd.MM.yyyy");

        ///lineCount = valueOf(lineNumber);
        labelLineCount.setText("Cтрок:  " + lineCount);
        allTime = endCut - startCut;
        statusLabel.setText("Вхідні дані");
        statusBar.setText("Файл \n" + openFile);
        labelHamModel.setText(hamModel);
        labelHamNumber.setText(hamNumber);
        labelFileName.setText(" Файл \n" + openFile);
        labelFileData.setText(" Дата \n" + sdf.format(data));
        labelFileTime.setText(" Час  \n" + fileTime);
        labelAllTime.setText(" Час \n вимірювання \n" + allTime + " сек");
        labelPress.setText("Тиск на рівні землі \n" + pressureNull + "  Pa");

        tSave.setDisable(false);
        tCalc.setDisable(false);
        tAcc.setDisable(false);
        tVel.setDisable(false);
        tChart.setDisable(false);
    }

    @SneakyThrows
    public void onClickUpdate() {
        quaternions.clear();
        outputTable.getColumns().clear();
        outputTable.getItems().clear();
        lineCount = 0;
        openData();
    }

    public void onClickOpenFile() throws Exception {
        if (statusBar.getText().equals("")) {
            fileChooserRun.openFileChooser();
            openFile = selectedOpenFile.getName().substring(0, selectedOpenFile.getName().length() - 4);
            openDirectory = selectedOpenFile.getParent();

            openData();
        } else {
            inform.hd = "Файл уже відкритий";
            inform.ct = " Повторне відкриття файлу призведе до втрати не збережених даних \n";
            inform.inform();
            return;
        }
    }

    @SneakyThrows
    public void onClickEulerAngles() {
        updateLabel();
        eulerAngles = quaternionToEulerAnglesBulk(quaternions);

        List<String> eulerAnglesStrings = eulerAngles.stream().map(EulerAngles::toStringEuler).collect(Collectors.toList());

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
        //statusBar.setText(headEuler);
        statusLabel.setText("Кути Ейлера");
    }

    @SneakyThrows
    public void onClickAcceleration() {
        updateLabel();
        accelerations = calculateAccelerateBulk(quaternions);
        List<String> rateOfDeclinesStrings = accelerations.stream().map(Acceleration::toString).collect(Collectors.toList());
        //output to Table----------------------------------------
        inputDates(rateOfDeclinesStrings);
        TableColumn<InputDate, String> tTime = new TableColumn<>("Час");
        TableColumn<InputDate, String> tAx = new TableColumn<>("nx");
        TableColumn<InputDate, String> tAy = new TableColumn<>("ny");
        TableColumn<InputDate, String> tAz = new TableColumn<>("nz");
        TableColumn<InputDate, String> tA = new TableColumn<>("n abs");

        for (int i = 0; i <= colsInpDate - 5; i++) {
            final int indexColumn = i;
            tTime.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(0 + indexColumn)));
            tAx.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(1 + indexColumn)));
            tAy.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(2 + indexColumn)));
            tAz.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(3 + indexColumn)));
            tA.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(4 + indexColumn)));
        }
        outputTable.getColumns().addAll(tTime, tAx, tAy, tAz, tA);
        outputTable.setItems(inputDatesList);
        //--------------------------------------------------------
        statusLabel.setText("Перевантаження, g");
        statusBar.setText("Файл \n" + openFile);
    }

    @SneakyThrows
    public void onClickVelocity() {
        updateLabel();
        eulerAngles = quaternionToEulerAnglesBulk(quaternions);
        eulerAngles = calculateAltVelocity(eulerAngles);

        List<String> rateOfDeclinesStrings = eulerAngles.stream().map(EulerAngles::toStringVelocity).collect(Collectors.toList());
        inputDates(rateOfDeclinesStrings);
        TableColumn<InputDate, String> tTime = new TableColumn<>("Час");
        TableColumn<InputDate, String> tPress = new TableColumn<>("Атмосферний тиск");
        TableColumn<InputDate, String> tAlt = new TableColumn<>("Висота");
        TableColumn<InputDate, String> tVertVel = new TableColumn<>("Вертикальна швидкість");
        TableColumn<InputDate, String> tT = new TableColumn<>("Температура");

        for (int i = 0; i <= colsInpDate - 5; i++) {
            final int indexColumn = i;
            tTime.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(0 + indexColumn)));
            tPress.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(1 + indexColumn)));
            tAlt.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(2 + indexColumn)));
            tVertVel.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(3 + indexColumn)));
            tT.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getItems().get(4 + indexColumn)));
        }
        outputTable.getColumns().addAll(tTime, tPress, tAlt, tVertVel, tT);
        outputTable.setItems(inputDatesList);
        //--------------------------------------------------------
        //statusBar.setText(headVelocity);
        statusLabel.setText("Вертикальна швидкість");
    }

    public void onClickOpenFileInDesktop() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        fileChooserRun.openFileChooser();
        desktop.open(selectedOpenFile);
    }

    @SneakyThrows
    public void onClickSave() {
        results = resultsBulk(quaternions);
        results = calculateAltVel(results);
//        accelerations.forEach(acc -> {
//            Optional<EulerAngles> angelOpt = eulerAngles.stream().filter(ea -> ea.getTime().equals(acc.getTime())).findFirst();
//
//            angelOpt.ifPresent(ea -> {
//                Result result = new Result(acc.getTime(),acc.getAx(),acc.getAy(),acc.getAabs(), ea.getRoll(),ea.getPitch(),ea.getYaw(),ea.getAltitude(),ea.getVelocity());
//                results.add(result);
//            });
//        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
        //progressIndicatorRun();
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
            cell.setCellValue(sdf.format(data));
            //cell.setCellValue(fileData);
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
                //sheet.autoSizeColumn(colnum);
            }
            rownum++;

            for (Result result : results) {
                row = sheet.createRow(rownum);
                for (int colnum = 0; colnum <= columnCount; colnum++) {
                    cell = row.createCell(colnum, CellType.STRING);
                    cell.setCellValue(result.toString().split(",")[colnum]);
                    // sheet.autoSizeColumn(colnum);
                }
                rownum++;
            }

//            for (int i = 0; i <= 10; i++) {
//                sheet.autoSizeColumn(i);
//            }
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
            for (Result result : results) {
                //log.info(eulerAngle.toString());
                osw.write(result.toString());
            }
            osw.close();
            statusBar.setText("Успішно записано в файл " + openFile + "_euler.csv");
            inform.title = "Збереження файлу";
            inform.hd = null;
            inform.ct = "Успішно записано в файл '" + openFile + "_euler.csv'";
            inform.inform();
        }


    }

    public void onClickChart() throws IOException {
        switch (statusLabel.getText()) {
            case "Кути Ейлера": {
                os.viewURL = "/view/chartEuler.fxml";
                os.title = "Кути Ейлера   " + openFile;
                os.openStage();
                break;
            }
            case "Вхідні дані": {
                os.viewURL = "/view/chartQuaternion.fxml";
                os.title = "Кватерніони   " + openFile;
                os.openStage();
                break;
            }
            case "Вертикальна швидкість": {
                os.viewURL = "/view/chartVelocity.fxml";
                os.title = "Вертикальна швидкість   " + openFile;
                os.openStage();
                break;
            }
            case "Перевантаження, g": {
                os.viewURL = "/view/chartAcceleration.fxml";
                os.title = "Прискорення   " + openFile;
                os.openStage();
                break;
            }
            case "": {
                statusBar.setText("Помилка! Відсутні дані для рохрахунку");
                inform.hd = null;
                inform.ct = " Необхідно відкрити підготовлений файл вхідних даних\n ";
                inform.alert();
                break;
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
    public void onClickManual() {
        if (Desktop.isDesktopSupported()) {
            File url = new File("userManual/UserManual_Euler.pdf");
            Desktop desktop = Desktop.getDesktop();
            //System.out.println(url.getPath());
            desktop.open(url);
        }
    }

    @SneakyThrows
    public void onClickMenuHAM() {
        if (Desktop.isDesktopSupported()) {
            File url = new File("userManual/UserManual_HAM.pdf");
            Desktop desktop = Desktop.getDesktop();
            System.out.println(url.getPath());
            desktop.open(url);
        }
    }

    public void onClick_menuAbout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/about.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onClickNew() {
        outputTable.getColumns().clear();
        outputTable.getItems().clear();
        statusBar.setText("");
        statusLabel.setText("Відкрийте файл HAM");
        labelHamModel.setText(" ");
        labelHamNumber.setText(" ");
        labelFileName.setText(" ");
        labelFileData.setText(" ");
        labelFileTime.setText(" ");
        labelLineCount.setText(" ");
        labelAllTime.setText(" ");
        labelPress.setText(" ");
        quaternions.clear();
        eulerAngles.clear();
        results.clear();
        startCut = -1;
        endCut = 0;
        tSave.setDisable(true);
        tCalc.setDisable(true);
        tAcc.setDisable(true);
        tVel.setDisable(true);
        tChart.setDisable(true);
    }

    public void onClickPressureSettings() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/settings.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Наземний тиск   " + openFile);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/HAM.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onClickCut() throws IOException {
        eulerAngles = quaternionToEulerAnglesBulk(quaternions);
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/cut.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Обрізка даних   " + openFile);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/HAM.png")));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @SneakyThrows
    public void updateLabel() {
        getSettings.getSettings();
        labelPress.setText("Тиск на рівні землі \n" + pressureNull + "  Pa");
    }

    public void progressIndicatorRun() {
        Platform.runLater(() -> {
            os.viewURL = "/view/wait.fxml";
            os.title = "Збереження файлу   " + openFile;
            try {
                os.openStage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void onClickCancelBtn() {
        System.exit(0);
    }


}


