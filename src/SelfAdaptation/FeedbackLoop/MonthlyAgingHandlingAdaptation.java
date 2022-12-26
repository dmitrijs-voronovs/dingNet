package SelfAdaptation.FeedbackLoop;

import IotDomain.*;
import SelfAdaptation.FeedbackLoop.utils.EnergyAdjustmentCalculator;
import utils.TimeHelper;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;

public class MonthlyAgingHandlingAdaptation extends GenericFeedbackLoop {
    private final HashMap<Mote, LocalTime> messageDepartureTimeBuffer = new HashMap<>();
    private final EnergyAdjustmentCalculator energyAdjustmentCalculator = new EnergyAdjustmentCalculator(Constants.AGING_ADJUSTMENT_MULTIPLIER);
    public MonthlyAgingHandlingAdaptation(){
        super("Aging-factor-based");
    }

    @Override
    public void adapt(AgingMote mote, Gateway dataGateway){
        LoraTransmission lastTransmission = getLastTransmission(dataGateway);
        LocalTime prevDepartureTime = messageDepartureTimeBuffer.get(mote);

        if(prevDepartureTime == null) {
            messageDepartureTimeBuffer.put(mote, lastTransmission.getDepartureTime());
            return;
        }

        if (isTimeForAdaptation(prevDepartureTime, lastTransmission)) {
            messageDepartureTimeBuffer.put(mote, lastTransmission.getDepartureTime());
            int energyAdjustment = energyAdjustmentCalculator.calculateEnergyToAdd(Constants.SIMULATION_BEGINNING, getCurrentSimulationTime(dataGateway), moteProbe.getAge(mote));
            moteEffector.setPower(mote, moteProbe.getPowerSetting(mote) + energyAdjustment);
            moteEffector.addAgingFactor(mote, dataGateway.getEnvironment().getAgingAdjustmentCalculator().getAgingFactorAdjustment());
        }
    }

    private boolean isTimeForAdaptation(LocalTime prevDepartureTime, LoraTransmission lastTransmission) {
        long prevDepartureTimeMs = getAdjustedTimeMs(prevDepartureTime);
        long timeForAdaptation = prevDepartureTimeMs + Constants.DEVICE_ADJUSTMENT_RATE.toMillis();
        long lastTransmissionDepartureTimeMs = getAdjustedTimeMs(lastTransmission.getDepartureTime());
        return lastTransmissionDepartureTimeMs >= timeForAdaptation;
    }

    private static Duration getCurrentSimulationTime(Gateway dataGateway) {
        return Duration.ofMillis(getAdjustedTimeMs(dataGateway.getEnvironment().getTime()));
    }

    private static long getAdjustedTimeMs(LocalTime time) {
        return TimeHelper.localTimeToMillis(time) * Constants.SIMULATION_TIME_STEP.toMillis();
    }

    private static LoraTransmission getLastTransmission(Gateway dataGateway) {
        int lastRunIdx = dataGateway.getEnvironment().getNumberOfRuns() - 1;
        return dataGateway.getReceivedTransmissions(lastRunIdx).getLast();
    }

}
