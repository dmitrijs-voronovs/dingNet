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
        updateAge();
        this.agingFactorHistory = new Vector<>();
        this.agingFactorHistory.add(new Vector<>());
    }

    @Override
    public void addRun() {
        super.addRun();
        this.agingFactorHistory.add(new Vector<>());
        updateAge();
    }

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

    public void increaseAge(Duration period) {
        setAge(age.plus(period));
    }

    public AgingMote(Long DevEUI, Integer xPos, Integer yPos, Environment environment, Integer transmissionPower, Integer SF, LinkedList<MoteSensor> moteSensors, Integer energyLevel, LinkedList<GeoPosition> path, Integer samplingRate, Double movementSpeed, Integer startOffset, Duration initialAge) {
        super(DevEUI, xPos, yPos, environment, transmissionPower, SF, moteSensors, energyLevel, path, samplingRate, movementSpeed, startOffset);
        environment.addMote(this);
        setAge(initialAge);
        this.agingFactorHistory = new Vector<>();
        this.agingFactorHistory.add(new Vector<>());
    }

    public AgingMote(Long DevEUI, Integer xPos, Integer yPos, Environment environment, Integer transmissionPower,
                     Integer SF, LinkedList<MoteSensor> moteSensors, Integer energyLevel, LinkedList<GeoPosition> path, Integer samplingRate, Double movementSpeed){
        this(DevEUI,xPos,yPos, environment,transmissionPower,SF,moteSensors,energyLevel,path,samplingRate, movementSpeed, Math.abs((new Random()).nextInt(5)),Duration.ZERO);
    }

    public AgingMote(Long DevEUI, Integer xPos, Integer yPos, Environment environment, Integer transmissionPower,
                     Integer SF, LinkedList<MoteSensor> moteSensors, Integer energyLevel, LinkedList<GeoPosition> path, Integer samplingRate, Double movementSpeed, Duration initialAge){
        this(DevEUI,xPos,yPos, environment,transmissionPower,SF,moteSensors,energyLevel,path,samplingRate, movementSpeed, Math.abs((new Random()).nextInt(5)),initialAge);
    }

    @Override
    public boolean shouldSend() {
        if (agingFactorAffectsExecution()) return false;
        return super.shouldSend();
    }

    @Override
    public void setEnergyLevel(Integer energyLevel) {
        if (agingFactorAffectsExecution())
            super.setEnergyLevel((int) (energyLevel * this.agingFactor * (Math.random() - .5)));
        else super.setEnergyLevel(energyLevel);
    }

    @Override
    public Integer getEnergyLevel() {
        if (agingFactorAffectsExecution()) return (int) (super.getEnergyLevel() * this.agingFactor * (Math.random() - .5));
        return super.getEnergyLevel();
    }

    @Override
    public void addTransmissionPower(double transmissionPower) {
        if (agingFactorAffectsExecution())
            super.addTransmissionPower(transmissionPower * agingFactor * (Math.random() - .5));
        else super.addTransmissionPower(transmissionPower);

    }

    @Override
    public void enable(Boolean enabled) {
        if (!agingFactorAffectsExecution()) super.enable(enabled);
    }

    private boolean agingFactorAffectsExecution() {
        return Math.random() < this.agingFactor;
    }
}
