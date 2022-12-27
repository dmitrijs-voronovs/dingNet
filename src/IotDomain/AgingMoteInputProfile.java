package IotDomain;

import java.time.Duration;

public class AgingMoteInputProfile {
    private double activityProbability;
    private Duration initialAge;
    private boolean adaptationWasApplied;

    public double getActivityProbability() {
        return activityProbability;
    }

    public void setActivityProbability(double activityProbability) {
        this.activityProbability = activityProbability;
    }

    public Duration getInitialAge() {
        return initialAge;
    }

    public void setInitialAge(Duration initialAge) {
        this.initialAge = initialAge;
    }

    public boolean isAdaptationWasApplied() {
        return adaptationWasApplied;
    }

    public void setAdaptationWasApplied(boolean adaptationWasApplied) {
        this.adaptationWasApplied = adaptationWasApplied;
    }

    public AgingMoteInputProfile(double activityProbability, Duration initialAge, boolean adaptationWasApplied) {
        this.activityProbability = activityProbability;
        this.initialAge = initialAge;
        this.adaptationWasApplied = adaptationWasApplied;
    }
}
