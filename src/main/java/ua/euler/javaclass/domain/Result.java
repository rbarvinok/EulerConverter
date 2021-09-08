package ua.euler.javaclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class Result {

    private String time;
    private double ax;
    private double ay;
    private double az;
    private double aabs;
    private double roll;
    private double pitch;
    private double yaw;
    private double altitude;
    private double velocity;
    private double pressure;
    private double temperature;

    @AllArgsConstructor
    @Data
    public static class TimeResult {
        private String time;
        private double ax;
        private double ay;
        private double az;
        private double aabs;
        private double roll;
        private double pitch;
        private double yaw;
        private double altitude;
        private double velocity;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return time + "," + ax + "," + ay + "," + az+  "," + aabs+ "," + roll + "," + pitch + "," + yaw +  "," + altitude + "," + velocity+ "," + pressure + "," + temperature +"\n";
    }

}
