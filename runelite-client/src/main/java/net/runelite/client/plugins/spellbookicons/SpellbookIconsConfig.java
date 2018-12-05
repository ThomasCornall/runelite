package net.runelite.client.plugins.spellbookicons;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(SpellbookIconsPlugin.CONFIG_GROUP)
public interface SpellbookIconsConfig extends Config
{
	@ConfigItem(
		keyName = "widgetSize",
		name = "Widget Size",
		description = "Size in pixels to set the spell icon widgets"
	)
	default int widgetSize()
	{
		return SpellbookIconsPlugin.SPELL_WIDGET_DEFAULT_SIZE;
	}
}
