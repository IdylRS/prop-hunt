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

import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

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

	private HashMap<String, RuneLiteObject>playerDisguises;

	private String[] players;
	private HashMap<String, PropHuntPlayerData> playersData;

	private final Hooks.RenderableDrawListener drawListener = this::shouldDraw;

	@Override
	protected void startUp() throws Exception
	{
		playersData = new HashMap<>();
		hooks.registerRenderableDrawListener(drawListener);
		clientThread.invokeLater(() -> transmogPlayer(client.getLocalPlayer()));
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
			clientThread.invokeLater(() -> transmogPlayer(client.getLocalPlayer()));
		}
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		clientThread.invokeLater(this::removeTransmog);
		if(config.hideMode()) {
			clientThread.invokeLater(() -> transmogPlayer(client.getLocalPlayer()));
		}

		if(event.getKey().equals("players")) {
			log.info("Players changed");
			setPlayersFromString(config.players());
			log.info(config.players());
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

			if(players == null) return true;

			ArrayList<String> playerList = new ArrayList<>(Arrays.asList(players));

			if(playerList.contains(player.getName())) {
				PropHuntPlayerData data = playersData.get(player.getName());

				if(data == null) return true;

				if(data.hiding) {
					clientThread.invokeLater(() -> transmogPlayer(player));
					return !data.hiding;
				}
			}
		}

		return true;
	}

	private void transmogPlayer(Player player) {
		if(!config.hideMode() || client.getLocalPlayer() == null) return;

		disguise = client.createRuneLiteObject();

		LocalPoint loc = LocalPoint.fromWorld(client, player.getWorldLocation());
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

		disguise.setLocation(loc, player.getWorldLocation().getPlane());
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

	private void setPlayersFromString(String playersString) {
		players = playersString.split(",");
		getPlayerConfigs();
	}

	private void getPlayerConfigs() {
		if(players.length < 1) return;

		propHuntDataManager.getPropHuntersByUsernames(players);
	}

	// Called from PropHuntDataManager
	public void updatePlayerData(HashMap<String, PropHuntPlayerData> data) {
		playersData.clear();
		playersData.putAll(data);
	}

	@Provides
	PropHuntConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PropHuntConfig.class);
	}
}
