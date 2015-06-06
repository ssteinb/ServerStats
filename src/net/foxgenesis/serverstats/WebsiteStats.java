package net.foxgenesis.serverstats;

import static net.foxgenesis.serverstats.Logger.log;
import static net.foxgenesis.serverstats.Logger.error;

import java.net.MalformedURLException;
import java.net.URL;

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

	public WebsiteStats(String url) {
		try {
			this.url = new URL(url);
			update(this.url);
		} catch (MalformedURLException e) {error(ErrorCodes.error(ErrorCodes.WEB_REQUEST));}
	}

	/**
	 * Get if the stats should be enabled
	 * @return stats enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Check if the data object is null
	 * @return if data object is not null
	 */
	protected boolean hasData() {
		return hasURL() && json != null;
	}

	/**
	 * Check if the stats contains a valid url
	 * @return url is valid
	 */
	public boolean hasURL() {
		return url != null;
	}

	protected String get(String key) {
		JSONObject j = getJSON();
		if(j!=null)
			return j.getString(key);
		return null;
	}

	private JSONObject getJSON() {
		if(getNewStats()) {
			json = update(url);
			lastUpdate = System.currentTimeMillis();
		}
		else
			log("using old data");
		return json;
	}

	private boolean getNewStats() {
		return lastUpdate == -1 || (System.currentTimeMillis() - lastUpdate) >= updateTime;
	}

	/**
	 * Load settings from configuration file
	 * @param config - config to load from
	 */
	public void loadSettings(FileConfiguration config) {
		updateTime = config.getLong("settings.cache.expiration-time");
		enabled = config.getBoolean("settings." + getShorthandName() + ".enable");
		display = config.getString("settings." + getShorthandName() + ".display").split(";");
	}

	public void display(CommandSender sender) {
		if(enabled) {
			try {
			String[] out = format(display);
			for(String a:out)
				sender.sendMessage(format(a));
			}catch(Exception e) {
				sender.sendMessage(ChatColor.RED + ErrorCodes.error(ErrorCodes.FORMAT));
			}
		}
		else
			sender.sendMessage(ChatColor.RED + getShorthandName() + " is not enabled");
	}

	protected String[] format(String[] lines) {
		for(int i=0; i<lines.length; i++)
			lines[i] = format(lines[i]);
		return lines;
	}
	
	protected String format(String line) {
		if(!line.contains("%"))
			return line;
		int firstIndex = line.indexOf("%")+1,
				last = line.indexOf("%", firstIndex+1);
		return format(removeAndInsert(line,firstIndex,last,getStat(line.substring(firstIndex, last))).replaceFirst("%", "").replaceFirst("%", ""));
	}

	private String removeAndInsert(String line, int start, int end, String value) {
		String front = line.substring(0, start);
		String back = line.substring(end, line.length());
		return front + value + back;
	}

	/**
	 * Get the new statistics and return them as a JSONObject
	 * @param url - url to get stats from
	 * @return JSONObject representation of the stats
	 */
	protected abstract JSONObject update(URL url);
	protected abstract String getShorthandName();
	protected abstract String getStat(String key);
}
