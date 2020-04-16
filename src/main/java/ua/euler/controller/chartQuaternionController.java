package ua.euler.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import ua.euler.javaclass.domain.Quaternion;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ua.euler.controller.Controller.openFile;

//не подтягивается нормально
public class chartQuaternionController implements Initializable {

    @FXML
    public LineChart lineChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        ScatterChart<Number, Number> lcc = new ScatterChart<Number, Number>(x, y);
        lcc.setTitle("Кватерніони" + openFile);
        x.setLabel("Час,UTC");
        y.setLabel("Кватерніон");


        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Qw");

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Qx");

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Qy");

        XYChart.Series series4 = new XYChart.Series();
        series4.setName("Qz");

//Кватернионы
        List<Quaternion.TimeW> quaternionTimeW = Controller.quaternions.stream().map(quaternion -> {
            return new Quaternion.TimeW(quaternion.getTime(), quaternion.getW());
        }).collect(Collectors.toList());

        List<Quaternion.TimeX> quaternionTimeX = Controller.quaternions.stream().map(quaternion -> {
            return new Quaternion.TimeX(quaternion.getTime(), quaternion.getX());
        }).collect(Collectors.toList());

        List<Quaternion.TimeY> quaternionTimeY = Controller.quaternions.stream().map(quaternion -> {
            return new Quaternion.TimeY(quaternion.getTime(), quaternion.getY());
        }).collect(Collectors.toList());

        List<Quaternion.TimeZ> quaternionTimeZ = Controller.quaternions.stream().map(quaternion -> {
            return new Quaternion.TimeZ(quaternion.getTime(), quaternion.getZ());
        }).collect(Collectors.toList());

        ObservableList<XYChart.Data> Qw = FXCollections.observableArrayList();
        for (Quaternion.TimeW timeW : quaternionTimeW) {
            Qw.add(new XYChart.Data(Double.parseDouble(timeW.getTime()), timeW.getW()));
        }

        ObservableList<XYChart.Data> Qx = FXCollections.observableArrayList();
        for (Quaternion.TimeX timeX : quaternionTimeX) {
            Qx.add(new XYChart.Data(Double.parseDouble(timeX.getTime()), timeX.getX()));
        }

        ObservableList<XYChart.Data> Qy = FXCollections.observableArrayList();
        for (Quaternion.TimeY timeY : quaternionTimeY) {
            Qy.add(new XYChart.Data(Double.parseDouble(timeY.getTime()), timeY.getY()));
        }

        ObservableList<XYChart.Data> Qz = FXCollections.observableArrayList();
        for (Quaternion.TimeZ timeZ : quaternionTimeZ) {
            Qz.add(new XYChart.Data(Double.parseDouble(timeZ.getTime()), timeZ.getZ()));
        }

        series1.setData(Qw);
        series2.setData(Qx);
        series3.setData(Qy);
        series4.setData(Qz);

       lineChart.getData().addAll(series1, series2, series3,series4);
    }
}