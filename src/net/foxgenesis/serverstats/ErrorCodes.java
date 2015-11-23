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

public final class ErrorCodes {
	public static final int FORMAT = 0;
	public static final int INVALID_URL = 1;
	public static final int INVALID_LANG = 2;

	public static final void error(final int error) {
		Logger.error(ExternalStrings.get("error") + error);
	}

	public static final String errorText(final int error) {
		return ExternalStrings.get("error") + error;
	}
}
