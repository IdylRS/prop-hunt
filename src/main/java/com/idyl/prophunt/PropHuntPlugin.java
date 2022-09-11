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
import net.runelite.client.task.Schedule;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

	private RuneLiteObject localDisguise;

	private HashMap<String, RuneLiteObject> playerDisguises = new HashMap<>();

	private String[] players;
	private HashMap<String, PropHuntPlayerData> playersData;

	private final Hooks.RenderableDrawListener drawListener = this::shouldDraw;

	@Override
	protected void startUp() throws Exception
	{
		playersData = new HashMap<>();
		hooks.registerRenderableDrawListener(drawListener);
		clientThread.invokeLater(() -> transmogPlayer(client.getLocalPlayer()));
		setPlayersFromString(config.players());
		getPlayerConfigs();
	}

	@Override
	protected void shutDown() throws Exception
	{
		clientThread.invokeLater(this::removeAllTransmogs);
		hooks.unregisterRenderableDrawListener(drawListener);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (GameState.LOGGED_IN.equals(event.getGameState()))
		{
			if(config.hideMode()) clientThread.invokeLater(() -> transmogPlayer(client.getLocalPlayer()));
			propHuntDataManager.updatePropHuntApi(new PropHuntPlayerData(client.getLocalPlayer().getName(),
					config.hideMode(), config.modelID().toInt()));
		}
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {
		clientThread.invokeLater(this::removeAllTransmogs);
		if(config.hideMode()) {
			clientThread.invokeLater(() -> transmogPlayer(client.getLocalPlayer()));
		}

		if(event.getKey().equals("players")) {
			setPlayersFromString(config.players());
			getPlayerConfigs();
		}

		if(client.getLocalPlayer() != null) {
			propHuntDataManager.updatePropHuntApi(new PropHuntPlayerData(client.getLocalPlayer().getName(),
					config.hideMode(), config.modelID().toInt()));
			clientThread.invokeLater(() -> transmogOtherPlayers());
		}
	}

	@Subscribe
	public void onGameTick(final GameTick event) {
		if(config.hideMode() && localDisguise != null) {
			WorldPoint playerPoint = client.getLocalPlayer().getWorldLocation();
			localDisguise.setLocation(LocalPoint.fromWorld(client, playerPoint), playerPoint.getPlane());
		}

		client.getPlayers().forEach(player -> updateDisguiseLocation(player));
	}

	// Hide players who are participating in prop hunt
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
					return !data.hiding;
				}
			}
		}

		return true;
	}

	private void transmogPlayer(Player player) {
		transmogPlayer(player, config.modelID().toInt(), true);
	}

	private void transmogPlayer(Player player, int modelID, boolean local) {
		if(client.getLocalPlayer() == null) return;

		RuneLiteObject disguise = client.createRuneLiteObject();

		LocalPoint loc = LocalPoint.fromWorld(client, player.getWorldLocation());
		if (loc == null)
		{
			return;
		}

		Model model = client.loadModel(modelID);

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

				localDisguise.setModel(reloadedModel);

				return true;
			});
		}
		else {
			disguise.setModel(model);
		}

		disguise.setLocation(loc, player.getWorldLocation().getPlane());
		disguise.setActive(true);

		if(local) {
			localDisguise = disguise;
		}
		else {
			playerDisguises.put(player.getName(), disguise);
		}
	}

	private void transmogOtherPlayers() {
		if(players == null || client.getLocalPlayer() == null) return;

		client.getPlayers().forEach(player -> {
			if(client.getLocalPlayer() == player) return;

			PropHuntPlayerData data = playersData.get(player.getName());

			if(data == null || !data.hiding) return;

			transmogPlayer(player, data.modelID, false);
		});
	}

	private void removeLocalTransmog() {
		if (localDisguise != null)
		{
			localDisguise.setActive(false);
		}
		localDisguise = null;
	}

	private void removeTransmogs()
	{
		playerDisguises.forEach((p, disguise) -> {
			if(disguise == null) return;

			disguise.setActive(false);
			disguise = null;
		});
	}

	private void removeAllTransmogs() {
		removeTransmogs();
		removeLocalTransmog();
	}

	private void updateDisguiseLocation(Player p) {
		RuneLiteObject obj = playerDisguises.get(p.getName());
		if(obj == null) return;

		obj.setLocation(p.getLocalLocation(), p.getWorldLocation().getPlane());
	}

	private void setPlayersFromString(String playersString) {
		players = playersString.split(",");
	}

	@Schedule(
			period = 15,
			unit = ChronoUnit.SECONDS,
			asynchronous = true
	)
	public void getPlayerConfigs() {
		if(players.length < 1) return;

		log.info("Getting player configs...");

		propHuntDataManager.getPropHuntersByUsernames(players);
	}

	// Called from PropHuntDataManager
	public void updatePlayerData(HashMap<String, PropHuntPlayerData> data) {
		clientThread.invokeLater(() -> {
			removeTransmogs();
			playersData.clear();
			playerDisguises.clear();
			playersData.putAll(data);
			playersData.values().forEach(player -> playerDisguises.put(player.username, null));
			transmogOtherPlayers();
		});
	}

	@Provides
	PropHuntConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PropHuntConfig.class);
	}
}
