package GUI;

import IotDomain.AgingInputProfile;
import IotDomain.AgingMoteInputProfile;
import IotDomain.Environment;
import IotDomain.Mote;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringWriter;
import java.util.Locale;


public class EditInputProfileGUI {
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;
    private JEditorPane xmlEditorPane;
    private JComboBox numberOfRoundsComboBox;
    private JSpinner moteProbSpinner;
    private JButton classSaveButton;
    private JSpinner regionProbSpinner;
    private JButton updateMoteButton;
    private JButton updateRegionButton;
    private JComboBox moteNumberComboBox;
    private JLabel QOSLabel;
    private JSpinner deviceAdjustmentRate;
    private JComboBox deviceAdjustmentRateUnit;
    private JSpinner initialAge;
    private JComboBox initialAgeUnit;
    private JCheckBox adaptationWasApplied;
    private JSpinner agingCompensationCoefficient;
    private JSpinner energyAdjustmentMultiplier;
    private JSpinner deviceLifespan;
    private JComboBox deviceLifespanUnit;
    private JTextField simulationBeginning;
    private JSpinner simulationStepTime;
    private JComboBox simulationStepTimeUnit;
    private JSpinner regionNumberSpinner;
    private AgingInputProfile inputProfile;
    private Environment environment;

    public EditInputProfileGUI(AgingInputProfile inputProfile, Environment environment) {
        this.inputProfile = inputProfile;
        this.environment = environment;
        setConstants();
        refresh();

        classSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputProfile.setNumberOfRuns(Integer.valueOf((String) numberOfRoundsComboBox.getSelectedItem()));
                refresh();
            }
        });
        updateMoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputProfile.putProbabilitiyForMote(getSelectedMote(), (Double) moteProbSpinner.getValue());
                refresh();
            }
        });
        moteNumberComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputProfile.getProbabilitiesForMotesKeys().contains(getSelectedMote())) {
                    refreshMoteFields();
                } else {
                    moteProbSpinner.setValue(1.00);
                    inputProfile.putProbabilitiyForMote(getSelectedMote(), (Double) moteProbSpinner.getValue());
                    refresh();
                }
            }
        });
    }

    private void setConstants() {
        deviceAdjustmentRate.setValue(1);
        deviceAdjustmentRateUnit.setSelectedItem("months");
    }

    private void refresh() {
        DOMSource DOMSource = new DOMSource(inputProfile.getXmlSource());
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            transformer.transform(DOMSource, result);
        } catch (TransformerConfigurationException e) {

        } catch (TransformerException e) {

        }

        QOSLabel.setText(inputProfile.getName());
        if (environment != null) {
            numberOfRoundsComboBox.setSelectedItem(inputProfile.getNumberOfRuns().toString());
            refreshSimulationDetails();
        }

        if (environment != null) {
            moteNumberComboBox.removeAllItems();
            for (Mote mote : environment.getMotes()) {
                moteNumberComboBox.addItem("Mote " + (environment.getMotes().indexOf(mote) + 1));
            }
            moteNumberComboBox.setSelectedIndex(0);
        } else {
            moteNumberComboBox.removeAllItems();
        }
        refreshMoteFields();
    }

    private void refreshMoteFields() {
        moteProbSpinner.setModel(createSpinnerCoefficientModel(inputProfile.getProbabilityForMote(getSelectedMote())));
        AgingMoteInputProfile selectedMoteInputProfile = inputProfile.getMoteInputProfile(getSelectedMote());
        initialAge.setModel(createSpinnerDurationModel((double) selectedMoteInputProfile.getInitialAge().getLeft()));
        initialAgeUnit.setSelectedItem(selectedMoteInputProfile.getInitialAge().getRight());
        adaptationWasApplied.setSelected(selectedMoteInputProfile.wasAdaptationApplied());
    }

    private int getSelectedMote() {
        return moteNumberComboBox.getSelectedIndex();
    }

    private void refreshSimulationDetails() {
        deviceLifespan.setModel(createSpinnerDurationModel((double) inputProfile.getInputProfileDetails().getDeviceLifespan().getLeft()));
        deviceLifespanUnit.setSelectedItem(inputProfile.getInputProfileDetails().getDeviceLifespan().getRight());
        simulationStepTime.setModel(createSpinnerDurationModel((double) inputProfile.getInputProfileDetails().getSimulationStepTime().getLeft()));
        simulationStepTimeUnit.setSelectedItem(inputProfile.getInputProfileDetails().getSimulationStepTime().getRight());
        simulationBeginning.setText(inputProfile.getInputProfileDetails().getSimulationBeginning());
        agingCompensationCoefficient.setModel(createSpinnerCoefficientModel(inputProfile.getInputProfileDetails().getAgingCompensationCoefficient()));
        energyAdjustmentMultiplier.setModel(createSpinnerDurationModel(inputProfile.getInputProfileDetails().getEnergyAdjustmentMultiplier()));
    }

    private SpinnerNumberModel createSpinnerDurationModel(double value) {
        return new SpinnerNumberModel(value,
                0.0,
                999999999.0,
                1.0);
    }

    private SpinnerNumberModel createSpinnerCoefficientModel(double value) {
        return new SpinnerNumberModel(value,
                0.0,
                1.0,
                0.01);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        tabbedPane1.setEnabled(true);
        mainPanel.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(16, 7, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Configure", panel1);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(15, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("InputProfile:");
        panel1.add(label1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Number of rounds");
        panel1.add(label2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, -1, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Mote:");
        panel1.add(label3, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(28, 28), null, 0, false));
        QOSLabel = new JLabel();
        QOSLabel.setText("Label");
        panel1.add(QOSLabel, new GridConstraints(1, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("QoS:");
        panel1.add(label4, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Reliable Communication");
        panel1.add(label5, new GridConstraints(2, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        moteNumberComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        moteNumberComboBox.setModel(defaultComboBoxModel1);
        panel1.add(moteNumberComboBox, new GridConstraints(11, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(78, 28), null, 0, false));
        numberOfRoundsComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("1");
        defaultComboBoxModel2.addElement("2");
        defaultComboBoxModel2.addElement("3");
        defaultComboBoxModel2.addElement("4");
        defaultComboBoxModel2.addElement("5");
        defaultComboBoxModel2.addElement("6");
        defaultComboBoxModel2.addElement("7");
        defaultComboBoxModel2.addElement("8");
        defaultComboBoxModel2.addElement("9");
        defaultComboBoxModel2.addElement("10");
        defaultComboBoxModel2.addElement("15");
        defaultComboBoxModel2.addElement("20");
        defaultComboBoxModel2.addElement("30");
        defaultComboBoxModel2.addElement("40");
        defaultComboBoxModel2.addElement("50");
        defaultComboBoxModel2.addElement("100");
        defaultComboBoxModel2.addElement("250");
        defaultComboBoxModel2.addElement("500");
        defaultComboBoxModel2.addElement("1000");
        numberOfRoundsComboBox.setModel(defaultComboBoxModel2);
        panel1.add(numberOfRoundsComboBox, new GridConstraints(3, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Device adjustment rate");
        panel1.add(label6, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deviceAdjustmentRate = new JSpinner();
        deviceAdjustmentRate.setEnabled(false);
        deviceAdjustmentRate.setToolTipText("");
        panel1.add(deviceAdjustmentRate, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(88, 28), null, 0, false));
        deviceAdjustmentRateUnit = new JComboBox();
        deviceAdjustmentRateUnit.setEnabled(false);
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("millis");
        defaultComboBoxModel3.addElement("seconds");
        defaultComboBoxModel3.addElement("minutes");
        defaultComboBoxModel3.addElement("hours");
        defaultComboBoxModel3.addElement("days");
        defaultComboBoxModel3.addElement("weeks");
        defaultComboBoxModel3.addElement("months");
        defaultComboBoxModel3.addElement("years");
        deviceAdjustmentRateUnit.setModel(defaultComboBoxModel3);
        panel1.add(deviceAdjustmentRateUnit, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Activity probability");
        panel1.add(label7, new GridConstraints(12, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(101, 28), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Initial age");
        panel1.add(label8, new GridConstraints(13, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(101, 28), null, 0, false));
        moteProbSpinner = new JSpinner();
        panel1.add(moteProbSpinner, new GridConstraints(12, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(88, 28), null, 0, false));
        classSaveButton = new JButton();
        classSaveButton.setText("Save");
        panel1.add(classSaveButton, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updateMoteButton = new JButton();
        updateMoteButton.setText("Update");
        panel1.add(updateMoteButton, new GridConstraints(11, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(78, 28), null, 0, false));
        initialAge = new JSpinner();
        initialAge.setEnabled(true);
        initialAge.setToolTipText("");
        panel1.add(initialAge, new GridConstraints(13, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(88, 28), null, 0, false));
        initialAgeUnit = new JComboBox();
        initialAgeUnit.setEnabled(true);
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("millis");
        defaultComboBoxModel4.addElement("seconds");
        defaultComboBoxModel4.addElement("minutes");
        defaultComboBoxModel4.addElement("hours");
        defaultComboBoxModel4.addElement("days");
        defaultComboBoxModel4.addElement("weeks");
        defaultComboBoxModel4.addElement("months");
        defaultComboBoxModel4.addElement("years");
        initialAgeUnit.setModel(defaultComboBoxModel4);
        panel1.add(initialAgeUnit, new GridConstraints(13, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Was adaptation applied?");
        panel1.add(label9, new GridConstraints(14, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(101, 28), null, 0, false));
        adaptationWasApplied = new JCheckBox();
        adaptationWasApplied.setSelected(true);
        adaptationWasApplied.setText("");
        panel1.add(adaptationWasApplied, new GridConstraints(14, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Aging compensation coefficient");
        panel1.add(label10, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        agingCompensationCoefficient = new JSpinner();
        agingCompensationCoefficient.setEnabled(true);
        agingCompensationCoefficient.setToolTipText("");
        panel1.add(agingCompensationCoefficient, new GridConstraints(8, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(88, 28), null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Energy adjustment multiplier");
        panel1.add(label11, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        energyAdjustmentMultiplier = new JSpinner();
        energyAdjustmentMultiplier.setEnabled(true);
        energyAdjustmentMultiplier.setToolTipText("");
        panel1.add(energyAdjustmentMultiplier, new GridConstraints(9, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(88, 28), null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Device lifespan");
        panel1.add(label12, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deviceLifespan = new JSpinner();
        deviceLifespan.setEnabled(true);
        deviceLifespan.setToolTipText("");
        panel1.add(deviceLifespan, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(88, 28), null, 0, false));
        deviceLifespanUnit = new JComboBox();
        deviceLifespanUnit.setEnabled(true);
        final DefaultComboBoxModel defaultComboBoxModel5 = new DefaultComboBoxModel();
        defaultComboBoxModel5.addElement("millis");
        defaultComboBoxModel5.addElement("seconds");
        defaultComboBoxModel5.addElement("minutes");
        defaultComboBoxModel5.addElement("hours");
        defaultComboBoxModel5.addElement("days");
        defaultComboBoxModel5.addElement("weeks");
        defaultComboBoxModel5.addElement("months");
        defaultComboBoxModel5.addElement("years");
        deviceLifespanUnit.setModel(defaultComboBoxModel5);
        panel1.add(deviceLifespanUnit, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Simulation beginning");
        panel1.add(label13, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        simulationBeginning = new JTextField();
        simulationBeginning.setText("yyyy-mm-dd");
        simulationBeginning.setToolTipText("yyyy-mm-dd");
        panel1.add(simulationBeginning, new GridConstraints(7, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Simulation step time");
        panel1.add(label14, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        simulationStepTime = new JSpinner();
        simulationStepTime.setEnabled(true);
        simulationStepTime.setToolTipText("");
        panel1.add(simulationStepTime, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(88, 28), null, 0, false));
        simulationStepTimeUnit = new JComboBox();
        simulationStepTimeUnit.setEnabled(true);
        final DefaultComboBoxModel defaultComboBoxModel6 = new DefaultComboBoxModel();
        defaultComboBoxModel6.addElement("millis");
        defaultComboBoxModel6.addElement("seconds");
        defaultComboBoxModel6.addElement("minutes");
        defaultComboBoxModel6.addElement("hours");
        defaultComboBoxModel6.addElement("days");
        defaultComboBoxModel6.addElement("weeks");
        defaultComboBoxModel6.addElement("months");
        defaultComboBoxModel6.addElement("years");
        simulationStepTimeUnit.setModel(defaultComboBoxModel6);
        panel1.add(simulationStepTimeUnit, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel1.add(spacer5, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 1), null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}

