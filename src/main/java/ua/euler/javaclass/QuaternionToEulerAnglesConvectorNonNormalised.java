package ua.euler.javaclass;

import lombok.experimental.UtilityClass;
import ua.euler.javaclass.domain.EulerAngles;
import ua.euler.javaclass.domain.Quaternion;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;

@UtilityClass
public class QuaternionToEulerAnglesConvectorNonNormalised {

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
            } else
            eulerAngles.setRoll(toDegrees(atan2(2 * quaternion.getY() * quaternion.getW() - 2 * quaternion.getX() * quaternion.getZ(), sqx - sqy - sqz + sqw)));
            eulerAngles.setPitch(toDegrees(asin(2 * test / unit)));
            eulerAngles.setYaw(toDegrees(atan2(2 * quaternion.getX() * quaternion.getW() - 2 * quaternion.getY() * quaternion.getZ(), -sqx + sqy - sqz + sqw)));
        }
        eulerAngles.setTime(quaternion.getTime());

        return eulerAngles;
    }

    public static List<EulerAngles> quaternionToEulerAnglesBulk(List<Quaternion> quaternions) {
        return quaternions.stream().map(QuaternionToEulerAnglesConvectorNonNormalised::quaternionToEulerAngles).collect(Collectors.toList());
    }

    public static String timeFormatter(String time) {
        return time;
    }

}
