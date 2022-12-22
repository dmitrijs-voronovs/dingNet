package IotDomain;

import java.time.Duration;
import java.util.Calendar;

public class Constants {
    public static final Duration DEVICE_ADJUSTMENT_RATE = Duration.ofHours(2);
    public static final Calendar SIMULATION_BEGINNING;
    public static final float COMPENSATION_COEFFICIENT = 0.9f;
    public static final Duration DEVICE_LIFESPAN = Duration.ofDays(365 * 15);
    public static final Duration SIMULATION_TIME_MEASUREMENT = Duration.ofHours(1);


    static {
         SIMULATION_BEGINNING = Calendar.getInstance();
         SIMULATION_BEGINNING.set(2022,Calendar.JANUARY,1,0,0);
    }

}
