package SelfAdaptation.FeedbackLoop.utils;

import java.time.Duration;
import java.util.Calendar;

public class EnergyAdjustmentCalculator {
    private enum Weather {
        COLD,
        CHILL,
        WARM,
        HOT
    }
    private static final float USAGE_FACTOR = 1.05f;
    public static final int DAYS_IN_MONTH = 30;
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
    private final float energyAdjustmentMultiplier;

    public EnergyAdjustmentCalculator(float energyAdjustmentMultiplier) {
        this.energyAdjustmentMultiplier = energyAdjustmentMultiplier;
    }

    public double calculateEnergyToAdd(Calendar simulationBeginning, Duration currentTime, Duration moteAge) {
        Calendar adaptationTime = getAdaptationTime(simulationBeginning, currentTime);
        int moteAgeInMoths = (int) Math.floor(moteAge.toDays() / DAYS_IN_MONTH);
        if (moteAgeInMoths == 0) return 0;

        int monthOfYear = adaptationTime.get(Calendar.MONTH);
//        double energyDelta = calculateDelta(moteAgeInMoths, monthOfYear) - calculateDelta(moteAgeInMoths - 1, Math.floorMod(monthOfYear - 1, 12));
//        TODO: should we compare months with the same temperature?
        double energyDelta = calculateDelta(moteAgeInMoths, monthOfYear) - calculateDelta(moteAgeInMoths - 1, monthOfYear);
        return energyAdjustmentMultiplier * energyDelta;
    }

    private static Calendar getAdaptationTime(Calendar simulationBeginning, Duration currentTime) {
        Calendar adaptationTime = ((Calendar) simulationBeginning.clone());
        int currentTimeInMonths = (int) Math.floor(currentTime.toDays() / DAYS_IN_MONTH);
//        TODO: check if calendar + 13 months increments years as well?
        adaptationTime.add(Calendar.MONTH, currentTimeInMonths);
        return adaptationTime;
    }

    private double calculateDelta(int month, int monthOfYearIdx) {
        return calculateEnergyDeltaForMonth(month) * USAGE_FACTOR * getWeatherMonthCoefficient(monthOfYearIdx);
    }

    private double calculateEnergyDeltaForMonth(int monthOfYearIdx) {
        return 1 + 0.595 * (1 - Math.pow(Math.E, -(monthOfYearIdx/6.517)));
    }

    /**
     * @param monthOfYearIdx idx on the month [0..11]
     * @return temperature coefficient
     */
    private float getWeatherMonthCoefficient(int monthOfYearIdx) {
        Weather weather = yearlyWeatherByMonth[Math.floorMod(monthOfYearIdx - 1, 12)];
        return switch (weather) {
            case COLD, HOT -> 1.05f;
            case CHILL -> 1.01f;
            case WARM -> 1;
        };
    }
}
