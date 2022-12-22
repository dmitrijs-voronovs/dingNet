package IotDomain;

import org.jxmapviewer.viewer.GeoPosition;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Vector;

public class AgingMote extends Mote {
    @Override
    public void reset() {
        super.reset();
//        TODO: maybe reset to input config values??
        setAge(0);
        setAgingFactor(0f);
        setTotalEnergyConsumed(0f);
        this.agingFactorHistory = new Vector<>();
        this.agingFactorHistory.add(new Vector<>());
    }

    @Override
    public void addRun() {
        super.addRun();
        this.agingFactorHistory.add(new Vector<>());
    }

    @Override
    protected void loraSend(LoraWanPacket message) {
        super.loraSend(message);
        System.out.println("loraSend");
        if (!this.isTransmitting) {
            System.out.println("updating history");
            updateAgingFactoryHistory();
        }
    }

    /**
     * Data type corresponds with the time delta for each tik of the Simulation
     */
    protected long age = 0;

    /**
     * Increases with the age
     * value [0..1]
     */
    protected float agingFactor = 0.0f;

    public Vector<Float> getAgingFactorHistory(int run) {
        return agingFactorHistory.get(run);
    }

    protected void updateAgingFactoryHistory() {
        try {
            this.agingFactorHistory.lastElement().add(getAgingFactor());
        } catch (NoSuchElementException e) {
            this.agingFactorHistory.lastElement().add(getAgingFactor());
        }
    }

    protected Vector<Vector<Float>> agingFactorHistory = new Vector<>(new Vector<>());

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

    protected void setAgingFactor(float agingFactor) {
        this.agingFactor = agingFactor;
    }

    public long getAge() {
        return age;
    }

    protected void setAge(long age) {
        this.age = age;
    }

    public void increaseAgeBy(long ageDelta) {
        System.out.println("adding age to "+ this.getEUI() + " by " + ageDelta);
        setAge(getAge() + ageDelta);
        setAgingFactor(getAgingFactor() + ageDelta * .1f);
    }

    public AgingMote(Long DevEUI, Integer xPos, Integer yPos, Environment environment, Integer transmissionPower, Integer SF, LinkedList<MoteSensor> moteSensors, Integer energyLevel, LinkedList<GeoPosition> path, Integer samplingRate, Double movementSpeed, Integer startOffset, long initialAge, float initialTotalEnergyConsumed) {
        super(DevEUI, xPos, yPos, environment, transmissionPower, SF, moteSensors, energyLevel, path, samplingRate, movementSpeed, startOffset);
        environment.addMote(this);
        setAge(initialAge);
        setTotalEnergyConsumed(initialTotalEnergyConsumed);
        this.agingFactorHistory = new Vector<>();
        this.agingFactorHistory.add(new Vector<>());
    }

    public AgingMote(Long DevEUI, Integer xPos, Integer yPos, Environment environment, Integer transmissionPower,
                Integer SF, LinkedList<MoteSensor> moteSensors, Integer energyLevel, LinkedList<GeoPosition> path, Integer samplingRate, Double movementSpeed){
        this(DevEUI,xPos,yPos, environment,transmissionPower,SF,moteSensors,energyLevel,path,samplingRate, movementSpeed, Math.abs((new Random()).nextInt(5)),0,0);
    }

//    @Override
//    public boolean shouldSend() {
//        if (Math.random() > this.agingFactor) return super.shouldSend();
//        return false;
//    }
}
