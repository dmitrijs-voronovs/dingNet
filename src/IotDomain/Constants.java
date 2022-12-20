package IotDomain;

import java.time.Duration;
import java.time.Period;
import java.util.Calendar;

public class Constants {
    public static final Period DEVICE_ADJUSTMENT_RATE = Period.ofDays(5);
    public static final Calendar SIMULATION_BEGINNING;
    public static final float COMPENSATION_COEFFICIENT = 0.9f;
    public static final Period DEVICE_LIFESPAN = Period.ofYears(10);
//    public static final Duration SIMULATION_TIME_MEASUREMENT = Duration.ofMinutes(1);
    public static final Duration SIMULATION_TIME_MEASUREMENT = Duration.ofHours(1);


    static {
         SIMULATION_BEGINNING = Calendar.getInstance();
         SIMULATION_BEGINNING.set(2022,Calendar.JANUARY,1,0,0);
    }

}
