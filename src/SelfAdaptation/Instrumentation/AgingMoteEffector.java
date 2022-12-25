package SelfAdaptation.Instrumentation;

import IotDomain.AgingMote;

public class AgingMoteEffector extends MoteEffector {
    public void addAgingFactor(AgingMote mote, float agingFactor){
        mote.addAgingFactor(agingFactor);
    }
}
