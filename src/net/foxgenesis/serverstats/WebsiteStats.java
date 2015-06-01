package net.foxgenesis.serverstats;

import java.net.URL;

import org.json.JSONObject;

public abstract class WebsiteStats {
	private final URL url;
	private long lastUpdate = -1;
	private long updateTime = 60000;
	private JSONObject json;

	public WebsiteStats(URL url) {
		this.url = url;
		update(url);
	}
	
	/**
	 * Check if the data object is null
	 * @return if data object is not null
	 */
	protected boolean hasData() {
		return json != null;
	}
	
	protected JSONObject getData() {
		return getJSON();
	}
	
	/**
	 * Set the cache expiration time
	 * @param time - time until expiration
	 */
	public void setUpdateTime(long time) {
		this.updateTime = time;
	}

	private JSONObject getJSON() {
		if(getNewStats()) {
			json = update(url);
			lastUpdate = System.currentTimeMillis();
		}
		else
			System.out.println("[ServerStats API]: using old data");
		return json;
	}

	private boolean getNewStats() {
		return lastUpdate == -1 || (System.currentTimeMillis() - lastUpdate) >= updateTime;
	}

	protected abstract JSONObject update(URL url);
}
