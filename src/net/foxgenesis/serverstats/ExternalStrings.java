package net.foxgenesis.serverstats;

import static net.foxgenesis.serverstats.Logger.error;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

public final class ExternalStrings {
	private static final HashMap<String,String> strings = new HashMap<>();
	private static boolean hasLang = false;

	public static boolean init(String lang) {
		Scanner scanner = null;
		try {
			InputStream is = ServerStats.instance.getResource(lang + ".lang");
			scanner = new Scanner(is,"UTF-8");
			String line = "";
			while(scanner.hasNextLine())
				if((line = scanner.nextLine().trim()) != null) {
					if(line.startsWith("#") || line.isEmpty())
						continue;
					String[] d = line.split(":",2);
					if(d.length < 2) {
						error("Invalid lang file format [" + line  + "]");
						return false;
					}
					strings.put(d[0].trim(), d[1].trim());
				}
			return hasLang = true;
		} catch(NullPointerException e) {
			error(lang + ".lang is not supported or was not found!");
			e.printStackTrace();
		} finally {
			if(scanner != null)
				scanner.close();
		}
		return false;
	}

	public static String get(String key) {
		if(hasLang)
			return strings.get(key);
		return "";
	}

	public static boolean hasLang() {
		return hasLang;
	}
}
