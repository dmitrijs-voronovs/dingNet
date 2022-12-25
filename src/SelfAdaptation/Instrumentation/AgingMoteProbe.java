package SelfAdaptation.Instrumentation;

import IotDomain.AgingMote;

import java.time.Duration;

public class AgingMoteProbe extends MoteProbe {
    public float getAgingFactor(AgingMote mote) {
        return mote.getAgingFactor();
    }

    public float getTotalEnergyConsumed(AgingMote mote) {
        return mote.getTotalEnergyConsumed();
    }

    public Duration getAge(AgingMote mote) {
        return mote.getAge();
    }
}
