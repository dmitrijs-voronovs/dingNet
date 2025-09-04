package utils;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class TimeHelper {
    public static final long NANOS_IN_MILLI = Duration.ofMillis(1).toNanos();

    public static long localTimeToMillis(LocalTime previousDepartureTime) {
        return previousDepartureTime.toNanoOfDay() / NANOS_IN_MILLI;
    }

    public static Duration parseDuration(long initialAgeN, String initialAgeUnit) {
        return ChronoUnit.valueOf(initialAgeUnit.toUpperCase()).getDuration().multipliedBy(initialAgeN);
    }

    public static Calendar parseCalendar(String value, Calendar defaultValue) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.valueOf(value));
            return calendar;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
