package IotDomain;

import GUI.MainGUI;
import org.w3c.dom.Document;
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
    public void addMoteInputProfile(int id, AgingMoteInputProfile moteInputProfile) {
        agingMoteInputProfiles.put(id, moteInputProfile);
    }

    @Override
    protected void createInputProfileNodeExtension(Element inputProfileElement) {
        super.createInputProfileNodeExtension(inputProfileElement);

        append(inputProfileElement, "deviceAdjustmentRate", getInputProfileDetails().getDeviceAdjustmentRate().getLeft().toString());
        append(inputProfileElement, "deviceAdjustmentRateUnit", getInputProfileDetails().getDeviceAdjustmentRate().getRight());
        append(inputProfileElement, "agingCompensationCoefficient", Float.toString(getInputProfileDetails().getAgingCompensationCoefficient()));
        append(inputProfileElement, "energyAdjustmentMultiplier", Float.toString(getInputProfileDetails().getEnergyAdjustmentMultiplier()));
        append(inputProfileElement, "deviceLifespan", getInputProfileDetails().getDeviceLifespan().getLeft().toString());
        append(inputProfileElement, "deviceLifespanUnit", getInputProfileDetails().getDeviceLifespan().getRight());
        append(inputProfileElement, "simulationBeginning", getInputProfileDetails().getSimulationBeginning());
        append(inputProfileElement, "simulationStepTime", getInputProfileDetails().getSimulationStepTime().getLeft().toString());
        append(inputProfileElement, "simulationStepTimeUnit", getInputProfileDetails().getSimulationStepTime().getRight());
    }

    @Override
    protected void createMoteNodeExtension(Element moteElement, int moteNumber) {
        super.createMoteNodeExtension(moteElement, moteNumber);

        append(moteElement, "initialAge", getMoteInputProfile(moteNumber).getInitialAge().getLeft().toString());
        append(moteElement, "initialAgeUnit", getMoteInputProfile(moteNumber).getInitialAge().getRight());
        append(moteElement, "adaptationWasApplied", Boolean.toString(getMoteInputProfile(moteNumber).wasAdaptationApplied()));
    }

    private void append(Element target, String name, String value) {
        Document doc = getXmlSource();
        Element el = doc.createElement(name);
        el.appendChild(doc.createTextNode(value));
        target.appendChild(el);
    }
}
