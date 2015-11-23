/**
    Copyright (C) 2015  FoxGenesis

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.foxgenesis.serverstats.websites;

import static net.foxgenesis.helper.StringHelper.extract;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import net.foxgenesis.helper.ArrayHelper;
import net.foxgenesis.helper.SiteReader;

import org.json.JSONObject;

/**
 * PMCStats takes in a MCSL server post and retrieves the statistics
 *
 * @author fox_news
 */
public class MinecraftServerListStats extends WebsiteStats {
	private static final String[] keys = { "title", "votes_month", "votes" },
			values = { "title", "votes_month", "votes" };

	private static String[] getTable(final URL url) {
		String[] data = {};
		boolean foundTable = false;
		try {
			for (final String a : SiteReader.getHTMLLines(url)) {
				if (foundTable)
					if (a.contains("/table"))
						return ArrayHelper.rest(ArrayHelper.merge(data).replaceAll("<tr>", "").split("</tr>"));
					else
						data = ArrayHelper.append(data, a);
				if (!foundTable && a.startsWith("<table ") && a.contains("serverdata"))
					foundTable = true;
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Create a new MinecraftServerListStats with given server url
	 *
	 * @throws MalformedURLException
	 */
	public MinecraftServerListStats() {
		super("mcsl", '@');
	}

	@Override
	protected String[][] registerTags() {
		return new String[][] { keys, values };
	}

	@Override
	protected JSONObject update(final URL url) {
		final JSONObject j = new JSONObject();
		final String data[] = getTable(url);
		j.put("title", extract(data[0], ">", "<"));
		j.put("votes_month", extract(data[10], "<td>", "Votes in"));
		j.put("votes", extract(data[10], "<span class=\"votes\">", "</span>"));
		return j;
	}
}
