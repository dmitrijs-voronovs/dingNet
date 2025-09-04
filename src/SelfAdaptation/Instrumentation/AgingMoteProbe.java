package SelfAdaptation.Instrumentation;

import IotDomain.AgingMote;

import java.time.Duration;

public class AgingMoteProbe extends MoteProbe {
    public Duration getAge(AgingMote mote) {
        return mote.getAge();
    }
}
