package iot.networkentity;

import iot.Environment;
import util.Path;

import java.util.List;

//TODO: implement
public class AgeingMote extends Mote {
    private Integer age;

    public AgeingMote(long DevEUI, double xPos, double yPos, int transmissionPower, int SF, List<MoteSensor> moteSensors, int energyLevel, Path path, double movementSpeed, int startMovementOffset, int periodSendingPacket, int startSendingOffset, Environment environment, Integer age) {
        super(DevEUI, xPos, yPos, transmissionPower, SF, moteSensors, energyLevel, path, movementSpeed, startMovementOffset, periodSendingPacket, startSendingOffset, environment);
        this.age = age;
    }
}
