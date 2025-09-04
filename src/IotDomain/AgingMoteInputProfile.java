package IotDomain;

import utils.TimeHelper;

import java.time.Duration;

public class AgingMoteInputProfile {
    private double activityProbability;
    private Pair<Long, String> initialAge;
    private boolean adaptationWasApplied;

    public double getActivityProbability() {
        return activityProbability;
    }

    public void setActivityProbability(double activityProbability) {
        this.activityProbability = activityProbability;
    }

    public Pair<Long, String> getInitialAge() {
        return initialAge;
    }

    public Duration getInitialAgeDuration() {
        return TimeHelper.parseDuration(initialAge.getLeft(), initialAge.getRight());
    }

    public void setInitialAge(Pair<Long, String> initialAge) {
        this.initialAge = initialAge;
    }

    public boolean wasAdaptationApplied() {
        return adaptationWasApplied;
    }

    public void setAdaptationWasApplied(boolean adaptationWasApplied) {
        this.adaptationWasApplied = adaptationWasApplied;
    }

    public AgingMoteInputProfile(double activityProbability, Pair<Long, String> initialAge, boolean adaptationWasApplied) {
        this.activityProbability = activityProbability;
        this.initialAge = initialAge;
        this.adaptationWasApplied = adaptationWasApplied;
    }
}
