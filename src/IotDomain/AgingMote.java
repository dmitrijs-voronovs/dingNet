package IotDomain;

import org.jxmapviewer.viewer.GeoPosition;

import java.util.LinkedList;
import java.util.Random;

public class AgingMote extends Mote {
    /**
     * Data type corresponds with the time delta for each tik of the Simulation
     */
    protected long age = 0;

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

    public float getAgingFactor() {
        return ageingFactor;
    }

    public void setAgeingFactor(float ageingFactor) {
        this.ageingFactor = ageingFactor;
    }

    public long getAge() {
        return age;
    }

    protected void setAge(long age) {
        this.age = age;
    }

    public void increaseAgeBy(long ageDelta) { this.age += ageDelta; }

    public AgingMote(Long DevEUI, Integer xPos, Integer yPos, Environment environment, Integer transmissionPower, Integer SF, LinkedList<MoteSensor> moteSensors, Integer energyLevel, LinkedList<GeoPosition> path, Integer samplingRate, Double movementSpeed, Integer startOffset, long initialAge, float initalTotalEnergyConsumed) {
        super(DevEUI, xPos, yPos, environment, transmissionPower, SF, moteSensors, energyLevel, path, samplingRate, movementSpeed, startOffset);
        environment.addMote(this);
        setAge(initialAge);
        setTotalEnergyConsumed(initalTotalEnergyConsumed);
    }

    public AgingMote(Long DevEUI, Integer xPos, Integer yPos, Environment environment, Integer transmissionPower,
                Integer SF, LinkedList<MoteSensor> moteSensors, Integer energyLevel, LinkedList<GeoPosition> path, Integer samplingRate, Double movementSpeed){
        this(DevEUI,xPos,yPos, environment,transmissionPower,SF,moteSensors,energyLevel,path,samplingRate, movementSpeed, Math.abs((new Random()).nextInt(5)),0,0);
    }

    @Override
    public boolean shouldSend() {
        if (Math.random() > this.ageingFactor) return super.shouldSend();
        return false;
    }
}
