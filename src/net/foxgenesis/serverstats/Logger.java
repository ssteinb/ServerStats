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
