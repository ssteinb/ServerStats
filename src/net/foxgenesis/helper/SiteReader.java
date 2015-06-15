package net.foxgenesis.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public final class SiteReader {

	/**
	 * Get a site's HTML
	 *
	 * @param url
	 *            - site's url
	 * @return site's HTML
	 * @throws IOException
	 */
	public static String getHTML(final URL url) throws IOException {
		final BufferedReader in = getStream(url);
		String output = "";
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			output += inputLine;
		in.close();
		return output;
	}

	/**
	 * Get a sites HTML and store it by line
	 *
	 * @param url
	 *            - site url
	 * @return site's HTML by line
	 * @throws IOException
	 */
	public static String[] getHTMLLines(final URL url) throws IOException {
		final BufferedReader in = getStream(url);
		final ArrayList<String> output = new ArrayList<String>();
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			output.add(inputLine);
		in.close();
		return output.toArray(new String[] {});
	}

	private static BufferedReader getStream(final URL url) throws IOException {
		final URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-Agent", "X-ServerStats");
		connection.setReadTimeout(5000);
		return new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
	}
}
