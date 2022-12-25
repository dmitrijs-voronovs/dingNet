package utils;

import java.time.Duration;
import java.time.LocalTime;

public class TimeHelper {
    public static final long NANOS_IN_MILLI = Duration.ofMillis(1).toNanos();

    public static long localTimeToMillis(LocalTime previousDepartureTime) {
        return previousDepartureTime.toNanoOfDay() / NANOS_IN_MILLI;
    }
}
