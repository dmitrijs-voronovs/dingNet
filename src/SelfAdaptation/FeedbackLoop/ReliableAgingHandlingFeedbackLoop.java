package SelfAdaptation.FeedbackLoop;

import IotDomain.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;

// TODO: remake according to the aging strategy - i.e. increase every Nth frame power for every old mote
//  currently it is a blind copy of the ReliableEfficientDistanceGateway
public class ReliableAgingHandlingFeedbackLoop extends GenericFeedbackLoop {
    private final HashMap<Mote, LocalTime> messageDepartureTimeBuffer = new HashMap<>();
    private final AgingCalculator agingCalculator = new AgingCalculator();
    public ReliableAgingHandlingFeedbackLoop(){
        super("Aging-factor-based");
    }

    @Override
    public void adapt(AgingMote mote, Gateway dataGateway){
        LoraTransmission lastTransmission = getLastTransmission(dataGateway);
        LocalTime previousDepartureTime = messageDepartureTimeBuffer.get(mote);

        if(previousDepartureTime == null) {
            messageDepartureTimeBuffer.put(mote, lastTransmission.getDepartureTime());
            return;
        }

        long NANOS_IN_MILLI = Duration.ofMillis(1).toNanos();
        long prevInMillisOriginal = previousDepartureTime.toNanoOfDay() / NANOS_IN_MILLI;
        long prevInMillisInCorrectMetric = prevInMillisOriginal * Constants.SIMULATION_TIME_MEASUREMENT.toMillis();
        long timeForAdaptation = prevInMillisInCorrectMetric + Constants.DEVICE_ADJUSTMENT_RATE.toMillis();
        long lastTimeInMillis = lastTransmission.getDepartureTime().toNanoOfDay() / NANOS_IN_MILLI * Constants.SIMULATION_TIME_MEASUREMENT.toMillis();
        if (lastTimeInMillis >= timeForAdaptation) {
            messageDepartureTimeBuffer.put(mote, lastTransmission.getDepartureTime());
            System.out.println("adapting mote with id " + mote.getEUI());
            int energyAdjustment = agingCalculator.calculateEnergyToAdd(Constants.SIMULATION_BEGINNING, (int) moteProbe.getAge(mote));
            moteEffector.setPower(mote, moteProbe.getPowerSetting(mote) + energyAdjustment);
        }
    }

    private static LoraTransmission getLastTransmission(Gateway dataGateway) {
        int lastRunIdx = dataGateway.getEnvironment().getNumberOfRuns() - 1;
        return dataGateway.getReceivedTransmissions(lastRunIdx).getLast();
    }

}
