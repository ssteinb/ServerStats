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
package net.foxgenesis.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONObject;

public final class SiteReader {

	/**
	 * Get a site's HTML
	 *
	 * @param url
	 *            - site's url
	 * @return site's HTML
	 * @throws IOException
	 */
	public static String getHTML(URL url) throws IOException {
		BufferedReader in = getStream(url);
		String output = "";
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			output += inputLine;
		in.close();
		return output;
	}
	
	/**
	 * Read a page containing JSON.
	 * @param url - {@link URL} to use
	 * @return A {@link JSONObject} of the page
	 * @throws IOException Normal IO issues
	 */
	public static JSONObject readJSON(URL url) throws IOException {
		return new JSONObject(SiteReader.getHTML(url));
	}

	/**
	 * Get a sites HTML and store it by line
	 *
	 * @param url
	 *            - site url
	 * @return site's HTML by line
	 * @throws IOException
	 */
	public static String[] getHTMLLines(URL url) throws IOException {
		BufferedReader in = getStream(url);
		ArrayList<String> output = new ArrayList<String>();
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			output.add(inputLine);
		in.close();
		return output.toArray(new String[] {});
	}

	private static BufferedReader getStream(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-Agent", "X-ServerStats");
		connection.setReadTimeout(5000);
		return new BufferedReader(new InputStreamReader(connection.getInputStream()));
	}
}
