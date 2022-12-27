package IotDomain;

import GUI.MainGUI;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AgingInputProfile extends InputProfile {
    private InputProfileDetails inputProfileDetails;
    private HashMap<Integer, AgingMoteInputProfile> agingMoteInputProfiles;

    public AgingInputProfile(String name,
                             QualityOfService qualityOfServiceProfile,
                             Integer numberOfRuns,
                             InputProfileDetails inputProfileDetails,
                             HashMap<Integer, AgingMoteInputProfile> agingMoteInputProfiles,
                             HashMap<Integer, Double> probabilitiesForGateways,
                             HashMap<Integer, Double> regionProbabilities,
                             Element xmlSource,
                             MainGUI gui) {
        super(name,
                qualityOfServiceProfile,
                numberOfRuns,
                (HashMap) agingMoteInputProfiles.entrySet().stream()
                        .collect(Collectors.toMap(
                                m -> ((Map.Entry) m).getKey(),
                                m -> ((AgingMoteInputProfile)((Map.Entry) m).getValue()).getActivityProbability())),
                probabilitiesForGateways,
                regionProbabilities,
                xmlSource,
                gui);
        this.inputProfileDetails = inputProfileDetails;
        this.agingMoteInputProfiles = agingMoteInputProfiles;
    }

    public InputProfileDetails getInputProfileDetails() {
        return inputProfileDetails;
    }

    public AgingMoteInputProfile getMoteInputProfile(int id) {
        return agingMoteInputProfiles.get(id);
    }
}
