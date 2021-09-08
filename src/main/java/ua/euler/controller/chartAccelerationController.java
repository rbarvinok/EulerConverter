package ua.euler.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import ua.euler.javaclass.domain.Acceleration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class chartAccelerationController implements Initializable {

    @FXML
    public LineChart lineChart, lineChartAlt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        LineChart<Number, Number> lcc = new LineChart<Number, Number>(x, y);

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Ax");

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Ay");

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Az");

        XYChart.Series series4 = new XYChart.Series();
        series4.setName("Aabs");

        List<Acceleration.TimeA> timeA = Controller.accelerations.stream().map(acceleration -> {
            return new Acceleration.TimeA(acceleration.getTime(), acceleration.getAx(), acceleration.getAy(), acceleration.getAz(), acceleration.getAabs(), acceleration.getAlt());
        }).collect(Collectors.toList());

        ObservableList<XYChart.Data> Ax = FXCollections.observableArrayList();
        for (Acceleration.TimeA time : timeA) {
            Ax.add(new XYChart.Data(Double.parseDouble(time.getTime()), time.getAx()));
        }

        ObservableList<XYChart.Data> Ay = FXCollections.observableArrayList();
        for (Acceleration.TimeA time : timeA) {
            Ay.add(new XYChart.Data(Double.parseDouble(time.getTime()), time.getAy()));
        }

        ObservableList<XYChart.Data> Az = FXCollections.observableArrayList();
        for (Acceleration.TimeA time : timeA) {
            Az.add(new XYChart.Data(Double.parseDouble(time.getTime()), time.getAz()));
        }

        ObservableList<XYChart.Data> Aabs = FXCollections.observableArrayList();
        for (Acceleration.TimeA time : timeA) {
            Aabs.add(new XYChart.Data(Double.parseDouble(time.getTime()), time.getAabs()));
        }

        series1.setData(Ax);
        series2.setData(Ay);
        series3.setData(Az);
        series4.setData(Aabs);

        lineChart.getData().addAll(series1, series2, series3, series4);


        //        Alt    ///////////////////////////////
        XYChart.Series series = new XYChart.Series();

        ObservableList<XYChart.Data> Alt = FXCollections.observableArrayList();
        for (Acceleration.TimeA time : timeA) {
            Alt.add(new XYChart.Data(Double.parseDouble(time.getTime()), time.getAlt()));
        }

        series.setData(Alt);

        lineChartAlt.getData().addAll(series);
    }
}