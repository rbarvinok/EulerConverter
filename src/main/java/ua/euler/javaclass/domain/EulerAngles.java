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


    @SneakyThrows
    @Override
    public String toString() {
        return time + ",    " + roll + ",    " + pitch + ",    " + yaw + "\n";
    }
}
