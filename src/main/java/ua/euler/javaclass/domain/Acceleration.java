package ua.euler.javaclass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

@Data
public class Acceleration {

    private String time;
    private double ax;
    private double ay;
    private double az;
    private double aabs;
    private double alt;

    @AllArgsConstructor
    @Data
    public static class TimeA {
        private String time;
        private double ax;
        private double ay;
        private double az;
        private double aabs;
        private double alt;
    }

//    @AllArgsConstructor
//    @Data
//    public static class TimeAX {
//        private String time;
//        private double ax;
//    }
//
//    @AllArgsConstructor
//    @Data
//    public static class TimeAy {
//        private String time;
//        private double ay;
//    }
//
//    @AllArgsConstructor
//    @Data
//    public static class TimeAz {
//        private String time;
//        private double az;
//    }
//
//    @AllArgsConstructor
//    @Data
//    public static class TimeAabs {
//        private String time;
//        private double aabs;
//    }
//
//    @AllArgsConstructor
//    @Data
//    public static class TimeAltitude {
//        private String time;
//        private double altitude;
//    }



    @SneakyThrows
    @Override
    public String toString() {
        return time + "," + ax + "," + ay + "," + az +  "," + aabs +  "\n";
    }


}
