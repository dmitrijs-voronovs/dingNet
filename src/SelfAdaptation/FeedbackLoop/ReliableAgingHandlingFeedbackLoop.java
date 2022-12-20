package SelfAdaptation.FeedbackLoop;

import IotDomain.*;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

// TODO: remake according to the aging strategy - i.e. increase every Nth frame power for every old mote
//  currently it is a blind copy of the ReliableEfficientDistanceGateway
public class ReliableAgingHandlingFeedbackLoop extends GenericFeedbackLoop {
    private final HashMap<Mote, LocalTime> messageDepartureTimeBuffer = new HashMap<>();
    private final AgingCalculator agingCalculator = new AgingCalculator();
//    private HashMap<AgingMote,>
    /**
     * Constructs a distance based approach.
     */
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

//        Instant timeForAd = Instant.from(previousDepartureTime).plusSeconds(Constants.DEVICE_ADJUSTMENT_RATE.get(ChronoUnit.SECONDS));

//        LocalTime timeForAdaptation = previousDepartureTime.plus(Constants.DEVICE_ADJUSTMENT_RATE);
        LocalTime timeForAdaptation = previousDepartureTime.plus(Constants.DEVICE_ADJUSTMENT_RATE.getDays() * 24, ChronoUnit.HOURS);
        if (lastTransmission.getDepartureTime().isAfter(timeForAdaptation)) {
            messageDepartureTimeBuffer.put(mote, lastTransmission.getDepartureTime());
//            TODO: fix mote age to be int
            System.out.println("adapting mote with id " + mote.getEUI());
            moteEffector.setPower(mote, moteProbe.getPowerSetting(mote) + agingCalculator.calculateEnergyToAdd(Constants.SIMULATION_BEGINNING , (int) moteProbe.getAge(mote)));
//            moteEffector.
        }
    }

    private static LoraTransmission getLastTransmission(Gateway dataGateway) {
        int lastRunIdx = dataGateway.getEnvironment().getNumberOfRuns() - 1;
        return dataGateway.getReceivedTransmissions(lastRunIdx).getLast();
    }

}
