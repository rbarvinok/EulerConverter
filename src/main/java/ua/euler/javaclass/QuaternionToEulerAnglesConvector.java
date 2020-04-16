package ua.euler.javaclass;

import lombok.experimental.UtilityClass;
import ua.euler.javaclass.domain.EulerAngles;
import ua.euler.javaclass.domain.Quaternion;
import java.util.List;
import java.util.stream.Collectors;
import static java.lang.Math.*;

@UtilityClass
public class QuaternionToEulerAnglesConvector {

    public static EulerAngles quaternionToEulerAngles(Quaternion quaternion) {

        EulerAngles eulerAngles = new EulerAngles();
        double sinrCosp = 2 * (quaternion.getW() + quaternion.getY() + quaternion.getZ());
        double cosrCosp = 1 - 2 * (quaternion.getX() * quaternion.getX() + quaternion.getX() * quaternion.getY());
        eulerAngles.setRoll(toDegrees(atan2(sinrCosp, cosrCosp)));

        double sinp = 2 * (quaternion.getW() * quaternion.getY() - quaternion.getZ() * quaternion.getX());
        if (abs(sinp) >= 1) {
           eulerAngles.setPitch(copySign(PI / 2, sinp));
        } else {
            eulerAngles.setPitch(toDegrees(asin(sinp)));
        }

        double sinyCosp = 2 * (quaternion.getW() * quaternion.getZ() + quaternion.getX() * quaternion.getY());
        double cosyCosp = 1 - 2 * (quaternion.getY() * quaternion.getY() + quaternion.getZ() * quaternion.getZ());
        eulerAngles.setYaw(toDegrees(atan2(sinyCosp, cosyCosp)));

        eulerAngles.setTime(quaternion.getTime());

        return eulerAngles;
    }

    public static List<EulerAngles> quaternionToEulerAnglesBulk(List<Quaternion> quaternions) {
        return quaternions.stream().map(QuaternionToEulerAnglesConvector::quaternionToEulerAngles).collect(Collectors.toList());
     }

     public static String timeFormatter(String time) {
        return time;
     }

}
