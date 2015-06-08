package net.foxgenesis.serverstats;

public final class Logger {
	protected static void error(String msg) {
		System.err.println("[ServerStats] ERROR: " + msg);
	}
	
	protected static void log(String msg) {
		System.out.println("[ServerStats] " + msg);
	}
}
