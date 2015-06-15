package net.foxgenesis.serverstats;

public final class Settings {
	public static final class Cache {
		public static final String EXPIRATION_TIME = "settings.cache.expiration-time";
	}

	public static final class Signs {
		public static final String LOG_UPDATE_TIME = "settings.signs.log-update-time";
		public static final String ALLOW_MARQUEE = "settings.signs.allow-marquee";
		public static final String UPDATE_TIME = "settings.signs.update-time";
	}

	public static final class Website {
		public static final String display(final String name) {
			return "settings." + name + ".display";
		}

		public static final String enabled(final String name) {
			return "settings." + name + ".enable";
		}

		public static final String url(final String name) {
			return "settings." + name + ".url";
		}
	}
}
