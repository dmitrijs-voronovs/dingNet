package SelfAdaptation.FeedbackLoop.utils;

import java.time.Duration;

public class AgingAdjustmentCalculator {
    private float agingFactorUnit;
    private final Duration deviceLifespan;
    private final Duration timeMeasurement;
    private final Duration deviceAdjustmentRate;
    private final float compensationCoefficient;

    public AgingAdjustmentCalculator(Duration deviceLifespan, Duration timeMeasurement, Duration deviceAdjustmentRate, float compensationCoefficient) {
        this.deviceLifespan = deviceLifespan;
        this.timeMeasurement = timeMeasurement;
        this.deviceAdjustmentRate = deviceAdjustmentRate;
        this.compensationCoefficient = compensationCoefficient;
        computeAgingFactorUnit();
    }

    private void computeAgingFactorUnit() {
        this.agingFactorUnit = (float) timeMeasurement.toNanos() / deviceLifespan.toNanos();
    }


    public float getAgingFactorUnit() {
        return agingFactorUnit;
    }

    public float getAgingFactorAdjustment() {
        float adjustmentsPerAdaptationCycle = (float) deviceAdjustmentRate.toNanos() / timeMeasurement.toNanos();
        return - getAgingFactorUnit() * adjustmentsPerAdaptationCycle * compensationCoefficient;
    }
}
