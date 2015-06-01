package net.foxgenesis.serverstats;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;
/**
 * PMCStats takes in a PMC server post and retrieves the statistics
 * @author Seth
 */
public class PMCStats {
	private final URL url;
	private long lastUpdate = -1;
	private long updateTime = 60000;
	private JSONObject json;
	
	/**
	 * Create a new PMCStats with given server url 
	 * @param s - server url (i.e. http://www.planetminecraft.com/server/-hunger-games-free-to-play-no-whitelist-heaps-of-players/)
	 * @throws MalformedURLException
	 */
	public PMCStats(String s) throws MalformedURLException {
		url = new URL(s + "/stats/");
		update();
	}
	
	/**
	 * Get page views
	 * @return page views
	 */
	public int getViews() {
		if(ifJSON())
			return getJSON().getInt("views");
		return -1;
	}
	
	/**
	 * Get page title
	 * @return page title
	 */
	public String getTitle() {
		if(ifJSON())
			return getJSON().getString("title");
		return "null";
	}
	
	/**
	 * Get author of page
	 * @return author
	 */
	public String getAuthor() {
		if(ifJSON())
			return getJSON().getString("author");
		return "null";
	}
	
	/**
	 * Get the amount of views today
	 * @return today's views
	 */
	public int getViewsToday() {
		if(ifJSON())
			return getJSON().getInt("v_today");
		return -1;
	}
	
	/**
	 * Get the amount of upvotes
	 * @return upvote count
	 */
	public int getUpvotes() {
		if(ifJSON())
			return getJSON().getInt("upvotes");
		return -1;
	}
	
	/**
	 * Get the amount of favorites
	 * @return favorite count
	 */
	public int getFavorites() {
		if(ifJSON())
			return getJSON().getInt("favorites");
		return -1;
	}
	
	/**
	 * Get the amount of comments
	 * @return comment count
	 */
	public int getComments() {
		if(ifJSON())
			return getJSON().getInt("comments");
		return -1;
	}
	
	/**
	 * Get the amount of server votes
	 * @return server vote count
	 */
	public int getServerVotes() {
		if(ifJSON())
			return getJSON().getInt("server_votes");
		return -1;
	}
	
	/**
	 * Get the server's score
	 * @return server score
	 */
	public int getServerScore() {
		if(ifJSON())
			return getJSON().getInt("server_score");
		return -1;
	}
	
	private JSONObject getJSON() {
		update();
		return json;
	}
	
	private void update() {
		if(getNewStats())
			try {
				json = new JSONObject(SiteReader.getHTML(url));
				lastUpdate = System.currentTimeMillis();
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
			System.out.println("[ServerStats API]: using old data");
	}
	
	private boolean getNewStats() {
		return lastUpdate == -1 || (System.currentTimeMillis() - lastUpdate) >= updateTime;
	}
	
	private boolean ifJSON() {
		if(json != null)
			return true;
		System.err.println("[ServerStats API]: PMC JSON data is null");
		return false;
	}
	
	/**
	 * Set the cache expiration time
	 * @param time - time until expiration
	 */
	public void setUpdateTime(long time) {
		this.updateTime = time;
	}
}
