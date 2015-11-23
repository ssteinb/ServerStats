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
package net.foxgenesis.helper;

public final class StringHelper {
	public static int countChar(char[] array, char c) {
		if (array == null || array.length == 0)
			return 0;
		return countChar(ArrayHelper.rest(array), c) + (array[0] == c ? 1 : 0);
	}

	public static int countChar(String line, char c) {
		return countChar(line.toCharArray(), c);
	}

	public static String extract(String line, String start, String end) {
		final int s0 = line.indexOf(start) + start.length();
		final int s1 = line.indexOf(end, s0 + 1);
		return line.substring(s0, s1);
	}
}
