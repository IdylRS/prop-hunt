package com.idyl.prophunt;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("prophunt")
public interface PropHuntConfig extends Config
{
	@ConfigSection(
			name = "Setup",
			description = "Setup settings for the plugin",
			position = 0
	)
	String setupSettings = "setupSettings";

	@ConfigSection(
			name = "Transmog",
			description = "Settings relating to transmog",
			position = 1
	)
	String transmogSettings = "transmogSettings";


	@ConfigItem(
			keyName = "players",
			name = "Player Names",
			description = "Names of the players you are playing with (comma separated)",
			position = 1,
			section = setupSettings
	)
	default String players() { return ""; }

	@ConfigItem(
			keyName = "models",
			name = "Custom Model List",
			description = "Models that you want to play with (formatted: name: id, ...)",
			position = 2,
			section = setupSettings
	)
	default String models() { return "Bush: 1565, Crate: 12125, Rock Pile: 1391"; }

	@ConfigItem(
			keyName = "hideMode",
			name = "Hide Mode",
			description = "Toggle whether you are currently hiding or not",
			position = 1,
			section = transmogSettings
	)
	default boolean hideMode()
	{
		return false;
	}

	@ConfigItem(
			keyName = "limitRightClicks",
			name = "Limit Right Clicks",
			description = "Limit the number of right clicks a non-hider can do",
			position = 5,
			section = setupSettings
	)
	default boolean limitRightClicks() { return false; }

	@ConfigItem(
			keyName = "maxRightClicks",
			name = "Maximum Right Clicks",
			description = "The number of right clicks a non-hider can do",
			position = 6,
			section = setupSettings
	)
	default int maxRightClicks() { return 10; }

	@ConfigItem(
			keyName = "depriorizteMenuOptions",
			name = "Deprioritize Menu Options",
			description = "Forces 'Walk Here' to the top of every menu to better hide props",
			position = 6,
			section = setupSettings
	)
	default boolean depriorizteMenuOptions() { return true; }

	@ConfigItem(
			keyName = "hideMinimapDots",
			name = "Hide Minimap Dots",
			description = "Toggle whether minimap dots are hidden",
			position = 5,
			section = setupSettings
	)
	default boolean hideMinimapDots()
	{
		return false;
	}

	@ConfigItem(
			keyName = "modelID",
			name = "Model ID",
			description = "The ID of the model you'd like to become",
			position = 8,
			section = transmogSettings
	)
	default int modelID() { return 1565; }

	@ConfigItem(
		keyName = "randMinID",
		name = "Min Random Model ID",
		description = "The minimum randomised ID of the model you'd like to become",
		position = 9,
		section = setupSettings
	)
	default int randMinID() { return 1; }

	@ConfigItem(
		keyName = "randMaxID",
		name = "Max Random Model ID",
		description = "The maximum randomised ID of the model you'd like to become",
		position = 10,
		section = setupSettings
	)
	default int randMaxID() { return 47604; }
}
