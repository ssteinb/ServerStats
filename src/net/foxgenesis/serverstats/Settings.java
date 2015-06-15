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
