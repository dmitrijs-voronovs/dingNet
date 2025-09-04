# Project: Adaptive IoT Network Simulator Extension

This project was developed as a university assignment for the "Fundamentals of Adaptive Software" course. It extends **DingNet** (version `1.2.1.`), an open-source IoT network simulator, to create a self-adaptive system that intelligently manages the power consumption of IoT devices (motes) in response to device aging. The primary goal is to enhance the reliability of data transmission by dynamically adjusting mote settings based on real-time network conditions.

A comprehensive report detailing the project's analysis, design, and implementation can be found [here](report/DingNet3-Implementation_and_demo.pdf).

## Architectural Decisions & Key Changes

The core of this extension is a feedback loop that monitors network performance and adjusts mote parameters to maintain a desired Quality of Service (QoS). The following are the key architectural decisions and technical changes implemented:

1.  **Refactoring for Geo-awareness and Real-world Simulation:**
    * **From Grid-based to Geo-positional Coordinates:** A fundamental architectural change was the transition from an integer-based grid system to using `GeoPosition` objects for all location-aware calculations. This was a critical step to enable more realistic simulations that can be mapped to real-world geographical data. All the classes that were handling positions were refactored, including `Mote`, `Gateway`, and `Environment`, to rely on latitude and longitude.
    * **Dynamic Data Generation:** The `SensorDataGenerator` interface was refactored to take the `Environment` and a `GeoPosition` as input, allowing sensor data to be generated based on the mote's actual geographical location. This makes the simulation more dynamic and realistic, as sensor readings can now be influenced by the mote's position on the map.

2.  **Enhanced Simulation Clock and Event Handling:**
    * **High-Precision `LocalDateTime` Clock:** The global simulation clock was upgraded from `LocalTime` to `LocalDateTime`. This was a crucial change to support multi-day simulations and to handle time-based events with greater precision, which is essential for simulating long-term phenomena like device aging.
    * **Asynchronous and Thread-Safe Event Triggers:** The `GlobalClock`'s trigger mechanism was re-implemented to be thread-safe using `synchronized` blocks. This ensures that in multi-threaded simulation scenarios, events are handled reliably without race conditions.

3.  **Introduction of `LifeLongMote` for Adaptive Behavior:**
    * **New Mote Specialization:** A new `LifeLongMote` class was introduced, extending the base `Mote` class. This new entity is designed to be "LLSA compliant" (Life-Long Self-Adaptive) and encapsulates the logic for adaptive behavior.
    * **Packet Buffering and Expiration:** The `LifeLongMote` includes an `ExpiringBuffer` for outgoing packets. This allows the mote to store data and transmit it at optimal times, while also ensuring that outdated data is discarded, which is critical for systems where data freshness is important.
    * **Configurable Transmission Strategy:** The `LifeLongMote` introduces a configurable transmitting interval and expiration time for data packets. These parameters can be dynamically adjusted by the feedback loop, allowing the system to adapt its communication strategy based on network conditions.

4.  **Improved User Interface and Configuration:**
    * **Enhanced Mote Configuration GUI:** The `MoteGUI` was extended to allow for the configuration of the new `LifeLongMote` parameters, including the transmitting interval and expiration time. This makes it easier for users to set up and run experiments with different adaptation strategies.
    * **Flexible Simulation Input Profiles:** The XML-based input profiles were extended to support the new simulation parameters, such as the `movementRepeatingTime` and `movementRepeatingTimeTimeUnit`. This allows for more complex and realistic simulation scenarios to be defined and loaded.

5.  ** refined Collision Detection and Transmission Model**
    * **More Realistic Packet Collision:** The packet collision logic in `ReceiverWaitPacket` has been refined. It now more accurately models how concurrent transmissions can interfere with each other, leading to more realistic simulation of packet loss.
    * **Geo-Aware Transmission Power Calculation:** The transmission power calculation in `LoraTransmission` was updated to be geo-aware, taking into account the geographical distance between the sender and receiver. This results in a more accurate simulation of signal propagation and attenuation.

By implementing these changes, the DingNet simulator has been transformed into a powerful tool for researching and evaluating self-adaptive strategies in IoT networks. The new architecture is more flexible, realistic, and better equipped to handle the complexities of real-world IoT deployments.


## How to launch
### Building the simulator

To build the simulator, simply run the command `mvn compile`. The generated source are placed in the `target` folder.
The simulator can then be run with the following command: `mvn exec:java`.

Alternatively, run the command `mvn package`. This will generate a jar file under the target directory: `DingNet-{version}-jar-with-dependencies.jar`.

Similarly to the previously listed commands, `mvn test` runs the tests for the project.

### Running the simulator

Either run the jar file generated from the previous step, or use the maven exec plugin.
<!-- A jar file is exported to the folder DingNetExe which also contains the correct file structure. Run the jar file to run the simulator.
The simulator can also be started from the main method in the MainGUI class. -->

## Future goals

- Rewrite of adaptation logic
- Project consistency (e.g. adjust gateway position assignment to geo-coordinates instead of legacy integer values)
- Refactor `InputProfile` and `QualityOfService`
- Rewrite transmission logic
- Provide means of realistic sensor data generation
- Improve testability of project (e.g. removal of singletons where applicable)
