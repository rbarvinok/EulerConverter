package ua.euler.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import ua.euler.javaclass.domain.EulerAngles;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ua.euler.controller.Controller.openFile;

public class chartEulerController implements Initializable {

    @FXML
    public LineChart lineChart, lineChartAlt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        LineChart<Number, Number> lcc = new LineChart<Number, Number>(x, y);
        lcc.setTitle("Кути Ейлера " + openFile);
        x.setLabel("Час,UTC");
        y.setLabel("Кут, град");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Курс");

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Крен");

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Тангаж");

//  Углы Эйлера
        List<EulerAngles.TimeRoll> eulerTimeRoll = Controller.eulerAngles.stream().map(eulerAngles -> {
            return new EulerAngles.TimeRoll(eulerAngles.getTime(), eulerAngles.getRoll());
        }).collect(Collectors.toList());

        List<EulerAngles.TimePith> eulerTimePith = Controller.eulerAngles.stream().map(eulerAngles -> {
            return new EulerAngles.TimePith(eulerAngles.getTime(), eulerAngles.getPitch());
        }).collect(Collectors.toList());

        List<EulerAngles.TimeYaw> eulerTimeYaw = Controller.eulerAngles.stream().map(eulerAngles -> {
            return new EulerAngles.TimeYaw(eulerAngles.getTime(), eulerAngles.getYaw());
        }).collect(Collectors.toList());


        ObservableList<XYChart.Data> roll = FXCollections.observableArrayList();
        for (EulerAngles.TimeRoll time :eulerTimeRoll) {
        roll.add(new XYChart.Data(Double.parseDouble(time.getTime()), time.getRoll()));
        }

        ObservableList<XYChart.Data> pitch = FXCollections.observableArrayList();
        for (EulerAngles.TimePith time :eulerTimePith) {
        pitch.add(new XYChart.Data(Double.parseDouble(time.getTime()), time.getPith()));
        }

        ObservableList<XYChart.Data> yaw = FXCollections.observableArrayList();
        for (EulerAngles.TimeYaw time :eulerTimeYaw) {
        yaw.add(new XYChart.Data(Double.parseDouble(time.getTime()), time.getYaw()));
        }

        series1.setData(roll);
        series2.setData(pitch);
        series3.setData(yaw);

        lineChart.getData().addAll(series1, series2, series3);


        //        Alt                 ///////////////////////////////


        XYChart.Series series = new XYChart.Series();
        //series1.setName("Швидкість");


        List<EulerAngles.TimeAltitude> eulerTimeAltitude = Controller.eulerAngles.stream().map(eulerAngles -> {
            return new EulerAngles.TimeAltitude(eulerAngles.getTime(), eulerAngles.getAltitude());
        }).collect(Collectors.toList());

        ObservableList<XYChart.Data> Alt = FXCollections.observableArrayList();
        for (EulerAngles.TimeAltitude timeAltitude : eulerTimeAltitude) {
            Alt.add(new XYChart.Data(Double.parseDouble(timeAltitude.getTime()), timeAltitude.getAltitude()));
        }

        series.setData(Alt);

        lineChartAlt.getData().

                addAll(series);


    }
}