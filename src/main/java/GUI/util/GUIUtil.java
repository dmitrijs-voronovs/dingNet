package GUI.util;

import GUI.MapViewer.LinePainter;
import GUI.MapViewer.MoteWayPoint;
import IotDomain.Environment;
import IotDomain.networkentity.UserMote;
import util.MapHelper;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GUIUtil {
    public static List<LinePainter> getBorderPainters(int maxX, int maxY) {
        var mapHelper = MapHelper.getInstance();
        List<LinePainter> painters = new ArrayList<>();

        painters.add(new LinePainter(List.of(mapHelper.toGeoPosition(0, 0), mapHelper.toGeoPosition(0, maxY))));
        painters.add(new LinePainter(List.of(mapHelper.toGeoPosition(0, 0), mapHelper.toGeoPosition(maxX, 0))));
        painters.add(new LinePainter(List.of(mapHelper.toGeoPosition(maxX, 0), mapHelper.toGeoPosition(maxX, maxY))));
        painters.add(new LinePainter(List.of(mapHelper.toGeoPosition(0, maxY), mapHelper.toGeoPosition(maxX, maxY))));

        return painters;
    }

    public static Map<MoteWayPoint, Integer> getMoteMap(Environment environment) {
        Map<MoteWayPoint, Integer> map = new HashMap<>();
        var motes = environment.getMotes();
        var mapHelper = MapHelper.getInstance();

        var wraps = motes.stream()
            .map(m -> {
                var pos = mapHelper.toGeoPosition(m.getPosInt());
                if (m instanceof UserMote) {
                    return new MoteWayPoint(pos, true, ((UserMote)m).isActive());
                }
                return new MoteWayPoint(pos);
            })
            .collect(Collectors.toList());

        IntStream.range(0, wraps.size())
            .forEach(i -> map.put(wraps.get(i), i+1));
        return map;
    }

    public static File getOutputFile(File givenFile, String extension) {
        String name = givenFile.getName();

        if (name.length() < extension.length() + 2 || !name.substring(name.length() - (extension.length()+1)).equals("." + extension)) {
            // Either the filename is too short, or it is still missing the (right) extension
            return new File(givenFile.getPath() + "." + extension);
        } else {
            return new File(givenFile.getPath());
        }
    }

    public static void updateTextFieldCoordinates(JTextField textField, double value, String alt1, String alt2) {
        // TODO clean up magic numbers
        int degrees = (int) Math.floor(value);
        int minutes = (int) Math.floor((value - degrees) * 60);
        double seconds = (double) Math.round(((value - degrees) * 60 - minutes) * 60 * 1000d) / 1000d;

        textField.setText(String.format("%s %d° %d' %f\"", value > 0 ? alt1 : alt2, degrees, minutes, seconds));
    }
}
