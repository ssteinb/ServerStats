package net.foxgenesis.serverstats;

import static net.foxgenesis.serverstats.Logger.error;
import static net.foxgenesis.serverstats.Logger.log;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

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
	private final HashMap<String, String> tags = new HashMap<String, String>();
	private final String name;

	public WebsiteStats(String shorthandName) {
		name = shorthandName;
		String[][] j = registerKeys();
		log("Creating tags for " + shorthandName);
		for(int i=0; i<j[0].length; i++)
			tags.put(j[0][i], j[1][i]);
	}

	private String get(String key) {
		JSONObject j = getJSON();
		if(j!=null)
			return j.getString(key);
		return "NULL";
	}

	private JSONObject getJSON() {
		if(getNewStats()) {
			json = update(url);
			lastUpdate = System.currentTimeMillis();
		}
		return json;
	}

	private boolean getNewStats() {
		return lastUpdate == -1 || (System.currentTimeMillis() - lastUpdate) >= updateTime;
	}
	
	/**
	 * Get the name of the stats
	 * @return stats name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Load settings from configuration file
	 * @param config - config to load from
	 */
	public void loadSettings(FileConfiguration config) {
		updateTime = config.getLong("settings.cache.expiration-time");
		enabled = config.getBoolean("settings." + name + ".enable");
		display = config.getString("settings." + name + ".display").split(";");
		try {
			url = new URL(config.getString("settings." + name + ".url"));
		} catch (MalformedURLException e) {error(ErrorCodes.error(ErrorCodes.URL_INVALID));}
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
			sender.sendMessage(ChatColor.RED + name + " is not enabled");
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
	
	private String getStat(String tag) {
		tag = tag.toLowerCase();
		if(tags.containsKey(tag))
			return get(tags.get(tag));
		else
			return "Unsupported Tag";
	}

	/**
	 * Get the new statistics and return them as a JSONObject
	 * @param url - url to get stats from
	 * @return JSONObject representation of the stats
	 */
	protected abstract JSONObject update(URL url);
	protected abstract String[][] registerKeys();
}
