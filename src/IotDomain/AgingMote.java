package IotDomain;

import org.jxmapviewer.viewer.GeoPosition;

import java.time.Duration;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

public class AgingMote extends Mote {
    @Override
    public void reset() {
        super.reset();
//        TODO: maybe reset to input config values??
        updateAge();
        setTotalEnergyConsumed(0f);
        this.agingFactorHistory = new Vector<>();
        this.agingFactorHistory.add(new Vector<>());
    }

    @Override
    public void addRun() {
        super.addRun();
        this.agingFactorHistory.add(new Vector<>());
        updateAge();
    }

//    TODO: with input profile values
    protected void updateAge() {
        setAgingFactor(0);
        setAge(Duration.ZERO);
    }

    @Override
    protected void loraSend(LoraWanPacket message) {
        super.loraSend(message);
        if (!this.isTransmitting) {
            updateAgingFactoryHistory();
        }
    }

    /**
     * Data type corresponds with the time delta for each tik of the Simulation
     */
    protected Duration age;

    /**
     * Increases with the age
     * value [0..1]
     */
    protected float agingFactor = 0.0f;

    public Vector<Float> getAgingFactorHistory(int run) {
        return agingFactorHistory.get(run);
    }

    protected void updateAgingFactoryHistory() {
        this.agingFactorHistory.lastElement().add(getAgingFactor());
    }

    protected Vector<Vector<Float>> agingFactorHistory;

    protected float totalEnergyConsumed = 0;

    public void setTotalEnergyConsumed(float totalEnergyConsumed) {
        this.totalEnergyConsumed = totalEnergyConsumed;
    }

    public float getTotalEnergyConsumed() {
        return totalEnergyConsumed;
    }

    public float getAgingFactor() {
        return agingFactor;
    }

    public void setAgingFactor(float agingFactor) {
        this.agingFactor = Math.max(Math.min(agingFactor, 1), 0);
    }

    public void addAgingFactor(float agingFactor) {
        setAgingFactor(this.agingFactor + agingFactor);
    }

    public Duration getAge() {
        return age;
    }

    protected void setAge(Duration age) {
        this.age = age;
    }

    public void increaseAge() {
        setAge(age.plus(Constants.SIMULATION_TIME_STEP));
    }

    public AgingMote(Long DevEUI, Integer xPos, Integer yPos, Environment environment, Integer transmissionPower, Integer SF, LinkedList<MoteSensor> moteSensors, Integer energyLevel, LinkedList<GeoPosition> path, Integer samplingRate, Double movementSpeed, Integer startOffset, Duration initialAge, float initialTotalEnergyConsumed) {
        super(DevEUI, xPos, yPos, environment, transmissionPower, SF, moteSensors, energyLevel, path, samplingRate, movementSpeed, startOffset);
        environment.addMote(this);
        setAge(initialAge);
        setTotalEnergyConsumed(initialTotalEnergyConsumed);
        this.agingFactorHistory = new Vector<>();
        this.agingFactorHistory.add(new Vector<>());
    }

    public AgingMote(Long DevEUI, Integer xPos, Integer yPos, Environment environment, Integer transmissionPower,
                     Integer SF, LinkedList<MoteSensor> moteSensors, Integer energyLevel, LinkedList<GeoPosition> path, Integer samplingRate, Double movementSpeed){
        this(DevEUI,xPos,yPos, environment,transmissionPower,SF,moteSensors,energyLevel,path,samplingRate, movementSpeed, Math.abs((new Random()).nextInt(5)),Duration.ZERO,0);
    }

//    @Override
//    public boolean shouldSend() {
//        if (Math.random() > this.agingFactor) return super.shouldSend();
//        return false;
//    }
}
