package IotDomain;

import java.time.Duration;
import java.util.Calendar;

public class Constants {
    /**
     * Should always be a single month!
     */
    public static final Duration DEVICE_ADJUSTMENT_RATE = Duration.ofDays(30);
    public static final Calendar SIMULATION_BEGINNING;
    public static final float AGING_COMPENSATION_COEFFICIENT = .95f;
    public static final Duration DEVICE_LIFESPAN = Duration.ofDays(365 * 20);
    public static final Duration SIMULATION_STEP_TIME = Duration.ofMinutes(30);
    public static final float ENERGY_ADJUSTMENT_MULTIPLIER = 30f;


    static {
         SIMULATION_BEGINNING = Calendar.getInstance();
         SIMULATION_BEGINNING.set(2022,Calendar.JANUARY,1,0,0);
    }

}
