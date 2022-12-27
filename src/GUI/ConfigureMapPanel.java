package GUI;

import IotDomain.Environment;
import IotDomain.Mote;
import IotDomain.Pair;
import GUI.MapViewer.*;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

public class ConfigureMapPanel {
    private JPanel mainPanel;

    private JPanel drawPanel;
    private JButton saveTrackButton;
    private JLabel errorLabel;
    private JButton cancelButton;
    private JButton guidedButton;
    private JButton freeButton;
    private Environment environment;
    private static JXMapViewer mapViewer = new JXMapViewer();
    // Create a TileFactoryInfo for OpenStreetMap
    private static TileFactoryInfo info = new OSMTileFactoryInfo();
    private static DefaultTileFactory tileFactory = new DefaultTileFactory(info);
    private LinkedList<GeoPosition> currentTrack;
    private Boolean guided = false;
    private Mote currentMote = null;
    private MainGUI parent;

    public ConfigureMapPanel(Environment environment, MainGUI parent) {
        this.parent = parent;
        this.environment = environment;
        loadMap(false);
        currentTrack = new LinkedList<>();
        mapViewer.addMouseListener(new MapMouseAdapter());
        mapViewer.setZoom(6);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        saveTrackButton.addActionListener(new MapSaveTrackActionLister());
        cancelButton.addActionListener(new MapCancelActionLister());
        guidedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guided = true;
                loadMap(true);
                errorLabel.setText("<html><br>1. Select mote<br> by clicking on <br>mote symbol<br><br>2. Select first<br>" +
                        "waypoint<br>along the path<br><br>3.<br>Continue<br>selecting<br>waypoints<br>until the end<br>of " +
                        "the path<br><br>4. Save the<br>path</html>");
            }
        });
        freeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guided = false;
                loadMap(true);
                errorLabel.setText("<html><br>1. Select mote<br> by clicking on <br>mote symbol<br><br>2. Click " +
                        "on first<br>position<br><br>3.<br>Continue<br>clicking on<br>positions<br>until the end<br>" +
                        "of the path<br><br>4. Save the<br>path</html>");
            }
        });
    }


    private void loadMap(Boolean isRefresh) {
        GeoPosition centerPosition = mapViewer.getCenterPosition();
        Integer zoom = mapViewer.getZoom();
        mapViewer.removeAll();
        mapViewer.setTileFactory(tileFactory);
        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(1);
        LinkedList<LinkedList<Pair<Double, Double>>> points = new LinkedList<>();
        for (int i = 0; i <= environment.getMaxXpos(); i += environment.getMaxXpos()) {
            points.add(new LinkedList<>());
            for (int j = 0; j <= environment.getMaxYpos(); j += environment.getMaxYpos()) {
                points.getLast().add(new Pair(environment.toLatitude(j), environment.toLongitude(i)));
            }
        }
        LinkedList<LinkedList<GeoPosition>> verticalLines = new LinkedList<>();
        verticalLines.add(new LinkedList<>());
        verticalLines.add(new LinkedList<>());
        LinkedList<LinkedList<GeoPosition>> horizontalLines = new LinkedList<>();
        horizontalLines.add(new LinkedList<>());
        horizontalLines.add(new LinkedList<>());

        for (int counter1 = 0; counter1 < points.size(); counter1++) {
            for (int counter2 = 0; counter2 < points.get(counter1).size(); counter2++) {
                verticalLines.get(counter1).add(new GeoPosition(points.get(counter1).get(counter2).getLeft(), points.get(counter1).get(counter2).getRight()));
                horizontalLines.get(counter2).add(new GeoPosition(points.get(counter1).get(counter2).getLeft(), points.get(counter1).get(counter2).getRight()));
            }
        }


        int i = 1;
        Map<Waypoint, Integer> motes = new HashMap();
        for (Mote mote : environment.getMotes()) {
            motes.put(new DefaultWaypoint(new GeoPosition(environment.toLatitude(mote.getYPos()), environment.toLongitude(mote.getXPos()))), i);
            i++;
        }

        MoteWaypointPainter<Waypoint> motePainter = new MoteWaypointPainter<>();
        motePainter.setWaypoints(motes.keySet());

        MoteNumberWaypointPainter<Waypoint> moteNumberPainter = new MoteNumberWaypointPainter<>();
        moteNumberPainter.setWaypoints(motes);

        List<Painter<JXMapViewer>> painters = new ArrayList<>();

        painters.add(motePainter);
        painters.add(moteNumberPainter);


        for (LinkedList<GeoPosition> verticalLine : verticalLines) {
            painters.add(new BorderPainter(verticalLine));
        }

        for (LinkedList<GeoPosition> horizontalLine : horizontalLines) {
            painters.add(new BorderPainter(horizontalLine));
        }

        if (currentMote == null) {
            for (Mote mote : environment.getMotes()) {
                painters.add(new TrackPainter(mote.getPath()));
            }
        }

        if (guided) {
            HashSet<DefaultWaypoint> set = new HashSet<DefaultWaypoint>();
            PathWaypointPainter<DefaultWaypoint> waypointPainter = new PathWaypointPainter<>();
            for (GeoPosition waypoint : environment.getWayPoints()) {
                set.add(new DefaultWaypoint(waypoint));
            }
            waypointPainter.setWaypoints(set);
            painters.add(waypointPainter);
        }

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);
        if (isRefresh) {
            mapViewer.setAddressLocation(centerPosition);
            mapViewer.setZoom(zoom);
        } else {
            mapViewer.setAddressLocation(environment.getMapCenter());
            mapViewer.setZoom(5);
        }
        drawPanel.add(mapViewer);
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
        mainPanel.setLayout(new GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 15), null, 0, false));
        drawPanel = new JPanel();
        drawPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(drawPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        drawPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(5, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, null, new Dimension(5, -1), null, 0, false));
        final Spacer spacer4 = new Spacer();
        mainPanel.add(spacer4, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 15), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(0, 3, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(110, -1), new Dimension(110, -1), new Dimension(110, -1), 0, false));
        saveTrackButton = new JButton();
        saveTrackButton.setText("Save");
        panel1.add(saveTrackButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel1.add(spacer5, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        panel1.add(cancelButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        guidedButton = new JButton();
        guidedButton.setText("Guided");
        panel1.add(guidedButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        errorLabel = new JLabel();
        errorLabel.setText("<html><br>Select free<br>or guided</html>");
        panel1.add(errorLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        freeButton = new JButton();
        freeButton.setText("Free");
        panel1.add(freeButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }


    private class MapMouseAdapter implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 1) {
                Point p = e.getPoint();
                GeoPosition geo = mapViewer.convertPointToGeoPosition(p);
                if (currentTrack.size() > 0) {
                    if (guided) {
                        GeoPosition nearestWayPoint = null;
                        for (GeoPosition wayPoint : environment.getWayPoints()) {
                            Integer xDistance = Math.abs(environment.toMapXCoordinate(geo) - environment.toMapXCoordinate(wayPoint));
                            Integer yDistance = Math.abs(environment.toMapYCoordinate(geo) - environment.toMapYCoordinate(wayPoint));

                            if (xDistance < 100 && yDistance < 100) {
                                nearestWayPoint = wayPoint;
                            }
                            if (nearestWayPoint != null) {
                                loadMap(true);
                                currentTrack.add(nearestWayPoint);
                                CompoundPainter<JXMapViewer> painter = (CompoundPainter<JXMapViewer>) mapViewer.getOverlayPainter();
                                painter.addPainter(new TrackPainter(currentTrack));
                                mapViewer.setOverlayPainter(painter);
                            }
                        }
                    } else {
                        loadMap(true);
                        currentTrack.add(geo);
                        CompoundPainter<JXMapViewer> painter = (CompoundPainter<JXMapViewer>) mapViewer.getOverlayPainter();
                        painter.addPainter(new TrackPainter(currentTrack));
                        mapViewer.setOverlayPainter(painter);
                    }
                } else {
                    Mote nearestMote = null;
                    for (Mote mote : environment.getMotes()) {
                        Integer xDistance = Math.abs(environment.toMapXCoordinate(geo) - mote.getXPos());
                        Integer yDistance = Math.abs(environment.toMapYCoordinate(geo) - mote.getYPos());

                        if (xDistance < 100 && yDistance < 100) {
                            nearestMote = mote;
                        }
                        if (nearestMote != null) {
                            currentMote = nearestMote;
                            loadMap(true);
                            currentTrack.add(new GeoPosition(environment.toLatitude(nearestMote.getYPos()), environment.toLongitude(nearestMote.getXPos())));
                            CompoundPainter<JXMapViewer> painter = (CompoundPainter<JXMapViewer>) mapViewer.getOverlayPainter();
                            painter.addPainter(new TrackPainter(currentTrack));
                            mapViewer.setOverlayPainter(painter);
                        }
                    }
                }

            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }


        public void actionPerformed(ActionEvent e) {

        }
    }

    private class MapSaveTrackActionLister implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (currentTrack.size() > 1) {
                currentMote.setPath(currentTrack);
                currentMote = null;
                currentTrack = new LinkedList<>();
                loadMap(true);


                parent.refresh();
            }
        }
    }

    private class MapCancelActionLister implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            currentTrack = new LinkedList<>();
            currentMote = null;
            loadMap(true);
        }
    }
}
