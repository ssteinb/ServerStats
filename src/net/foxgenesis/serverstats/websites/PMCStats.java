package net.foxgenesis.serverstats.websites;

import static net.foxgenesis.serverstats.Logger.error;

import java.io.IOException;
import java.net.URL;

import net.foxgenesis.helper.SiteReader;

import org.json.JSONObject;

/**
 * PMCStats takes in a PMC server post and retrieves the statistics
 *
 * @author Seth
 */
public class PMCStats extends WebsiteStats {
	private static final String[] keys = { "title", "views", "author",
			"diamonds", "favorites", "views_today", "server_votes", "comments",
			"server_score", "stats_updated", "url", "author_url" }, values = {
			"title", "views", "author", "upvotes", "favorites", "v_today",
			"server_votes", "comments", "server_score", "stats_updated", "url",
			"author_url" };

	/**
	 * Create a new PMCStats with given server url
	 */
	public PMCStats() {
		super("pmc", '%');
	}

	@Override
	protected String[][] registerTags() {
		return new String[][] { keys, values };
	}

	@Override
	protected JSONObject update(final URL url) {
		try {
			return new JSONObject(SiteReader.getHTML(new URL(url + "/stats/")));
		} catch (final IOException e) {
			error(e.getMessage().replace("/stats/", ""));
		}
		return null;
	}
}
