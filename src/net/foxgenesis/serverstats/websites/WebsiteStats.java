/**
    Copyright (C) 2015  FoxGenesis

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.foxgenesis.serverstats.websites;

import static net.foxgenesis.serverstats.Logger.log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import net.foxgenesis.helper.StringHelper;
import net.foxgenesis.serverstats.ErrorCodes;
import net.foxgenesis.serverstats.ExternalStrings;
import net.foxgenesis.serverstats.Settings;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.JSONObject;

public abstract class WebsiteStats {
	private URL url;
	private long lastUpdate = -1;
	private long updateTime = 60000;
	private JSONObject json;
	private boolean enabled;
	private String[] display;
	private boolean[] format;
	private final HashMap<String, String> tags = new HashMap<String, String>();
	private final String name;
	private final char tag;

	public WebsiteStats(final String shorthandName, final char tag) {
		name = shorthandName;
		this.tag = tag;
		final String[][] j = registerTags();
		log(ExternalStrings.get("web-stats-create-tags").replace("%name%",
				shorthandName));
		if (j.length > 0)
			for (int i = 0; i < j[0].length; i++)
				tags.put(j[0][i], j[1][i]);
	}

	/**
	 * Display the wanted text to the Player
	 *
	 * @param sender
	 *            - Player to use
	 */
	public final void display(final CommandSender sender) {
		if (enabled)
			for (final String a : format(display))
				try {
					sender.sendMessage(format(a));
				} catch (final Exception e) {
					sender.sendMessage(ChatColor.RED
							+ ErrorCodes.errorText(ErrorCodes.FORMAT));
				}
		else
			sender.sendMessage(ChatColor.RED
					+ ExternalStrings.get("server-stats-enabled").replace(
							"%name%", name));
	}

	/**
	 * Force an update
	 */
	public void forceUpdate() {
		json = update(url);
		lastUpdate = System.currentTimeMillis();
	}

	/**
	 * Format a string by replacing tags with their values
	 *
	 * @param line
	 *            - String to use
	 * @return formated String
	 */
	public final String format(final String line) {
		if (!line.contains(tag + ""))
			return line;
		final int firstIndex = line.indexOf("" + tag) + 1, last = line.indexOf(
				"" + tag, firstIndex + 1);
		return format(removeAndInsert(line, firstIndex, last,
				getStat(line.substring(firstIndex, last))).replaceFirst(
				tag + "", "").replaceFirst(tag + "", ""));
	}

	/**
	 * Format an array of Strings by replacing tags with their values
	 *
	 * @param lines
	 *            - Strings to use
	 * @return formated Strings
	 */
	public final String[] format(final String[] lines) {
		for (int i = 0; i < lines.length; i++)
			lines[i] = format(lines[i]);
		return lines;
	}

	private String get(final String key) {
		final JSONObject j = getJSON();
		if (j != null)
			return j.getString(key);
		return "NULL";
	}

	private JSONObject getJSON() {
		if (getNewStats()) {
			json = update(url);
			lastUpdate = System.currentTimeMillis();
		}
		return json;
	}

	/**
	 * Get the name of the stats
	 *
	 * @return stats name
	 */
	public final String getName() {
		return name;
	}

	private final boolean getNewStats() {
		return lastUpdate == -1
				|| (System.currentTimeMillis() - lastUpdate) >= updateTime;
	}

	private final String getStat(String tag) {
		tag = tag.toLowerCase();
		if (tags.containsKey(tag))
			return get(tags.get(tag));
		else
			return ExternalStrings.get("invalid-tag");
	}

	/**
	 * Return if the website is enabled
	 *
	 * @return website enabled
	 */
	public final boolean isEnabled() {
		return enabled;
	}

	/**
	 * Load settings from configuration file
	 *
	 * @param config
	 *            - config to load from
	 */
	public void loadSettings(final FileConfiguration config) {
		enabled = config.getBoolean(Settings.Website.enabled(name));
		if (!enabled)
			return;
		updateTime = config.getLong(Settings.Cache.EXPIRATION_TIME);
		display = config.getString(Settings.Website.display(name)).split(";");
		format = new boolean[display.length];
		for (int i = 0; i < display.length; i++)
			format[i] = (StringHelper.countChar(display[i], tag) % 2) == 0;
		try {
			url = new URL(config.getString(Settings.Website.url(name)));
		} catch (final MalformedURLException e) {
			ErrorCodes.error(ErrorCodes.INVALID_URL);
		}
	}

	protected abstract String[][] registerTags();

	private final String removeAndInsert(final String line, final int start,
			final int end, final String value) {
		final String front = line.substring(0, start);
		final String back = line.substring(end, line.length());
		return front + value + back;
	}

	/**
	 * Get the new statistics and return them as a JSONObject
	 *
	 * @param url
	 *            - url to get stats from
	 * @return JSONObject representation of the stats
	 */
	protected abstract JSONObject update(URL url);

	protected final void useNewTags(final String[][] tags) {
		this.tags.clear();
		for (int i = 0; i < tags[0].length; i++)
			this.tags.put(tags[0][i], tags[1][i]);
	}
}
