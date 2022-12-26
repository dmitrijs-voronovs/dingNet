package SelfAdaptation.FeedbackLoop.utils;

import java.time.Duration;

public class AgingAdjustmentCalculator {
    private float agingFactorUnit;
    private final Duration deviceLifespan;
    private final Duration timeMeasurement;
    private final float compensationCoefficient;

    public AgingAdjustmentCalculator(Duration deviceLifespan, Duration timeMeasurement, float compensationCoefficient) {
        this.deviceLifespan = deviceLifespan;
        this.timeMeasurement = timeMeasurement;
        this.compensationCoefficient = compensationCoefficient;
        computeAgingFactorUnit();
    }

    private void computeAgingFactorUnit() {
        this.agingFactorUnit = (float) timeMeasurement.toNanos() / deviceLifespan.toNanos();
    }

    public float getAgingFactorUnit() {
        return agingFactorUnit;
    }
    public float getAgingFactor(Duration period) {
        return agingFactorUnit * totalUnitsPerPeriod(period);
    }

    public float getAgingFactorAdjustment(Duration period) {
        return - getAgingFactor(period) * compensationCoefficient;
    }

    private float totalUnitsPerPeriod(Duration period) {
        return (float) period.toNanos() / timeMeasurement.toNanos();
    }
}
