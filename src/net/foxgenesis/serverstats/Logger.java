package net.foxgenesis.serverstats;


public final class Logger {
	private static java.util.logging.Logger l;
	
	protected static void init(java.util.logging.Logger logger) {
		l = logger;
	}
	
	protected static void error(String msg) {
		l.severe(msg);
	}
	
	protected static void log(String msg) {
		l.info(msg);
	}
}
