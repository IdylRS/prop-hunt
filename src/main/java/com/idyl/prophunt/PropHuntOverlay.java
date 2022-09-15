package com.idyl.prophunt;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

public class PropHuntOverlay extends Overlay {
    private static final int MAX_DRAW_DISTANCE = 32;

    private final Client client;
    private final PropHuntConfig config;
    private final PropHuntPlugin plugin;

    @Inject
    private PropHuntOverlay(Client client, PropHuntConfig config, PropHuntPlugin plugin) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;

        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.LOW);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        WorldPoint waypoint = plugin.getCurrentWayPoint();

        if(waypoint == null) {
            return null;
        }

        drawTile(graphics, waypoint, Color.CYAN, new BasicStroke((float) 2));
        return null;
    }

    private void drawTile(Graphics2D graphics, WorldPoint point, Color color, Stroke borderStroke)
    {
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();

        if (point.distanceTo(playerLocation) >= MAX_DRAW_DISTANCE)
        {
            return;
        }

        LocalPoint lp = LocalPoint.fromWorld(client, point);
        if (lp == null)
        {
            return;
        }

        Polygon poly = Perspective.getCanvasTilePoly(client, lp);
        if (poly != null)
        {
            OverlayUtil.renderPolygon(graphics, poly, color, new Color(0, 0, 0, 1), borderStroke);
        }
    }
}
