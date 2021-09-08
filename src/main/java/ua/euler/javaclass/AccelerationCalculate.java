package ua.euler.javaclass;

import lombok.experimental.UtilityClass;
import ua.euler.javaclass.domain.Acceleration;
import ua.euler.javaclass.domain.Quaternion;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;
import static ua.euler.controller.Controller.pressureNull;

@UtilityClass
public class AccelerationCalculate {
    public static int accelCoefficient = 2048;
    private double Aax;
    private double Aay;
    private double Aaz;
    private double Aalt;

    public static Acceleration calculateAccelerate(Quaternion quaternion) {

        Acceleration accelerations = new Acceleration();

        Aax = quaternion.getAx() / accelCoefficient;
        Aay = quaternion.getAy() / accelCoefficient;
        Aaz = quaternion.getAz() / accelCoefficient;
        Aalt = 44330 * (1 - pow(quaternion.getPressure()/ pressureNull, 1 / 5.255));

        accelerations.setTime(quaternion.getTime());
        accelerations.setAx(Aax);
        accelerations.setAy(Aay);
        accelerations.setAz(Aaz);
        accelerations.setAabs(sqrt(pow(Aax, 2) + pow(Aay, 2) + pow(Aaz, 2)));
        accelerations.setAlt(Aalt);

        return accelerations;
    }

    public static List<Acceleration> calculateAccelerateBulk(List<Quaternion> quaternions) {
        return quaternions.stream().map(AccelerationCalculate::calculateAccelerate).collect(Collectors.toList());
    }


}
