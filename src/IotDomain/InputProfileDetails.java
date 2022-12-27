package IotDomain;

import utils.TimeHelper;

import java.time.Duration;
import java.util.Calendar;

public class InputProfileDetails {
    private float agingCompensationCoefficient;
    private float energyAdjustmentMultiplier;
    private String simulationBeginning;
    private Pair<Long, String> deviceAdjustmentRate;
    private Pair<Long, String> deviceLifespan;
    private Pair<Long, String> simulationStepTime;

    public float getAgingCompensationCoefficient() {
        return agingCompensationCoefficient;
    }

    public void setAgingCompensationCoefficient(float agingCompensationCoefficient) {
        this.agingCompensationCoefficient = agingCompensationCoefficient;
    }

    public float getEnergyAdjustmentMultiplier() {
        return energyAdjustmentMultiplier;
    }

    public void setEnergyAdjustmentMultiplier(float energyAdjustmentMultiplier) {
        this.energyAdjustmentMultiplier = energyAdjustmentMultiplier;
    }

    public String getSimulationBeginning() {
        return simulationBeginning;
    }

    public Calendar getSimulationBeginningCalendar() {
        return TimeHelper.parseCalendar(simulationBeginning, Constants.SIMULATION_BEGINNING);
    }

    public void setSimulationBeginning(String simulationBeginning) {
        this.simulationBeginning = simulationBeginning;
    }

    public Pair<Long, String> getDeviceAdjustmentRate() {
        return deviceAdjustmentRate;
    }

    public Duration getDeviceAdjustmentRateDuration() {
        return TimeHelper.parseDuration(deviceAdjustmentRate.getLeft(), deviceAdjustmentRate.getRight());
    }

    public void setDeviceAdjustmentRate(Pair<Long, String> deviceAdjustmentRate) {
        this.deviceAdjustmentRate = deviceAdjustmentRate;
    }

    public Pair<Long, String> getDeviceLifespan() {
        return deviceLifespan;
    }

    public Duration getDeviceLifespanDuration() {
        return TimeHelper.parseDuration(deviceLifespan.getLeft(), deviceLifespan.getRight());
    }

    public void setDeviceLifespan(Pair<Long, String> deviceLifespan) {
        this.deviceLifespan = deviceLifespan;
    }

    public Pair<Long, String> getSimulationStepTime() {
        return simulationStepTime;
    }

    public Duration getSimulationStepTimeDuration() {
        return TimeHelper.parseDuration(simulationStepTime.getLeft(), simulationStepTime.getRight());
    }

    public void setSimulationStepTime(Pair<Long, String> simulationStepTime) {
        this.simulationStepTime = simulationStepTime;
    }

    public InputProfileDetails(float agingCompensationCoefficient,
                               float energyAdjustmentMultiplier,
                               String simulationBeginning,
                               Pair<Long, String> deviceAdjustmentRate,
                               Pair<Long, String> deviceLifespan,
                               Pair<Long, String> simulationStepTime) {
        this.agingCompensationCoefficient = agingCompensationCoefficient;
        this.energyAdjustmentMultiplier = energyAdjustmentMultiplier;
        this.simulationBeginning = simulationBeginning;
        this.deviceAdjustmentRate = deviceAdjustmentRate;
        this.deviceLifespan = deviceLifespan;
        this.simulationStepTime = simulationStepTime;
    }
}
