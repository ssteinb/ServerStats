package net.foxgenesis.serverstats;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
	public PMCStats(String s) throws MalformedURLException {
		super(new URL(s + "/stats/"));
	}

	/**
	 * Get page views
	 * @return page views
	 */
	public int getViews() {
		if(ifJSON())
			return getData().getInt("views");
		return -1;
	}

	/**
	 * Get page title
	 * @return page title
	 */
	public String getTitle() {
		if(ifJSON())
			return getData().getString("title");
		return "null";
	}

	/**
	 * Get author of page
	 * @return author
	 */
	public String getAuthor() {
		if(ifJSON())
			return getData().getString("author");
		return "null";
	}

	/**
	 * Get the amount of views today
	 * @return today's views
	 */
	public int getViewsToday() {
		if(ifJSON())
			return getData().getInt("v_today");
		return -1;
	}

	/**
	 * Get the amount of upvotes
	 * @return upvote count
	 */
	public int getUpvotes() {
		if(ifJSON())
			return getData().getInt("upvotes");
		return -1;
	}

	/**
	 * Get the amount of favorites
	 * @return favorite count
	 */
	public int getFavorites() {
		if(ifJSON())
			return getData().getInt("favorites");
		return -1;
	}

	/**
	 * Get the amount of comments
	 * @return comment count
	 */
	public int getComments() {
		if(ifJSON())
			return getData().getInt("comments");
		return -1;
	}

	/**
	 * Get the amount of server votes
	 * @return server vote count
	 */
	public int getServerVotes() {
		if(ifJSON())
			return getData().getInt("server_votes");
		return -1;
	}

	/**
	 * Get the server's score
	 * @return server score
	 */
	public int getServerScore() {
		if(ifJSON())
			return getData().getInt("server_score");
		return -1;
	}

	@Override
	protected JSONObject update(URL url) {
		try {
			return new JSONObject(SiteReader.getHTML(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean ifJSON() {
		if(hasData())
			return true;
		System.err.println("[ServerStats API]: PMC JSON data is null");
		return false;
	}
}
