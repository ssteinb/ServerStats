package net.foxgenesis.serverstats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public final class SiteReader {

	/**
	 * Get a site's HTML
	 * @param url - site's url
	 * @return site's HTML
	 * @throws IOException
	 */
	public static String getHTML(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36"); 
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String output = "";
		String inputLine;
		while((inputLine = in.readLine()) != null) 
			output+=inputLine;
		in.close();
		return output;
	}
	
	/**
	 * Get a sites HTML and store it by line
	 * @param url - site url
	 * @return site's HTML by line
	 * @throws IOException
	 */
	public static String[] getHTMLLines(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36"); 
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		ArrayList<String> output = new ArrayList<>();
		String inputLine;
		while((inputLine = in.readLine()) != null) 
			output.add(inputLine);
		in.close();
		return output.toArray(new String[]{});
	}
}