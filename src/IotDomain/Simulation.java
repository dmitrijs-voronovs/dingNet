package IotDomain;

import GUI.MainGUI;
import SelfAdaptation.FeedbackLoop.GenericFeedbackLoop;
import SelfAdaptation.FeedbackLoop.utils.AgingAdjustmentCalculator;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

/**
 * A class representing a simulation.
 */
public class Simulation implements Runnable {
    /**
     * The InputProfile used in the simulation.
     */
    private AgingInputProfile inputProfile;
    /**
     * The Environment used in th simulation.
     */
    private Environment environment;
    /**
     * The GenericFeedbackLoop used in the simulation.
     */
    private GenericFeedbackLoop approach;
    /**
     * The GUI on which the simulation is running.
     */
    private MainGUI gui;

    /**
     * Constructs a simulation  with a given InputProfile, Environment, GenericFeedbackLoop and GUI.
     * @param inputProfile The InputProfile to use.
     * @param environment The Environment to use.
     * @param approach The GenericFeedbackLoop to use.
     * @param gui The MainGUI to use.
     */
    public Simulation(AgingInputProfile inputProfile, Environment environment, GenericFeedbackLoop approach, MainGUI gui){
        this.environment = environment;
        this.inputProfile = inputProfile;
        this.approach = approach;
        this.gui = gui;
    }

    public Simulation(MainGUI gui){
        this.gui = gui;
    }

    /**
     * Gets the Environment used in th simulation.
     * @return The Environment used in the simulation.
     */
    
    public Environment getEnvironment() {
        return environment;
    }
    /**
     * Sets the Environment used in th simulation.
     * @param environment  The Environment to use in the simulation.
     */
    
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    /**
     * Gets the InputProfile used in th simulation.
     * @return The InputProfile used in the simulation.
     */
    
    public AgingInputProfile getInputProfile() {
        return inputProfile;
    }
    /**
     * Sets the InputProfile used in th simulation.
     * @param inputProfile  The InputProfile to use in the simulation.
     */
    
    public void setInputProfile(AgingInputProfile inputProfile) {
        this.inputProfile = inputProfile;
    }
    /**
     * Sets the GenericFeedbackLoop used in th simulation.
     * @param approach  The GenericFeedbackLoop to use in the simulation.
     */
    
    public void setAdaptationAlgorithm(GenericFeedbackLoop approach){
        this.approach = approach;
    }
    /**
     * Gets the GenericFeedbackLoop used in th simulation.
     * @return The GenericFeedbackLoop used in the simulation.
     */
    
    public GenericFeedbackLoop getAdaptationAlgorithm(){
        return approach;
    }

    /**
     * A method for running a single run with visualisation.
     * @param speed
     */
    public void singleRun(Integer speed) {
        prepareSimulation();
        setMoteInitialSettings();

        boolean arrived = true;
        HashMap<AgingMote,Integer> waypoinMap = new HashMap<>();
        HashMap<AgingMote,LocalTime> timemap = new HashMap<>();
        HashMap<AgingMote,Pair<Integer,Integer>> locationmap = new HashMap<>();
        HashMap<AgingMote,LinkedList<Pair<Integer,Integer>>> locationhistorymap = new HashMap<>();
        HashMap<AgingMote, HashMap<Pair<Integer, Integer>, Float>> agingHistory = new HashMap<>();

        for(AgingMote mote : getEnvironment().getMotes()){
            timemap.put(mote, getEnvironment().getTime());

            Pair<Integer, Integer> currentLocation = new Pair<>(mote.getXPos(),mote.getYPos());
            locationmap.put(mote,currentLocation);

            locationhistorymap.put(mote, new LinkedList<>());
            LinkedList historyMap = locationhistorymap.get(mote);
            historyMap.add(new Pair<>(mote.getXPos(),mote.getYPos()));
            locationhistorymap.put(mote,historyMap);

            agingHistory.put(mote, new HashMap<>());
            HashMap<Pair<Integer, Integer>, Float> agingAtLocations = agingHistory.get(mote);
            agingAtLocations.put(currentLocation, mote.getAgingFactor());
            agingHistory.put(mote, agingAtLocations);

            if(mote.getPath().size() != 0) {
                if (Integer.signum(mote.getXPos()- getEnvironment().toMapXCoordinate(mote.getPath().getLast())) != 0 ||
                        Integer.signum(mote.getYPos()- getEnvironment().toMapYCoordinate(mote.getPath().getLast())) != 0) {
                    arrived = false;
                }
            }
            waypoinMap.put(mote,0);
        }

        while (!arrived) {

            for(AgingMote mote : getEnvironment().getMotes()){
                if(mote.isEnabled()) {
                    if (Integer.signum(mote.getPath().size() - waypoinMap.get(mote)) > 0) {

                        if (isMoteInSimulation(timemap,mote)) {
                            timemap.put(mote, getEnvironment().getTime());
                            if (Integer.signum(mote.getXPos() - getEnvironment().toMapXCoordinate(mote.getPath().get(waypoinMap.get(mote)))) != 0 ||
                                    Integer.signum(mote.getYPos() - getEnvironment().toMapYCoordinate(mote.getPath().get(waypoinMap.get(mote)))) != 0) {

                                getEnvironment().moveMote(mote, mote.getPath().get(waypoinMap.get(mote)));
                                LinkedList historymap = locationhistorymap.get(mote);
                                historymap.add(new Pair<>(mote.getXPos(), mote.getYPos()));
                                locationhistorymap.put(mote, historymap);

                                Pair<Integer, Integer> currentLocation = new Pair<>(mote.getXPos(),mote.getYPos());
                                HashMap<Pair<Integer, Integer>, Float> agingAtLocations = agingHistory.get(mote);
                                agingAtLocations.put(currentLocation, mote.getAgingFactor());
                                agingHistory.put(mote, agingAtLocations);

                                sendDataToGateway(mote);
                            } else waypoinMap.put(mote, waypoinMap.get(mote) + 1);
                        }
                    }
                }

            }

            arrived = true;
            for(Mote mote : environment.getMotes()){
                if(mote.isEnabled()) {
                    if (mote.getPath().size() != 0) {
                        if (Integer.signum(mote.getXPos() - environment.toMapXCoordinate(mote.getPath().getLast())) != 0 ||
                                Integer.signum(mote.getYPos() - environment.toMapYCoordinate(mote.getPath().getLast())) != 0) {
                            arrived = false;
                        }
                    }
                }
            }
            envTick();

        }

        for(Mote mote : environment.getMotes()){
            Pair<Integer,Integer> location = locationmap.get(mote);
            mote.setXPos(location.getLeft());
            mote.setYPos(location.getRight());
        }

        Timer timer = new Timer();
        AnimationTimerTask animationTimerTask = new AnimationTimerTask(locationhistorymap, agingHistory);
        timer.schedule(animationTimerTask,0,75/(1*speed));
        for(Mote mote : environment.getMotes()){
            Pair<Integer,Integer> location = locationmap.get(mote);
            mote.setXPos(location.getLeft());
            mote.setYPos(location.getRight());
        }
    }

    private void prepareSimulation() {
        // reset the environment.
        getEnvironment().reset();

        InputProfileDetails inputProfileDetails = inputProfile.getInputProfileDetails();
        getEnvironment().setAgingAdjustmentCalculator(new AgingAdjustmentCalculator(inputProfileDetails.getDeviceLifespanDuration(),
                inputProfileDetails.getSimulationStepTimeDuration(),
                inputProfileDetails.getAgingCompensationCoefficient()));

        getApproach().setup(inputProfile);

        //Check if a mote can participate in this run.
        enableMotes();
    }

    private void envTick() {
        environment.tick(1);
        increaseMotesAge();
    }

    private void increaseMotesAge() {
        getActiveMotesStream().forEach(m -> m.increaseAge(inputProfile.getInputProfileDetails().getSimulationStepTimeDuration()));
        getActiveMotesStream().forEach(m -> m.addAgingFactor(getEnvironment().getAgingAdjustmentCalculator().getAgingFactorUnit()));
    }

    private Stream<AgingMote> getActiveMotesStream() {
        return environment.getMotes().stream().filter(Mote::isEnabled);
    }

    private void sendDataToGateway(Mote mote) {
        if (mote.shouldSend()) {
            LinkedList<Byte> data = new LinkedList<>();
            for (MoteSensor sensor : mote.getSensors()) {
                data.add(sensor.getValue(mote.getXPos(), mote.getYPos(), getEnvironment().getTime()));
            }
            Byte[] dataByte = new Byte[data.toArray().length];
            data.toArray(dataByte);
            mote.sendToGateWay(dataByte, new HashMap<>());
        }
    }

    private void enableMotes() {
        for(Mote mote: getEnvironment().getMotes()){
            Double activityProbability;
            if(getInputProfile().getProbabilitiesForMotesKeys().contains(getEnvironment().getMotes().indexOf(mote)))
                activityProbability = getInputProfile().getProbabilityForMote(getEnvironment().getMotes().indexOf(mote));
            else
                activityProbability = 1.0;
            mote.enable(Math.random() >= 1.0 - activityProbability);
        }
    }

    private void setMoteInitialSettings() {
        getActiveMotesStream().forEach(m -> {
            AgingMoteInputProfile moteInputProfile = inputProfile.getMoteInputProfile(getEnvironment().getMotes().indexOf(m));
            m.setAge(moteInputProfile.getInitialAgeDuration());
            m.setAgingFactor(getEnvironment().getAgingAdjustmentCalculator().getAgingFactorAfterAdjustments(m.getAge(), moteInputProfile.wasAdaptationApplied()));
        });
    }

    /**
     * A method for running the simulation as described in the inputProfile.
     */
    public void run(){
        prepareSimulation();

        for(int i =0; i< getInputProfile().getNumberOfRuns();i++) {
            gui.setProgress(i,getInputProfile().getNumberOfRuns());
            if(i != 0) {
                getEnvironment().addRun();
            }
            setMoteInitialSettings();

            Boolean arrived = true;
            HashMap<AgingMote, Integer> waypoinMap = new HashMap<>();
            HashMap<AgingMote, LocalTime> timemap = new HashMap<>();
            HashMap<AgingMote, Pair<Integer, Integer>> locationmap = new HashMap<>();
            for (AgingMote mote : getEnvironment().getMotes()) {
                timemap.put(mote, getEnvironment().getTime());
                locationmap.put(mote, new Pair<>(mote.getXPos(), mote.getYPos()));
                if (mote.getPath().size() != 0) {
                    if (Integer.signum(mote.getXPos() - getEnvironment().toMapXCoordinate(mote.getPath().getLast())) != 0 ||
                            Integer.signum(mote.getYPos() - getEnvironment().toMapYCoordinate(mote.getPath().getLast())) != 0) {
                        arrived = arrived && false;
                    }
                }
                waypoinMap.put(mote, 0);
            }

            while (!arrived) {

                for (AgingMote mote : getEnvironment().getMotes()) {
                    if(mote.isEnabled()) {
                        if (Integer.signum(mote.getPath().size() - waypoinMap.get(mote)) > 0) {

                            if (isMoteInSimulation(timemap, mote)) {
                                timemap.put(mote, getEnvironment().getTime());
                                if (Integer.signum(mote.getXPos() - getEnvironment().toMapXCoordinate(mote.getPath().get(waypoinMap.get(mote)))) != 0 ||
                                        Integer.signum(mote.getYPos() - getEnvironment().toMapYCoordinate(mote.getPath().get(waypoinMap.get(mote)))) != 0) {
                                    getEnvironment().moveMote(mote, mote.getPath().get(waypoinMap.get(mote)));
                                    sendDataToGateway(mote);
                                } else waypoinMap.put(mote, waypoinMap.get(mote) + 1);
                            }
                        }
                    }

                }

                arrived = true;
                for (Mote mote : environment.getMotes()) {
                    if(mote.isEnabled()) {
                        if (mote.getPath().getLast() != null) {
                            if (Integer.signum(mote.getXPos() - environment.toMapXCoordinate(mote.getPath().getLast())) != 0 ||
                                    Integer.signum(mote.getYPos() - environment.toMapYCoordinate(mote.getPath().getLast())) != 0) {
                                arrived = arrived && false;
                            }
                        }
                    }
                }
                envTick();
            }

            gui.setProgress(getInputProfile().getNumberOfRuns(),getInputProfile().getNumberOfRuns());
            for (Mote mote : environment.getMotes()) {
                Pair<Integer, Integer> location = locationmap.get(mote);
                mote.setXPos(location.getLeft());
                mote.setYPos(location.getRight());
            }
        }

    }

    private boolean isMoteInSimulation(HashMap<AgingMote, LocalTime> timemap, AgingMote mote) {
        long envTime = getEnvironment().getTime().toNanoOfDay();
        long moteTime = timemap.get(mote).toNanoOfDay();
        return 1 / mote.getMovementSpeed() * 1000 < (envTime - moteTime) / 100000 &&
                Long.signum(envTime / 100000 - Math.abs(mote.getStartOffset()) * 100000) > 0;
    }

    public GenericFeedbackLoop getApproach() {
        return approach;
    }

    /**
     * Sets the GenericFeedbackLoop.
     * @param approach The GenericFeedbackLoop to set.
     */
    
    public void setApproach(GenericFeedbackLoop approach) {
        if(getApproach()!= null) {
            getApproach().stop();
        }
        this.approach = approach;
        getApproach().start();
    }

    /**
     * An animation task needed for the visualisation.
     */
    private class AnimationTimerTask extends TimerTask {

        HashMap<Mote,Integer> timeMap = new HashMap<>();
        Boolean arrived = false;
        HashMap<Mote,Integer> waypointMap = new HashMap<>();
        HashMap<AgingMote,LinkedList<Pair<Integer,Integer>>> locationHistoryMap;
        HashMap<AgingMote, HashMap<Pair<Integer, Integer>, Float>> agingHistory;

        int i;
        public AnimationTimerTask(HashMap<AgingMote,LinkedList<Pair<Integer,Integer>>> locationHistoryMap,
                                  HashMap<AgingMote, HashMap<Pair<Integer, Integer>, Float>> agingHistory){
            i =0;
            for (Mote mote: environment.getMotes()){
                timeMap.put(mote,i);
                waypointMap.put(mote,0);
            }
            this.locationHistoryMap = locationHistoryMap;
            this.agingHistory = agingHistory;
        }

        @Override
        public void run() {
            Boolean moved = false;
            arrived = true;
            for (AgingMote mote : getEnvironment().getMotes()){
                if(waypointMap.get(mote) < locationHistoryMap.get(mote).size()) {
                    arrived  = false;
                    if(i-timeMap.get(mote)> 1 / mote.getMovementSpeed() *100){
                        timeMap.put(mote, i);
                        Integer moteXPos = locationHistoryMap.get(mote).get(waypointMap.get(mote)).getLeft();
                        Integer moteYPos = locationHistoryMap.get(mote).get(waypointMap.get(mote)).getRight();
                        mote.setXPos(moteXPos);
                        mote.setYPos(moteYPos);

                        Float agingFactorAtLocation = agingHistory.get(mote).get(new Pair<>(moteXPos, moteYPos));
                        mote.setAgingFactor(agingFactorAtLocation);
                        moved = true;
                        waypointMap.put(mote,waypointMap.get(mote)+25);
                    }
                }
            }
            if(arrived){
                for(Mote mote : environment.getMotes()){
                    Pair<Integer,Integer> location = locationHistoryMap.get(mote).getFirst();
                    mote.setXPos(location.getLeft());
                    mote.setYPos(location.getRight());
                }
                gui.refreshMap();

                cancel();
            }
            if(moved) {
                gui.refreshMap();
            }
            i = i+50;

        }
    }
}
