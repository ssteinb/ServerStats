package net.foxgenesis.serverstats;

import static net.foxgenesis.serverstats.Logger.error;
import static net.foxgenesis.serverstats.Logger.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import net.foxgenesis.helper.Marquee;

import org.bukkit.Location;

public final class SignData {
	protected static File file;
	protected static ArrayList<SignData> locs = new ArrayList<>();
	private static boolean marquee;

	protected static void add(Location loc, String[] lines) {
		locs.add(new SignData(loc,lines));
	}

	protected static synchronized void save() {
		if(locs.isEmpty())
			return;
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
		try(FileWriter writer = new FileWriter(file, false)) {
			for(SignData loc: locs) {
				writer.write(loc.toString());
				writer.write(System.lineSeparator());
			}
			writer.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	protected static void init(boolean m) {
		marquee = m;
		try (BufferedReader r = new BufferedReader(new FileReader(file))) {
			String line = "";
			while((line = r.readLine()) != null) {
				try {
					if(!line.startsWith("Location"))
						continue;
					SignData s = null;
					try {
						s = new SignData(line);
					} catch(Exception e) {log(line);}
					if(!locs.contains(s))
						locs.add(s);
				} catch(NullPointerException e) {
					error(e.getMessage());
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static String get(String line, String i, boolean s) {
		int s0 = line.indexOf(i) + i.length();
		int s1 = line.indexOf(s?"}":",", s0 + 1);
		return line.substring(s0,s1);
	}

	// ================================================================================

	private Location loc;
	private String[] lines;
	private Marquee[] m;
	
	SignData(Location loc, String[] lines) {
		this.loc = loc;
		this.lines = lines;
		m = new Marquee[lines.length];
	}

	SignData(String line) {
		if(!line.startsWith("Location"))
			throw new NullPointerException("Invalid sign data! [" + line + "]");
		String worldName = get(line,"name=",true);
		double x = Double.parseDouble(get(line,"x=", false)),
				y = Double.parseDouble(get(line,"y=", false)),
				z = Double.parseDouble(get(line,"z=", false));
		loc = new Location(ServerStats.instance.getServer().getWorld(worldName), x, y, z);
		lines = new String[]{get(line, ":0", false),get(line, ":1", false),
				get(line, ":2", false),get(line, ":3", false)};
		m = new Marquee[lines.length];
	}

	/**
	 * Get the lines of the Sign
	 * @return lines
	 */
	public String[] getLines() {
		return lines;
	}

	/**
	 * Get the Location of the Sign
	 * @return Location
	 */
	public Location getLocation() {
		return loc;
	}

	/**
	 * Update the Marquees
	 */
	public void update() {
		for(Marquee a: m)
			if(a != null)
				a.update();
	}

	public String[] callback(String[] l) {
		String[] lines = Arrays.copyOf(l,l.length);
		String[] output = new String[lines.length];
		for(int i=0; i<lines.length; i++) {
			if(marquee && lines[i].length() > 16)
				if(m[i] == null)
					m[i] = new Marquee(lines[i],16);
				else
					m[i].setString(lines[i]);
			if( m[i] == null)
				output[i] = lines[i];
			else
				output[i] = m[i].toString();
		}
		return output;
	}

	@Override
	public String toString() {
		String output = loc.toString();
		for(int i=0; i<lines.length; i++)
			output += ":" + i + (lines[i].equals("")?" ":lines[i]) + ",";
		return output;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof SignData))
			return false;
		else {
			SignData s = (SignData) obj;
			if(s.loc == loc)
				return true;
			else
				return equals(s.lines,lines);
		}
	}

	private static boolean equals(String[] a1, String[] a2) {
		if(a1.length != a2.length)
			return false;
		for(int i=0; i<a1.length; i++)
			if(a1[i].equalsIgnoreCase(a2[i]))
				return true;
		return false;
	}
}
