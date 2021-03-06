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
package net.foxgenesis.serverstats.signs;

import static net.foxgenesis.serverstats.Logger.error;
import static net.foxgenesis.serverstats.Logger.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;

import net.foxgenesis.helper.Marquee;
import net.foxgenesis.serverstats.ServerStats;

public final class SignData {
	/**
	 * SignData file. Where all SignData is stored
	 */
	private static File file;
	/**
	 * List of all SignData
	 */
	protected static final ArrayList<SignData> locs = new ArrayList<>();
	private static ServerStats plugin;
	private static boolean marquee;

	/**
	 * Add a Location
	 * 
	 * @param loc
	 *            - Location to add
	 * @param lines
	 *            - Sign lines to add
	 */
	protected static void add(Location loc, String[] lines) {
		locs.add(new SignData(loc, lines));
	}

	/**
	 * Check to see if a Location is already used
	 * 
	 * @param loc
	 *            - location to use
	 * @return if Location is in use
	 */
	protected static boolean contains(Location loc) {
		return locs.stream().anyMatch(a -> a.equals(loc));
	}

	private static String get(String line, String i, boolean s) {
		int s0 = line.indexOf(i) + i.length();
		int s1 = line.indexOf(s ? "}" : ",", s0 + 1);
		return line.substring(s0, s1);
	}

	/**
	 * Read and create all SignData
	 * 
	 * @param m
	 *            - Should use marquees
	 * @locks SignData file
	 */
	public static void init(ServerStats plugin, boolean m) {
		SignData.plugin = plugin;
		file = new File(plugin.getDataFolder().toString() + "/.signdata");
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
		marquee = m;
		synchronized (file) {
			try {
				Files.readAllLines(file.toPath()).stream().filter(line -> line.startsWith("Location")).forEach(line -> {
					SignData s = null;
					try {
						s = new SignData(line);
					} catch (Exception e) {
						log(line);
					}
					if (!locs.contains(s))
						locs.add(s);
				});
			} catch (IOException e) {
				error(e.getMessage());
			}
		}
	}

	/**
	 * Save all SignData
	 * 
	 * @locks SignData file
	 */
	public static void save() {
		if (file == null)
			return;
		synchronized (file) {
			if (locs.isEmpty())
				return;
			try (FileWriter writer = new FileWriter(file, false)) {
				for (SignData loc : locs) {
					writer.write(loc.toString());
					writer.write(System.lineSeparator());
				}
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ================================================================================

	private final Location loc;
	private final String[] lines;
	private final Marquee[] m;

	SignData(Location loc, String[] lines) {
		this.loc = loc;
		this.lines = lines;
		m = new Marquee[lines.length];
	}

	SignData(String line) {
		if (!line.startsWith("Location"))
			throw new NullPointerException("Invalid sign data! [" + line + "]");
		String worldName = get(line, "name=", true);
		double x = Double.parseDouble(get(line, "x=", false)), y = Double.parseDouble(get(line, "y=", false)),
				z = Double.parseDouble(get(line, "z=", false));
		loc = new Location(plugin.getServer().getWorld(worldName), x, y, z);
		lines = new String[] { get(line, ":0", false), get(line, ":1", false), get(line, ":2", false),
				get(line, ":3", false) };
		m = new Marquee[lines.length];
	}

	public String[] callback(String[] l) {
		String[] lines = Arrays.copyOf(l, l.length);
		String[] output = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			if (marquee && lines[i].length() > 16)
				if (m[i] == null)
					m[i] = new Marquee(lines[i], 16);
				else
					m[i].setString(lines[i]);
			if (m[i] == null)
				output[i] = lines[i];
			else
				output[i] = m[i].toString();
		}
		return output;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Location)
			return loc.equals(obj);
		else if (obj instanceof SignData)
			return toString().equals(obj.toString());
		return false;
	}

	/**
	 * Get the lines of the Sign
	 * 
	 * @return lines
	 */
	public String[] getLines() {
		return lines;
	}

	/**
	 * Get the Location of the Sign
	 * 
	 * @return Location
	 */
	public Location getLocation() {
		return loc;
	}

	@Override
	public String toString() {
		String output = loc.toString();
		for (int i = 0; i < lines.length; i++)
			output += ":" + i + (lines[i].equals("") ? " " : lines[i]) + ",";
		return output;
	}

	/**
	 * Update the Marquees
	 */
	public void update() {
		Arrays.asList(m).stream().filter(a -> a != null).forEach(a -> a.update());
	}
}
