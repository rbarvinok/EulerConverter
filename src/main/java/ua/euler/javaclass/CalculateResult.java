package ua.euler.javaclass;

import lombok.experimental.UtilityClass;
import ua.euler.javaclass.domain.Quaternion;
import ua.euler.javaclass.domain.Result;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;
import static java.lang.Math.*;
import static java.lang.StrictMath.pow;
import static java.lang.StrictMath.sqrt;
import static ua.euler.controller.Controller.pressureNull;
import static ua.euler.javaclass.AccelerationCalculate.accelCoefficient;
import static ua.euler.javaclass.QuaternionToEulerAnglesConvectorNonNormalised.period;


@UtilityClass
public class CalculateResult {
    int round = 10000;

    public static Result calculateResult(Quaternion quaternion) {

        Result result = new Result();
        double sqw = quaternion.getW() * quaternion.getW();
        double sqx = quaternion.getX() * quaternion.getX();
        double sqy = quaternion.getY() * quaternion.getY();
        double sqz = quaternion.getZ() * quaternion.getZ();
        double unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise is correction factor
        double test = quaternion.getX() * quaternion.getY() + quaternion.getZ() * quaternion.getW();

        double Aax = quaternion.getAx() / accelCoefficient;
        double Aay = quaternion.getAy() / accelCoefficient;
        double Aaz = quaternion.getAz() / accelCoefficient;
        double Aalt = 44330 * (1 - pow(quaternion.getPressure() / pressureNull, 1 / 5.255));

        result.setTime(quaternion.getTime());
        result.setAx(rint(Aax*round)/round);
        result.setAy(rint(Aay*round)/round);
        result.setAz(rint(Aaz*round)/round);
        result.setAabs(rint(sqrt(pow(Aax, 2) + pow(Aay, 2) + pow(Aaz, 2))*round)/round);
        result.setAltitude(rint(Aalt*round)/round);

        if (test > 0.499 * unit) { // singularity at north pole
            result.setRoll(toDegrees(2 * atan2(quaternion.getX(), quaternion.getW())));
            result.setPitch(toDegrees(PI / 2));
            result.setYaw(toDegrees(0));
        } else {
            if (test < -0.499 * unit) { // singularity at south pole
                result.setRoll(toDegrees(-2 * atan2(quaternion.getX(), quaternion.getW())));
                result.setPitch(toDegrees(-PI / 2));
                result.setYaw(toDegrees(0));
            } else {
                result.setRoll(toDegrees(atan2(2 * quaternion.getY() * quaternion.getW() - 2 * quaternion.getX() * quaternion.getZ(), sqx - sqy - sqz + sqw)));
                result.setPitch(toDegrees(asin(2 * test / unit)));
                result.setYaw(toDegrees(atan2(2 * quaternion.getX() * quaternion.getW() - 2 * quaternion.getY() * quaternion.getZ(), -sqx + sqy - sqz + sqw)));
            }
        }

        // pressure
        double press = quaternion.getPressure();
        result.setPressure(rint(press* round) / round);

            //double temperature
        result.setTemperature(quaternion.getTemperature()/1000);

        return result;
    }

    public static List<Result> calculateAltVel(List<Result> result) {
        for (int i = period; i < result.size(); i++) {
            double deltaTime = parseDouble(result.get(i).getTime()) - parseDouble(result.get(i - period).getTime());
            double deltaAlt = result.get(i).getAltitude() - result.get(i - period).getAltitude();
            double velocity = deltaAlt / deltaTime;
            result.get(i).setVelocity(rint(velocity* round)/ round);
        }

        return result;
    }



    public static List<Result> resultsBulk(List<Quaternion> quaternions) {
        return quaternions.stream().map(CalculateResult::calculateResult).collect(Collectors.toList());
    }

}
