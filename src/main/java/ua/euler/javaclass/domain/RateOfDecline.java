package ua.euler.javaclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class RateOfDecline {

    private String time;
    private double pressure;
    private double altitude;
    private double velocity;


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
        return time + ",    " + pressure + ",    " + altitude + ",    " + velocity + "\n";
    }





}
