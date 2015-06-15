package net.foxgenesis.serverstats.websites;

import static net.foxgenesis.serverstats.Logger.error;

import java.io.IOException;
import java.net.URL;

import net.foxgenesis.helper.SiteReader;

import org.json.JSONObject;

public class CustomJSON extends WebsiteStats {

	public CustomJSON() {
		super("custom", '~');
	}

	@Override
	protected String[][] registerTags() {
		return new String[][] {};
	}

	@Override
	protected JSONObject update(final URL url) {
		try {
			final JSONObject j = new JSONObject(SiteReader.getHTML(url));
			useNewTags(new String[][] {j.keySet().toArray(new String[]{}),
					 j.keySet().toArray(new String[]{}) });
			return j;
		} catch (final IOException e) {
			error(e.getMessage());
		}
		return null;
	}
}
