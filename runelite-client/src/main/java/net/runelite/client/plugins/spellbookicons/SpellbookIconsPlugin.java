package net.runelite.client.plugins.spellbookicons;

import com.google.inject.Provides;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.WidgetHiddenChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.Arrays;

@PluginDescriptor(
	name = "Spellbook Icons",
	description = "Enables the ability to resize the spellbook icons"
)
@Slf4j
public class SpellbookIconsPlugin extends Plugin
{
	static final String CONFIG_GROUP = "spellbookicons";

	static final int SPELL_WIDGET_DEFAULT_SIZE = 24;

	@Inject
	private SpellbookIconsConfig config;

	@Inject
	private Client client;

	private Widget spellWidgetContainer;

	@Override
	protected void startUp() throws Exception
	{
		resizeIcons();
	}

	@Override
	protected void shutDown() throws Exception
	{
		resetWidgets();
	}

	@Provides
	SpellbookIconsConfig getConfig(ConfigManager manager)
	{
		return manager.getConfig(SpellbookIconsConfig.class);
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		if (event.getGroupId() == WidgetID.SPELLBOOK_GROUP_ID)
		{
			spellWidgetContainer = client.getWidget(WidgetInfo.SPELLBOOK_WIDGET_CONTAINER);
		}
	}

	@Subscribe
	public void onWidgetHiddenChanged(WidgetHiddenChanged event)
	{
		// Resize spell widgets when the spellbook tab opens
		if (event.getWidget() == client.getWidget(WidgetID.SPELLBOOK_GROUP_ID, 0))
		{
			resizeIcons();
		}
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(CONFIG_GROUP))
		{
			resizeIcons();
		}
	}

	private void setWidgetSize(Widget widget, int width, int height)
	{
		widget.setWidth(width);
		widget.setHeight(height);
	}

	private void setWidgetSize(Widget widget, int size)
	{
		setWidgetSize(widget, size, size);
	}

	private void setWidgetPosition(Widget widget, int x, int y)
	{
		widget.setRelativeX(x);
		widget.setRelativeY(y);
	}

	private void setWidgetSizeAndPosition(Widget widget, int width, int height, int x, int y)
	{
		setWidgetSize(widget, width, height);
		setWidgetPosition(widget, x, y);
	}

	private void resizeIcons()
	{
		if (spellWidgetContainer == null)
		{
			return;
		}

		// Make sure the container is full size (Size changes when filtered)
		setWidgetSizeAndPosition(spellWidgetContainer, 180, 240, 2, 0);

		final Widget[] spellWidgets = spellWidgetContainer.getStaticChildren();
		int widgetSize = config.widgetSize();

		Arrays.stream(spellWidgets)
				.forEach(widget -> setWidgetSize(widget, widgetSize));
	}

	private void resetWidgets()
	{
		if (spellWidgetContainer == null)
		{
			return;
		}

		final Widget[] spellWidgets = spellWidgetContainer.getStaticChildren();

		Arrays.stream(spellWidgets)
				.forEach(widget -> setWidgetSize(widget, SPELL_WIDGET_DEFAULT_SIZE));
	}
}
