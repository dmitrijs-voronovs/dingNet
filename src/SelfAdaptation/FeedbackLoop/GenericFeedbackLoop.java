package SelfAdaptation.FeedbackLoop;

import IotDomain.AgingMote;
import IotDomain.Gateway;
import SelfAdaptation.Instrumentation.AgingMoteProbe;
import SelfAdaptation.Instrumentation.MoteEffector;
// 
// 

/**
 * A class representing an adaptation approach for the simulation.
 */
public abstract class GenericFeedbackLoop {

    /**
     * A string representing the name of the approach.
     */
    // 
    private String name;

    /**
     * The mote probe used by the feedback loop.
     */
    protected AgingMoteProbe moteProbe;

    /**
     * The mote effector used by the feedback loop.
     */
    protected MoteEffector moteEffector;

    /**
     * A boolean to know if the feedback loop is active
     */
    private Boolean active;

    /**
     * A method describing what the approach should do when being called on a mote.
     * @param mote The mote to adapt.
     */
    public abstract void adapt(AgingMote mote, Gateway dataGateway);

    /**
     * A method to activate the feedback loop.
     */
    public void start() {
        this.active = true;
    }

    /**
     * A method to deactivate the feedback loop.
     */
    public void stop(){
        this.active = false;
    }

    /**
     * A method to check if the feedback loop is active.
     * @return true if the feedback loop is active.
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * A constructor generating an adaptationApproach with a given name.
     * @param name The name of the approach.
     */
    public GenericFeedbackLoop(String name){
        this.name =name;
        this.active = false;
    }

    /**
     * Returns the name of the approach.
     * @return The name of the approach.
     */
    
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the approach to a given string.
     * @param name The name of the approach.
     */
    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the mote probe used by this feedbackLoop.
     * @return The mote probe used by this feedbackLoop.
     */
    public AgingMoteProbe getMoteProbe() {
        return moteProbe;
    }

    /**
     * Returns the mote effector used by this feedbackLoop.
     * @return The mote effector used by this feedbackLoop.
     */
    public MoteEffector getMoteEffector() {
        return moteEffector;
    }

    /**
     * Sets the mote probe and adds this feedback loop to the mote probe.
     * @param moteProbe the mote probe to set.
     */
    public void setMoteProbe(AgingMoteProbe moteProbe) {
        this.moteProbe = moteProbe;
        getMoteProbe().setGenericFeedbackLoop(this);
    }

    /**
     * Sets the effector to the given effector.
     * @param moteEffector The effector to set.
     */
    public void setMoteEffector(MoteEffector moteEffector) {
        this.moteEffector = moteEffector;
    }
}

