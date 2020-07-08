package ua.euler.javaclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Quaternion {

    private String time;
    private double w;
    private double x;
    private double y;
    private double z;
    private double pressure;


    @AllArgsConstructor
    @Data
    public static class TimeW {
        private String time;
        private double w;
    }

    @AllArgsConstructor
    @Data
    public static class TimeX {
        private String time;
        private double x;
    }

    @AllArgsConstructor
    @Data
    public static class TimeY {
        private String time;
        private double y;
    }

    @AllArgsConstructor
    @Data
    public static class TimeZ {
        private String time;
        private double z;
    }

    @AllArgsConstructor
    @Data
    public static class TimePressure {
        private String time;
        private double pressure;
    }

    @Override
    public String toString() {
        return time + ",    " + w + ",    " + x + ",    " + y + ",    " + z +  "\n";

    }
}
