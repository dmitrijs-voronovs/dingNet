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
        System.out.println("trying to adapt");
        LoraTransmission lastTransmission = getLastTransmission(dataGateway);
        LocalTime prevDepartureTime = messageDepartureTimeBuffer.get(mote);

        if(prevDepartureTime == null) {
            messageDepartureTimeBuffer.put(mote, lastTransmission.getDepartureTime());
            return;
        }

        if (isTimeForAdaptation(prevDepartureTime, lastTransmission)) {
            messageDepartureTimeBuffer.put(mote, lastTransmission.getDepartureTime());
            System.out.println("adapting mote with id " + mote.getEUI());
            int energyAdjustment = energyAdjustmentCalculator.calculateEnergyToAdd(Constants.SIMULATION_BEGINNING, getCurrentSimulationTime(dataGateway), moteProbe.getAge(mote));
            moteEffector.setPower(mote, moteProbe.getPowerSetting(mote) + energyAdjustment);
            moteEffector.addAgingFactor(mote, dataGateway.getEnvironment().getAgingAdjustmentCalculator().getAgingFactorAdjustment());
        }
    }

    private boolean isTimeForAdaptation(LocalTime prevDepartureTime, LoraTransmission lastTransmission) {
        long prevDepartureTimeMs = TimeHelper.localTimeToMillis(prevDepartureTime) * Constants.SIMULATION_TIME_MEASUREMENT.toMillis();
        long timeForAdaptation = prevDepartureTimeMs + Constants.DEVICE_ADJUSTMENT_RATE.toMillis();
        long lastTransmissionDepartureTimeMs = TimeHelper.localTimeToMillis(lastTransmission.getDepartureTime()) * Constants.SIMULATION_TIME_MEASUREMENT.toMillis();
        return lastTransmissionDepartureTimeMs >= timeForAdaptation;
    }

    private static Duration getCurrentSimulationTime(Gateway dataGateway) {
        return Duration.ofMillis(TimeHelper.localTimeToMillis(dataGateway.getEnvironment().getTime()));
    }

    private static LoraTransmission getLastTransmission(Gateway dataGateway) {
        int lastRunIdx = dataGateway.getEnvironment().getNumberOfRuns() - 1;
        return dataGateway.getReceivedTransmissions(lastRunIdx).getLast();
    }

}
