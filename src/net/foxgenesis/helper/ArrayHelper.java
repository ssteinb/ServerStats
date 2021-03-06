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

import java.util.Arrays;

public final class ArrayHelper {

	public static <T> T[] append(T[] array, T element) {
		T[] d = Arrays.copyOf(array, array.length + 1);
		d[d.length - 1] = element;
		return d;
	}

	public static String merge(String[] array) {
		return Arrays.asList(array).stream().reduce((a, b) -> a + b).get();
	}

	public static char[] rest(char[] array) {
		return array.length > 1 ? Arrays.copyOfRange(array, 1, array.length) : null;
	}

	public static <T> T[] rest(T[] array) {
		return array.length > 1 ? Arrays.copyOfRange(array, 1, array.length) : null;
	}
}
