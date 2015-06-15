package net.foxgenesis.serverstats;

public final class Logger {
	private static java.util.logging.Logger l;

	public static void error(final String msg) {
		l.severe(msg);
	}

	protected static void init(final java.util.logging.Logger logger) {
		l = logger;
	}

	public static void log(final String msg) {
		l.info(msg);
	}
}
