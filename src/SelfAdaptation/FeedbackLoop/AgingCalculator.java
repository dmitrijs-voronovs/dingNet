package SelfAdaptation.FeedbackLoop;

import java.util.Calendar;

enum Weather {
    COLD,
    CHILL,
    WARM,
    HOT
}

public class AgingCalculator {
    private final Weather[] yearlyWeatherByMonth = {
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

    int calculateEnergyToAdd(Calendar simulationBeginning, float time) {
        simulationBeginning.add(Calendar.MONTH, Math.round(time));
        int monthN = simulationBeginning.get(Calendar.MONTH);

        if (monthN <= 0) return 0;
        return calculateDelta(monthN + 1) - calculateDelta(monthN);
    }

    private int calculateDelta(int monthN) {
        float USAGE_FACTOR = 1.05f;
        return (int) Math.round(calculateEnergyDeltaForMonth(monthN) * USAGE_FACTOR * getWeatherMonthCoefficient(monthN));
    }

    private double calculateEnergyDeltaForMonth(int monthN) {
        return 1 + 0.595 * (1 - Math.pow(Math.E, -((double)monthN/6517)));
    }

    /**
     * @param monthN [1..12]
     * @return temperature coefficient
     */
    private float getWeatherMonthCoefficient(int monthN) {
        Weather weather = yearlyWeatherByMonth[(monthN - 1) % 12];
        return switch (weather) {
            case COLD, HOT -> 1.05f;
            case CHILL -> 1.01f;
            case WARM -> 1;
        };
    }
}
