package SelfAdaptation.FeedbackLoop;

import IotDomain.*;
import SelfAdaptation.FeedbackLoop.utils.EnergyAdjustmentCalculator;
import utils.TimeHelper;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;

public class MonthlyAgingHandlingAdaptation extends GenericFeedbackLoop {
    private HashMap<Mote, LocalTime> messageDepartureTimeBuffer = new HashMap<>();
    public Calendar SIMULATION_BEGINNING;
    public Duration DEVICE_ADJUSTMENT_RATE;
    public Duration SIMULATION_STEP_TIME;
    private EnergyAdjustmentCalculator energyAdjustmentCalculator;

    public MonthlyAgingHandlingAdaptation() {
        super("Aging-factor-based");
    }

    @Override
    public void setup(AgingInputProfile agingInputProfile) {
        super.setup(agingInputProfile);
        InputProfileDetails inputProfileDetails = agingInputProfile.getInputProfileDetails();
        this.DEVICE_ADJUSTMENT_RATE = inputProfileDetails.getDeviceAdjustmentRateDuration();
        this.SIMULATION_STEP_TIME = inputProfileDetails.getSimulationStepTimeDuration();
        this.SIMULATION_BEGINNING = inputProfileDetails.getSimulationBeginningCalendar();
        this.energyAdjustmentCalculator = new EnergyAdjustmentCalculator(inputProfileDetails.getEnergyAdjustmentMultiplier());
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
            double energyAdjustment = energyAdjustmentCalculator.calculateEnergyToAdd(SIMULATION_BEGINNING, getCurrentSimulationTime(dataGateway), moteProbe.getAge(mote));
            moteEffector.addPower(mote, energyAdjustment);
            moteEffector.addAgingFactor(mote, dataGateway.getEnvironment().getAgingAdjustmentCalculator().getAgingFactorAdjustment(DEVICE_ADJUSTMENT_RATE));
        }
    }

    private boolean isTimeForAdaptation(LocalTime prevDepartureTime, LoraTransmission lastTransmission) {
        long prevDepartureTimeMs = getAdjustedTimeMs(prevDepartureTime);
        long timeForAdaptation = prevDepartureTimeMs + DEVICE_ADJUSTMENT_RATE.toMillis();
        long lastTransmissionDepartureTimeMs = getAdjustedTimeMs(lastTransmission.getDepartureTime());
        return lastTransmissionDepartureTimeMs >= timeForAdaptation;
    }

    private Duration getCurrentSimulationTime(Gateway dataGateway) {
        return Duration.ofMillis(getAdjustedTimeMs(dataGateway.getEnvironment().getTime()));
    }

    private long getAdjustedTimeMs(LocalTime time) {
        return TimeHelper.localTimeToMillis(time) * SIMULATION_STEP_TIME.toMillis();
    }

    private LoraTransmission getLastTransmission(Gateway dataGateway) {
        int lastRunIdx = dataGateway.getEnvironment().getNumberOfRuns() - 1;
        return dataGateway.getReceivedTransmissions(lastRunIdx).getLast();
    }

}
