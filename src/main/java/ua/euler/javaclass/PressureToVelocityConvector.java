package ua.euler.javaclass;

import lombok.experimental.UtilityClass;
import ua.euler.javaclass.domain.Quaternion;
import ua.euler.javaclass.domain.RateOfDecline;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;
import static ua.euler.controller.Controller.pressureNull;

@UtilityClass
public class PressureToVelocityConvector {
    //public static double pressureNull = 101325.0;

    public static RateOfDecline PressureToVelocity(Quaternion quaternion) {

        RateOfDecline rateOfDecline = new RateOfDecline();

        // pressure
        double press = quaternion.getPressure();
        rateOfDecline.setPressure(press);

        // altitude
        double alt = 44330 * (1 - pow(press / pressureNull, 1 / 5.255));
        rateOfDecline.setAltitude(alt);

        //time
        double time = Double.parseDouble(quaternion.getTime());
        rateOfDecline.setTime(quaternion.getTime());

        // velocity
        double deltaAlt = alt;
        double deltaTime = time;
        rateOfDecline.setVelocity(deltaAlt / deltaTime);

        return rateOfDecline;
    }

    public static List<RateOfDecline> rateOfDeclineBulk(List<Quaternion> quaternions) {
        return quaternions.stream().map(PressureToVelocityConvector::PressureToVelocity).collect(Collectors.toList());
    }

}
