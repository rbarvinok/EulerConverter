package ua.euler.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import ua.euler.javaclass.domain.EulerAngles;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ua.euler.controller.Controller.openFile;

public class chartVelocityController implements Initializable {

    @FXML
    public LineChart lineChartTime, lineChartAlt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        ScatterChart<Number, Number> lcc = new ScatterChart<Number, Number>(x, y);
        lcc.setTitle("Вертикальна швидкість" + openFile);
        x.setLabel("Час,c");
        y.setLabel("Швидкість");

//Time
        XYChart.Series series1 = new XYChart.Series();
        //series1.setName("Швидкість");


        List<EulerAngles.TimeVelocity> eulerTimeVelocity = Controller.eulerAngles.stream().map(eulerAngles -> {
            return new EulerAngles.TimeVelocity(eulerAngles.getTime(), eulerAngles.getVelocity());
        }).collect(Collectors.toList());

        ObservableList<XYChart.Data> Vel = FXCollections.observableArrayList();
        for (EulerAngles.TimeVelocity timeVelocity : eulerTimeVelocity) {
            Vel.add(new XYChart.Data(Double.parseDouble(timeVelocity.getTime()), timeVelocity.getVelocity()));
        }

        series1.setData(Vel);

        lineChartTime.getData().addAll(series1);


        //        Alt                 ///////////////////////////////

        XYChart.Series series2 = new XYChart.Series();
        //series1.setName("Висота");

        List<EulerAngles.TimeAltitude> eulerTimeAltitude = Controller.eulerAngles.stream().map(eulerAngles -> {
            return new EulerAngles.TimeAltitude(eulerAngles.getTime(), eulerAngles.getAltitude());
        }).collect(Collectors.toList());

        ObservableList<XYChart.Data> Alt = FXCollections.observableArrayList();
        for (EulerAngles.TimeAltitude timeAltitude : eulerTimeAltitude) {
            Alt.add(new XYChart.Data(Double.parseDouble(timeAltitude.getTime()), timeAltitude.getAltitude()));
        }

        series2.setData(Alt);

        lineChartAlt.getData().addAll(series2);

    }
}