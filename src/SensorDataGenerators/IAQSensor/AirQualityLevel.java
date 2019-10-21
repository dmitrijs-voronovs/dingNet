package SensorDataGenerators.IAQSensor;

public enum AirQualityLevel {

    GOOD("Good", 1),
    FAIR("Fair", 2),
    MODERATE("Moderate", 3),
    POOR("Poor", 4),
    VERY_POOR("Very poor", 5);

    private final String level;
    private final byte cod;

    AirQualityLevel(String level, int cod) {
        this.level = level;
        this.cod = (byte) cod;
    }

    public String getLevel() {
        return level;
    }

    public byte getCod() {
        return cod;
    }
}
