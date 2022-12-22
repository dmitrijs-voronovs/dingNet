package GUI.MapViewer;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

import IotDomain.AgingMote;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointRenderer;

/**
 * Paints waypoints on the JXMapViewer. This is an
 * instance of Painter that only can draw on to JXMapViewers.
 * @param <W> the waypoint type
 * @author rbair
 */
public class MoteWaypointPainter<W extends Waypoint> extends AbstractPainter<JXMapViewer>
{
    private WaypointRenderer<? super W> renderer = new MoteWaypointRenderer();
    private Set<W> waypoints = new HashSet<W>();
    private LinkedList<AgingMote> motes = new LinkedList<AgingMote>();

    /**
     * Creates a new instance of WaypointPainter
     */
    public MoteWaypointPainter()
    {
        setAntialiasing(true);
        setCacheable(false);
    }

    /**
     * Sets the waypoint renderer to use when painting waypoints
     * @param r the new WaypointRenderer to use
     */
    public void setRenderer(WaypointRenderer<W> r)
    {
        this.renderer = r;
    }

    /**
     * Gets the current set of waypoints to paint
     * @return a typed Set of Waypoints
     */
    public Set<W> getWaypoints()
    {
        return Collections.unmodifiableSet(waypoints);
    }

    /**
     * Sets the current set of waypoints to paint
     * @param waypoints the new Set of Waypoints to use
     */
    public void setWaypoints(Set<? extends W> waypoints)
    {
        this.waypoints.clear();
        this.waypoints.addAll(waypoints);
        this.motes.clear();
    }

    public void setWaypoints(Set<? extends W> waypoints, LinkedList<AgingMote> motes)
    {
        this.waypoints.clear();
        this.waypoints.addAll(waypoints);
        this.motes.clear();
        this.motes.addAll(motes);
    }

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height)
    {
        if (renderer == null)
        {
            return;
        }

        Rectangle viewportBounds = map.getViewportBounds();

        g.translate(-viewportBounds.getX(), -viewportBounds.getY());

        for (W w : getWaypoints())
        {
            AgingMote mote = motes.stream().filter(currentMote -> new GeoPosition(currentMote.getEnvironment().toLatitude(currentMote.getYPos()),
                            currentMote.getEnvironment().toLongitude(currentMote.getXPos())).equals(w.getPosition()))
                    .toList()
                    .get(0);

            if(renderer instanceof MoteWaypointRenderer && mote != null) {
                ((MoteWaypointRenderer) renderer).paintWaypoint(g, map, w, mote.getAgingFactor());
            } else {
                renderer.paintWaypoint(g, map, w);
            }
        }

        g.translate(viewportBounds.getX(), viewportBounds.getY());

    }

}
