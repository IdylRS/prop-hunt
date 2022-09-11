package com.idyl.prophunt;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.Hooks;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Slf4j
@PluginDescriptor(
	name = "Prop Hunt"
)
public class PropHuntPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private PropHuntConfig config;

	@Inject
	private Hooks hooks;

	@Inject
	private ClientThread clientThread;

	@Inject
	private PropHuntDataManager propHuntDataManager;

	private RuneLiteObject disguise;

	private final Hooks.RenderableDrawListener drawListener = this::shouldDraw;

	@Override
	protected void startUp() throws Exception
	{
		hooks.registerRenderableDrawListener(drawListener);
		clientThread.invokeLater(this::transmogPlayer);
	}

	@Override
	protected void shutDown() throws Exception
	{
		clientThread.invokeLater(this::removeTransmog);
		hooks.unregisterRenderableDrawListener(drawListener);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (GameState.LOGGED_IN.equals(event.getGameState()) && config.hideMode())
		{
			clientThread.invokeLater(this::transmogPlayer);
		}
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		clientThread.invokeLater(this::removeTransmog);
		propHuntDataManager.getPropHunters();
		if(config.hideMode()) {
			clientThread.invokeLater(this::transmogPlayer);
		}
	}

	@Subscribe
	public void onGameTick(final GameTick event) {
		if(config.hideMode() && disguise != null) {
			WorldPoint playerPoint = client.getLocalPlayer().getWorldLocation();
			disguise.setLocation(LocalPoint.fromWorld(client, playerPoint), playerPoint.getPlane());
		}
	}

	@VisibleForTesting
	boolean shouldDraw(Renderable renderable, boolean drawingUI)
	{
		if (renderable instanceof Player)
		{
			Player player = (Player) renderable;
			Player local = client.getLocalPlayer();

			if (player == local)
			{
				return !config.hideMode();
			}
		}

		return true;
	}

	private void transmogPlayer() {
		if(!config.hideMode() || client.getLocalPlayer() == null) return;

		disguise = client.createRuneLiteObject();

		LocalPoint loc = LocalPoint.fromWorld(client, client.getLocalPlayer().getWorldLocation());
		if (loc == null)
		{
			return;
		}

		Model model = client.loadModel(config.modelID().toInt());

		if (model == null)
		{
			final Instant loadTimeOutInstant = Instant.now().plus(Duration.ofSeconds(5));

			clientThread.invoke(() ->
			{
				if (Instant.now().isAfter(loadTimeOutInstant))
				{
					return true;
				}

				Model reloadedModel = client.loadModel(config.modelID().toInt());

				if (reloadedModel == null)
				{
					return false;
				}

				disguise.setModel(reloadedModel);

				return true;
			});
		}
		else {
			disguise.setModel(model);
		}

		disguise.setLocation(loc, client.getLocalPlayer().getWorldLocation().getPlane());
		disguise.setActive(true);
	}

	private void removeTransmog()
	{
		if (disguise != null)
		{
			disguise.setActive(false);
		}
		disguise = null;
	}

	private Map<String, String> getPlayerList() {
		return null;
	}

	@Provides
	PropHuntConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PropHuntConfig.class);
	}
}
