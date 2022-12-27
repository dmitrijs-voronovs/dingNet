package IotDomain;

import java.time.Duration;
import java.util.Calendar;

public class InputProfileDetails {
    private float agingCompensationCoefficient;
    private float energyAdjustmentMultiplier;
    private Calendar simulationBeginning;
    private Duration deviceAdjustmentRate;
    private Duration deviceLifespan;
    private Duration simulationStepTime;

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

    public Calendar getSimulationBeginning() {
        return simulationBeginning;
    }

    public void setSimulationBeginning(Calendar simulationBeginning) {
        this.simulationBeginning = simulationBeginning;
    }

    public Duration getDeviceAdjustmentRate() {
        return deviceAdjustmentRate;
    }

    public void setDeviceAdjustmentRate(Duration deviceAdjustmentRate) {
        this.deviceAdjustmentRate = deviceAdjustmentRate;
    }

    public Duration getDeviceLifespan() {
        return deviceLifespan;
    }

    public void setDeviceLifespan(Duration deviceLifespan) {
        this.deviceLifespan = deviceLifespan;
    }

    public Duration getSimulationStepTime() {
        return simulationStepTime;
    }

    public void setSimulationStepTime(Duration simulationStepTime) {
        this.simulationStepTime = simulationStepTime;
    }

    public InputProfileDetails(float agingCompensationCoefficient,
                               float energyAdjustmentMultiplier,
                               Calendar simulationBeginning,
                               Duration deviceAdjustmentRate,
                               Duration deviceLifespan,
                               Duration simulationStepTime) {
        this.agingCompensationCoefficient = agingCompensationCoefficient;
        this.energyAdjustmentMultiplier = energyAdjustmentMultiplier;
        this.simulationBeginning = simulationBeginning;
        this.deviceAdjustmentRate = deviceAdjustmentRate;
        this.deviceLifespan = deviceLifespan;
        this.simulationStepTime = simulationStepTime;
    }
}
