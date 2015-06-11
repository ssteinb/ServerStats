package net.foxgenesis.serverstats;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static net.foxgenesis.serverstats.Logger.error;

import org.json.JSONObject;
/**
 * PMCStats takes in a PMC server post and retrieves the statistics
 * @author Seth
 */
public class PMCStats extends WebsiteStats {
	private static final String[] keys = {"title", "views", "author", "diamonds", "favorites", "views_today", 
		"server_votes", "comments", "server_score", "stats_updated", "url", "author_url"},
			values = {"title", "views", "author", "upvotes", "favorites", "v_today", "server_votes", "comments", "server_score", "stats_updated", "url", "author_url"};
	/**
	 * Create a new PMCStats with given server url 
	 * @throws MalformedURLException
	 */
	public PMCStats() {
		super("pmc");
	}

	@Override
	protected JSONObject update(URL url) {
		try {
			return new JSONObject(SiteReader.getHTML(new URL(url + "/stats/")));
		} catch (IOException e) {error(e.getMessage().replace("/stats/", ""));}
		return null;
	}
	
	public static void main(String[] args) {
		PMCStats s = new PMCStats();
		System.out.println(s.format("testing %title% testing %views%"));
	}

	@Override
	protected String[][] registerKeys() {
		return new String[][]{keys,values};
	}
}
