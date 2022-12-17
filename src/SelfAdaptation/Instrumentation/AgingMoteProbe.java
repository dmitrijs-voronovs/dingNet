package SelfAdaptation.Instrumentation;

import IotDomain.AgingMote;

public class AgingMoteProbe extends MoteProbe {
    public float getAgingFactor(AgingMote mote) {
        return mote.getAgingFactor();
    }

    public float getTotalEnergyConsumed(AgingMote mote) {
        return mote.getTotalEnergyConsumed();
    }

    public float getAge(AgingMote mote) {
        return mote.getAge();
    }
}
