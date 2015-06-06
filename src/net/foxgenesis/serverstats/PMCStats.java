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
public class PMCStats extends WebsiteStats{
	/**
	 * Create a new PMCStats with given server url 
	 * @param s - server url (i.e. http://www.planetminecraft.com/server/-hunger-games-free-to-play-no-whitelist-heaps-of-players/)
	 * @throws MalformedURLException
	 */
	public PMCStats(String s) {
		super(s + "/stats/");
	}

	@Override
	protected JSONObject update(URL url) {
		try {
			return new JSONObject(SiteReader.getHTML(url));
		} catch (IOException e) {error(e.getMessage().replace("/stats/", ""));}
		return null;
	}


	@Override
	protected String getShorthandName() {
		return "pmc";
	}

	@Override
	protected String getStat(String key) {
		switch(key.toLowerCase()) {
		default:break;
		case "title":
			return get("title");
		case "views":
			return get("views");
		case "author":
			return get("author");
		case "upvotes":
			return get("upvotes");
		case "favorites":
			return get("favorites");
		case "views_today":
			return get("v_today");
		case "server_votes":
			return get("server_votes");
		case "comments":
			return get("comments");
		case "server_score":
			return get("server_score");
		case "stats_updated":
			return get("stats_updated");
		case "url":
			return get("url");
		case "author_url":
			return get("author_url");
		}
		return "Unsupported Tag";
	}
	
	public static void main(String[] args) {
		PMCStats s = new PMCStats("http://www.planetminecraft.com/server/-hunger-games-free-to-play-no-whitelist-heaps-of-players");
		System.out.println(s.format("testing %title% testing %views%"));
	}
}
