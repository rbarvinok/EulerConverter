package ua.euler.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import ua.euler.javaclass.domain.Quaternion;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

//не подтягивается нормально
public class chartQuaternionController implements Initializable {

    @FXML
    public ScatterChart scatterChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        ScatterChart<Number, Number> scc = new ScatterChart<Number, Number>(x, y);
        scc.setTitle("Кватерніони");
        x.setLabel("Час,UTC");
        y.setLabel("Кватерніон");


        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Qw");

        XYChart.Series series2 = new XYChart.Series();
        series1.setName("Qx");

        XYChart.Series series3 = new XYChart.Series();
        series2.setName("Qy");

        XYChart.Series series4 = new XYChart.Series();
        series2.setName("Qz");

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
            Qw.add(new XYChart.Data(timeW.getTime(), timeW.getW()));
        }

        ObservableList<XYChart.Data> Qx = FXCollections.observableArrayList();
        for (Quaternion.TimeX timeX : quaternionTimeX) {
            Qx.add(new XYChart.Data(timeX.getTime(), timeX.getX()));
        }

        ObservableList<XYChart.Data> Qy = FXCollections.observableArrayList();
        for (Quaternion.TimeY timeY : quaternionTimeY) {
            Qy.add(new XYChart.Data(timeY.getTime(), timeY.getY()));
        }
        ObservableList<XYChart.Data> Qz = FXCollections.observableArrayList();
        for (Quaternion.TimeZ timeZ : quaternionTimeZ) {
            Qz.add(new XYChart.Data(timeZ.getTime(), timeZ.getZ()));
        }

//
        series1.setData(Qw);
        series1.setData(Qx);
        series1.setData(Qy);
        series1.setData(Qz);

       scatterChart.getData().addAll(Qw, Qx, Qy,Qz);
    }
}