package ua.euler.javaclass;

import lombok.experimental.UtilityClass;
import ua.euler.javaclass.domain.EulerAngles;
import ua.euler.javaclass.domain.Quaternion;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;
import static java.lang.Math.*;
import static ua.euler.controller.Controller.pressureNull;

@UtilityClass
public class QuaternionToEulerAnglesConvectorNonNormalised {
    public static int period = 21;

    public static List<EulerAngles> calculateAltVelocity(List<EulerAngles> eulerAngles) {
        for (int i = period; i < eulerAngles.size(); i++) {
            double deltaTime = parseDouble(eulerAngles.get(i).getTime()) - parseDouble(eulerAngles.get(i - period).getTime());
            double deltaAlt = eulerAngles.get(i).getAltitude() - eulerAngles.get(i - period).getAltitude();
            double velocity = deltaAlt / deltaTime;
            eulerAngles.get(i).setVelocity(velocity);
        }

        return eulerAngles;
    }

    public static EulerAngles quaternionToEulerAngles(Quaternion quaternion) {

        EulerAngles eulerAngles = new EulerAngles();

//http://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToEuler/index.htm

        double sqw = quaternion.getW() * quaternion.getW();
        double sqx = quaternion.getX() * quaternion.getX();
        double sqy = quaternion.getY() * quaternion.getY();
        double sqz = quaternion.getZ() * quaternion.getZ();
        double unit = sqx + sqy + sqz + sqw; // if normalised is one, otherwise is correction factor
        double test = quaternion.getX() * quaternion.getY() + quaternion.getZ() * quaternion.getW();

        if (test > 0.499 * unit) { // singularity at north pole
            eulerAngles.setRoll(toDegrees(2 * atan2(quaternion.getX(), quaternion.getW())));
            eulerAngles.setPitch(toDegrees(PI / 2));
            eulerAngles.setYaw(toDegrees(0));
            //return;
        } else {
            if (test < -0.499 * unit) { // singularity at south pole
                eulerAngles.setRoll(toDegrees(-2 * atan2(quaternion.getX(), quaternion.getW())));
                eulerAngles.setPitch(toDegrees(-PI / 2));
                eulerAngles.setYaw(toDegrees(0));
                //return;
            } else {
                eulerAngles.setRoll(toDegrees(atan2(2 * quaternion.getY() * quaternion.getW() - 2 * quaternion.getX() * quaternion.getZ(), sqx - sqy - sqz + sqw)));
                eulerAngles.setPitch(toDegrees(asin(2 * test / unit)));
                eulerAngles.setYaw(toDegrees(atan2(2 * quaternion.getX() * quaternion.getW() - 2 * quaternion.getY() * quaternion.getZ(), -sqx + sqy - sqz + sqw)));
            }
        }
        eulerAngles.setTime(quaternion.getTime());

        // pressure
        double press = quaternion.getPressure();
        eulerAngles.setPressure(quaternion.getPressure());

        // altitude
        double alt = 44330 * (1 - pow(press / pressureNull, 1 / 5.255));
        eulerAngles.setAltitude(alt);

        //time
        eulerAngles.setTime(quaternion.getTime());

        return eulerAngles;
    }

    public static List<EulerAngles> quaternionToEulerAnglesBulk(List<Quaternion> quaternions) {
        return quaternions.stream().map(QuaternionToEulerAnglesConvectorNonNormalised::quaternionToEulerAngles).collect(Collectors.toList());
    }


}
