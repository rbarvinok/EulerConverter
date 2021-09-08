package ua.euler.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import ua.euler.javaclass.domain.EulerAngles;
import ua.euler.javaclass.servisClass.AlertAndInform;
import ua.euler.javaclass.servisClass.GetSettings;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ua.euler.controller.Controller.*;
import static ua.euler.javaclass.servisClass.GetSettings.exposeChart;

public class CutController implements Initializable {
    AlertAndInform inform = new AlertAndInform();
    GetSettings getSettings = new GetSettings();

    public static double startCut = 0;
    public static double endCut = 0;


    @FXML
    public TextField cutStart, cutEnd;
    @FXML
    public Label labelPressureType;
    @FXML
    public Button saveButton, start, end, reset, addChart;
    @FXML
    public ScatterChart chart;

    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        getSettings.getSettings();
        cutStart.setText(String.valueOf(startCut));
        cutEnd.setText(String.valueOf(endCut));
        onClickAddChart();
    }

    @SneakyThrows
    public void onClickSave() throws IOException {
        if (endCut <= startCut) {
            inform.hd = "Помилка!";
            inform.ct = "Початкове значення не може бути більшим за кінцеве\n";
            inform.alert();
            onClickReset();
            return;
        }
        try {
            startCut = Double.parseDouble(cutStart.getText().replace(",", "."));
            endCut = Double.parseDouble(cutEnd.getText().replace(",", "."));

        } catch (NumberFormatException e) {
            inform.hd = "Помилка!";
            inform.ct = "Невірний формат даних\n";
            inform.alert();
            onClickReset();
            return;
        }
        //Controller.onClickUpdate();
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    public void getCurrentDataAlt() {
        ObservableList<XYChart.Data> dataList = ((XYChart.Series) chart.getData().get(0)).getData();
        for (XYChart.Data data : dataList) {
            Node node = data.getNode();
            Tooltip tooltip = new Tooltip("Час: " + data.getXValue().toString() + '\n' + "Висота: " + data.getYValue().toString());
            Tooltip.install(node, tooltip);

            node.setOnMouseEntered(event -> node.getStyleClass().add("onHover"));
            node.setOnMouseExited(event -> node.getStyleClass().remove("onHover"));
        }
    }

    public void getChart() {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        ScatterChart<Number, Number> lcc = new ScatterChart<Number, Number>(x, y);
        lcc.setTitle("Графік висоти" + openFile);
        x.setLabel("Час,c");
        y.setLabel("Висота, м");

        XYChart.Series series2 = new XYChart.Series();

        List<EulerAngles.TimeAltitude> eulerTimeAltitude = Controller.eulerAngles.stream().map(eulerAngles -> {
            return new EulerAngles.TimeAltitude(eulerAngles.getTime(), eulerAngles.getAltitude());
        }).collect(Collectors.toList());

        ObservableList<XYChart.Data> Alt = FXCollections.observableArrayList();
        for (EulerAngles.TimeAltitude timeAltitude : eulerTimeAltitude) {
            Alt.add(new XYChart.Data(Double.parseDouble(timeAltitude.getTime()), timeAltitude.getAltitude()));
        }
        series2.setData(Alt);

        chart.getData().addAll(series2);
        getCurrentDataAlt();
    }

    public void onClickAddChart() {
        if (exposeChart == false) {
            addChart.setText("Показати графік");
            chart.getData().clear();
            exposeChart = true;
        } else if (exposeChart == true) {
            addChart.setText("Приховати графік");
            getChart();
            exposeChart = false;
        }
    }

    public void onClickStart() {
        start.getStyleClass().remove("button1");
        ObservableList<XYChart.Data> dataList = ((XYChart.Series) chart.getData().get(0)).getData();
        for (XYChart.Data data : dataList) {
            Node node = data.getNode();
            node.setOnMouseClicked(event -> cutStart.setText(data.getXValue().toString()));
        }
    }

    public void onClickEnd() {
        end.getStyleClass().remove("button1");
        ObservableList<XYChart.Data> dataList = ((XYChart.Series) chart.getData().get(0)).getData();
        for (XYChart.Data data : dataList) {
            Node node = data.getNode();
            node.setOnMouseClicked(event -> cutEnd.setText(data.getXValue().toString()));
        }
    }

    public void onClickReset() {
        cutStart.setText(String.valueOf(timeStart));
        cutEnd.setText(String.valueOf(timeStop));
    }
}
