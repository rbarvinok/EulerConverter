package ua.euler.javaclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class EulerAngles {

    private String time;
    private double roll;
    private double pitch;
    private double yaw;
    private double altitude;
    private double velocity;
    private double pressure;


    @AllArgsConstructor
    @Data
    public static class TimeRoll {
        private String time;
        private double roll;
    }

    @AllArgsConstructor
    @Data
    public static class TimePith {
        private String time;
        private double pith;
    }

    @AllArgsConstructor
    @Data
    public static class TimeYaw {
        private String time;
        private double yaw;
    }

    @AllArgsConstructor
    @Data
    public static class TimePressure {
        private String time;
        private double pressure;
    }

    @AllArgsConstructor
    @Data
    public static class TimeAltitude {
        private String time;
        private double altitude;
    }

    @AllArgsConstructor
    @Data
    public static class TimeVelocity {
        private String time;
        private double velocity;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return time + ",    " + roll + ",    " + pitch + ",    " + yaw +  ",    " + altitude + ",    " + velocity+ "\n";
    }
}
