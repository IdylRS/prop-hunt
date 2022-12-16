package com.idyl.prophunt;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY;

public class PropHuntOverlay extends OverlayPanel {
    public static OverlayMenuEntry RESET_ENTRY = new OverlayMenuEntry(RUNELITE_OVERLAY, "Reset", "Counter");

    private PropHuntPlugin plugin;
    private PropHuntConfig config;
    private final LineComponent rightClicksRemainingComponent;

    @Inject
    private PropHuntOverlay(PropHuntPlugin plugin, PropHuntConfig config) {
        this.plugin = plugin;
        this.config = config;
        getMenuEntries().add(RESET_ENTRY);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGH);
        setPosition(OverlayPosition.BOTTOM_LEFT);

        rightClicksRemainingComponent = LineComponent.builder().left("Right Clicks Remaining:").right("").build();
        panelComponent.getChildren().add(rightClicksRemainingComponent);

        setClearChildren(false);
    }
    
    @Override
    public Dimension render(Graphics2D graphics) {
        if(config.limitRightClicks() <= 0) return null;

        graphics.setFont(FontManager.getRunescapeFont());
        rightClicksRemainingComponent.setRightColor(getColor());
        rightClicksRemainingComponent.setRight(getClicksRemaining()+"");
        return super.render(graphics);
    }

    private Color getColor() {
        return getClicksRemaining() > 3 ? Color.GREEN : getClicksRemaining() > 0 ? Color.YELLOW : Color.RED;
    }

    private int getClicksRemaining() {
        return config.limitRightClicks() - plugin.getRightClickCounter();
    }
}
