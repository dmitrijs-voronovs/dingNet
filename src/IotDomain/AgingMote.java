package IotDomain;

import org.jxmapviewer.viewer.GeoPosition;

import java.util.LinkedList;

public class AgingMote extends Mote {
    /**
     * TODO: decide in minutes / seconds / mseconds / months ??
     * Should be based upon the reference value we choose for time tracking
     */
    protected float age = 0;

    /**
     * Increases with the age
     * value [0..1]
     */
    protected float ageingFactor = 0;

    protected float totalEnergyConsumed = 0;

    public void setTotalEnergyConsumed(float totalEnergyConsumed) {
        this.totalEnergyConsumed = totalEnergyConsumed;
    }

    public float getTotalEnergyConsumed() {
        return totalEnergyConsumed;
    }

    public float getAgeingFactor() {
        return ageingFactor;
    }

    public void setAgeingFactor(float ageingFactor) {
        this.ageingFactor = ageingFactor;
    }

    protected float getAge() {
        return age;
    }

    protected void setAge(float age) {
        this.age = age;
    }

    public AgingMote(Long DevEUI, Integer xPos, Integer yPos, Environment environment, Integer transmissionPower, Integer SF, LinkedList<MoteSensor> moteSensors, Integer energyLevel, LinkedList<GeoPosition> path, Integer samplingRate, Double movementSpeed, Integer startOffset, float age) {
        super(DevEUI, xPos, yPos, environment, transmissionPower, SF, moteSensors, energyLevel, path, samplingRate, movementSpeed, startOffset);
        setAge(age);
    }

    @Override
    public boolean shouldSend() {
        if (Math.random() > this.ageingFactor) return super.shouldSend();
        return false;
    }
}
