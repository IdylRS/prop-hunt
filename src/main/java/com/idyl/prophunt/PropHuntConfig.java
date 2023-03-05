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
			keyName = "model",
			name = "Model",
			description = "ID for the model you want to appear as",
			position = 2,
			section = transmogSettings
	)
	default PropHuntModelId modelID()
	{
		return PropHuntModelId.BUSH;
	}


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
			keyName = "useCustomModelID",
			name = "Use Custom Model ID",
			description = "Use the custom Model ID Field instead of the drop down",
			position = 7,
			section = setupSettings
	)
	default boolean useCustomModelID() { return false; }

	@ConfigItem(
			keyName = "customModelID",
			name = "Custom Model ID",
			description = "The ID of the model you'd like to become",
			position = 8,
			section = transmogSettings
	)
	default int customModelID() { return 1565; }

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
