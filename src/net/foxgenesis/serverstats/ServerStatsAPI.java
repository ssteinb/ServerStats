package net.foxgenesis.serverstats;

import java.util.Arrays;

import net.foxgenesis.serverstats.websites.WebsiteStats;

public final class ServerStatsAPI {
	/**
	 * Format an array of Strings with WebsiteStats
	 *
	 * @param lines
	 *            - lines to format
	 * @return formated Strings
	 */
	public static String[] format(String[] lines) {
		Arrays.asList(ServerStats.instance.stats).forEach(a -> {
			a.format(lines);
		});
		return lines;
	}

	public static WebsiteStats findByName(String name) {
		return Arrays.asList(ServerStats.instance.stats).stream().filter(a -> a.getName().equalsIgnoreCase(name))
				.findFirst().orElseGet(null);
	}
}
