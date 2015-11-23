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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.json.JSONObject;

import net.foxgenesis.helper.StringHelper;
import net.foxgenesis.serverstats.ErrorHandler;
import net.foxgenesis.serverstats.ExternalStrings;
import net.foxgenesis.serverstats.Settings;

public abstract class WebsiteStats{
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

	public WebsiteStats(String shorthandName, char tag) {
		name = shorthandName;
		this.tag = tag;
		String[][] j = registerTags();
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
	public final void display(CommandSender sender) {
		if (enabled)
			Arrays.asList(format(display)).forEach(a -> {
				try {
					sender.sendMessage(format(a));
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + ErrorHandler.errorText(ErrorHandler.ErrorType.FORMAT));
				}
			});
		else
			sender.sendMessage(ChatColor.RED + ExternalStrings.get("server-stats-enabled").replace("%name%", name));
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
	public final String format(String line) {
		if (!line.contains(tag + ""))
			return line;
		int firstIndex = line.indexOf("" + tag) + 1, last = line.indexOf("" + tag, firstIndex + 1);
		return format(removeAndInsert(line, firstIndex, last, getStat(line.substring(firstIndex, last)))
				.replaceFirst(tag + "", "").replaceFirst(tag + "", ""));
	}

	/**
	 * Format an array of Strings by replacing tags with their values
	 *
	 * @param lines
	 *            - Strings to use
	 * @return formated Strings
	 */
	public final String[] format(String[] lines) {
		for (int i = 0; i < lines.length; i++)
			lines[i] = format(lines[i]);
		return lines;
	}

	private String get(String key) {
		JSONObject j = getJSON();
		if (j != null)
			return j.getString(key);
		return "NULL";
	}

	protected JSONObject getJSON() {
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

	private boolean getNewStats() {
		return lastUpdate == -1 || (System.currentTimeMillis() - lastUpdate) >= updateTime;
	}

	private String getStat(String tag) {
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
	public void loadSettings(FileConfiguration config) {
		enabled = config.getBoolean(Settings.Website.enabled(name));
		if (!enabled)
			return;
		updateTime = config.getLong(Settings.Cache.EXPIRATION_TIME) * 60000;
		display = config.getString(Settings.Website.display(name)).split(";");
		format = new boolean[display.length];
		for (int i = 0; i < display.length; i++)
			format[i] = (StringHelper.countChar(display[i], tag) % 2) == 0;
		try {
			url = new URL(config.getString(Settings.Website.url(name)));
		} catch (MalformedURLException e) {
			ErrorHandler.error(ErrorHandler.ErrorType.INVALID_URL);
		}
	}

	/**
	 * Preset the keys used for the JSON object<br>
	 * <b>This is only used if the data has constant keys.</b> Otherwise use {@link #useNewKeys()}<br>
	 * <b><u>Keys</u></b> - The keys of the JSON.<br>
	 * <b><u>Output keys</u></b> - Keys that will retrieve the data (the ones located in the config file).
	 * <br><br>(i.e.) <pre>{@code new String[][]{array of keys,array of output keys};</pre>
	 * @return Array of keys and output keys
	 */
	protected abstract String[][] registerTags();

	private String removeAndInsert(String line, int start, int end, String value) {
		String front = line.substring(0, start);
		String back = line.substring(end, line.length());
		return front + value + back;
	}

	/**
	 * Get the new statistics and return them as a {@link JSONObject}
	 * @param url - {@link URL} to get stats from
	 * @return {@link JSONObject} representation of the stats
	 */
	protected abstract JSONObject update(URL url);

	/**
	 * Update the keys used. See {@link #registerTags()} for details
	 * @param tags - Array of keys and output keys
	 */
	protected final void useNewKeys(String[][] tags) {
		this.tags.clear();
		for (int i = 0; i < tags[0].length; i++)
			this.tags.put(tags[0][i], tags[1][i]);
	}
	
	/**
	 * Update keys used by {@link JSONObject}. See {@link #registerTags()} for details
	 * @param j - {@link JSONObject} to get keys from
	 */
	protected final void useNewKeys(JSONObject j) {
		useNewKeys(new String[][] { j.keySet().toArray(new String[] {}), j.keySet().toArray(new String[] {}) });
	}
}
