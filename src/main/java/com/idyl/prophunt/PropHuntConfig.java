package com.idyl.prophunt;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("prophunt")
public interface PropHuntConfig extends Config
{
	@ConfigItem(
		keyName = "hideMode",
		name = "Hide Mode",
		description = "Toggle whether you are currently hiding or not"
	)
	default boolean hideMode()
	{
		return false;
	}

	@ConfigItem(
			keyName = "modelID",
			name = "Model ID",
			description = "ID for the model you want to appear as"
	)
	default PropHuntModelId modelID()
	{
		return PropHuntModelId.DEAD_TREE;
	}

	@ConfigItem(
			keyName = "players",
			name = "Player Names",
			description = "Names of the players you are playing with (comma separated)"
	)
	default String players() { return ""; }
}
