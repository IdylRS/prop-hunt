package com.idyl.prophunt;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("prophunt")
public interface PropHuntConfig extends Config
{
	@ConfigItem(
			keyName = "players",
			name = "Player Names",
			description = "Names of the players you are playing with (comma separated)",
			position = 1
	)
	default String players() { return ""; }

	@ConfigItem(
			keyName = "model",
			name = "Model",
			description = "ID for the model you want to appear as",
			position = 2
	)
	default PropHuntModelId modelID()
	{
		return PropHuntModelId.BUSH;
	}

	@ConfigItem(
			keyName = "hideMode",
			name = "Hide Mode",
			description = "Toggle whether you are currently hiding or not",
			position = 3
	)
	default boolean hideMode()
	{
		return false;
	}

	@ConfigItem(
			keyName = "hideMinimapDots",
			name = "Hide Minimap Dots",
			description = "Toggle whether minimap dots are hidden",
			position = 4
	)
	default boolean hideMinimapDots()
	{
		return false;
	}

	@ConfigItem(
			keyName = "useCustomModelID",
			name = "Use Model ID Field",
			description = "Use the custom Model ID Field instead of the drop down",
			position = 5
	)
	default boolean useCustomModelID() { return false; }

	@ConfigItem(
			keyName = "customModelID",
			name = "Custom Model ID",
			description = "The ID of the model you'd like to become",
			position = 6
	)
	default int customModelID() { return 1565; }
}
