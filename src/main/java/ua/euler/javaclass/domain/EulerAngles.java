package ua.euler.javaclass.domain;

import lombok.Data;
import lombok.SneakyThrows;

@Data
public class EulerAngles {

    private double roll;
    private double pitch;
    private double yaw;

    @SneakyThrows
    @Override
    public String toString() {
        return roll + "," + pitch + "," + yaw + "\n";
    }
}
