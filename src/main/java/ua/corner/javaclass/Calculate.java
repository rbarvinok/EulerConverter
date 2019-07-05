package ua.corner.javaclass;

import static java.lang.StrictMath.tan;
import static java.lang.StrictMath.toRadians;

public class Calculate {
    public static double a, d, b;

    public double beam() {
        b = Math.rint(d * tan(toRadians(a)) * 100) / 100; //округление
        return b;
    }

}
