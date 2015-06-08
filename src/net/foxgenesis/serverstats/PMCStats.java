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
	 * @param s - server url (i.e. http://www.planetminecraft.com/server/-hunger-games-free-to-play-no-whitelist-heaps-of-players/)
	 * @throws MalformedURLException
	 */
	public PMCStats(String url) {
		super(url + "/stats/", "pmc", keys, values);
	}

	@Override
	protected JSONObject update(URL url) {
		try {
			return new JSONObject(SiteReader.getHTML(url));
		} catch (IOException e) {error(e.getMessage().replace("/stats/", ""));}
		return null;
	}
	
	public static void main(String[] args) {
		PMCStats s = new PMCStats("http://www.planetminecraft.com/server/-hunger-games-free-to-play-no-whitelist-heaps-of-players");
		System.out.println(s.format("testing %title% testing %views%"));
	}
}
