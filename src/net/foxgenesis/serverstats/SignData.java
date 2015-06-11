package net.foxgenesis.serverstats;

import static net.foxgenesis.serverstats.Logger.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;

public final class SignData {
	protected static File file;
	protected static ArrayList<Location> locs = new ArrayList<>();
	protected static HashMap<Location, String[]> lines = new HashMap<>();

	protected static void add(Location loc, String[] lines) {
		SignData.lines.put(loc,lines);
		locs.add(loc);
	}

	protected static void save() {
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
		try(FileWriter writer = new FileWriter(file, false)) {
			for(Location loc: locs) {
				writer.write(loc.toString());
				String[] l = lines.get(loc);
				for(int i=0; i<l.length; i++)
					writer.write(":" + i + "=" + l[i] + ",");
				writer.write(System.lineSeparator());
			}
			writer.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	protected static void init() {
		try (BufferedReader r = new BufferedReader(new FileReader(file))) {
			String line = "";
			while((line = r.readLine()) != null) {
				if(!line.startsWith("Location"))
					continue;
				String worldName = get(line,"name=",true);
				double x = Double.parseDouble(get(line,"x=", false)),
						y = Double.parseDouble(get(line,"y=", false)),
						z = Double.parseDouble(get(line,"z=", false));
				Location l = new Location(ServerStats.instance.getServer().getWorld(worldName), x, y, z);
				if(locs.contains(l))
					continue;
				log("Creating sign loc from " + worldName + " " + x + " " + y + " " + z);
				locs.add(l);
				String[] e = {get(line, ":0", false),get(line, ":1", false),
						get(line, ":2", false),get(line, ":3", false)};
				lines.put(l,e);
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
}
