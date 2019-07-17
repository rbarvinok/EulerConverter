package ua.corner.javaclass;

import static java.lang.StrictMath.tan;
import static java.lang.StrictMath.toRadians;

public class Calculate {

    public static double f, wave, epr, length;

    public double roundLength() {
        length = Math.rint(epr * tan(toRadians(f)) * 100) / 100; //округление
        return length;
    }

    public double sqLength() {
        length = Math.rint(epr * tan(toRadians(f)) * 100) / 100; //округление
        return length;
    }

    public double frequencyToWavelength() {
        f = Math.rint(1 / wave * 100) / 100; //округление
        return f;
    }

    public double wavelengthToFrequency() {
        wave = Math.rint(1 / f * 100) / 100; //округление
        return wave;
    }
}

