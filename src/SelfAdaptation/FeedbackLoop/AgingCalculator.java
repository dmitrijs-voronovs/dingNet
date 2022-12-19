package SelfAdaptation.FeedbackLoop;

import java.util.Calendar;
import java.util.Date;

enum Weather {
    COLD,
    CHILL,
    WARM,
    HOT
}

public class AgingCalculator {
    private static final Weather[] yearlyWeatherByMonth = {
            Weather.COLD, //Jan
            Weather.COLD, //Feb
            Weather.CHILL, //Mar
            Weather.CHILL, //Apr
            Weather.WARM, //May
            Weather.WARM, //June
            Weather.HOT, //July
            Weather.HOT, //Aug
            Weather.CHILL, //Sep
            Weather.COLD, //Oct
            Weather.COLD, //Nov
            Weather.COLD, //Dec

    };

    private static final float USAGE_FACTOR = 1.05f;

    static int calculateEnergyToAdd(Date simulationBeginning, float time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(simulationBeginning);
        cal.add(Calendar.MONTH, Math.round(time));
        int monthN = cal.get(Calendar.MONTH);

        if (monthN <= 0) return 0;
        return calculateDelta(monthN + 1) - calculateDelta(monthN);
    }

    private static int calculateDelta(int monthN) {
        return (int) Math.round(calculateEnergyDeltaForMonth(monthN) * USAGE_FACTOR * getWeatherMonthCoefficient(monthN));
    }

    private static double calculateEnergyDeltaForMonth(int monthN) {
        return 1 + 0.595 * (1 - Math.pow(Math.E, -((double)monthN/6517)));
    }

    /**
     * @param monthN [1..12]
     * @return temperature coefficient
     */
    private static float getWeatherMonthCoefficient(int monthN) {
        Weather weather = yearlyWeatherByMonth[(monthN - 1) % 12];
        return switch (weather) {
            case COLD, HOT -> 1.05f;
            case CHILL -> 1.01f;
            case WARM -> 1;
        };
    }
}
