package net.foxgenesis.helper;

import java.util.Arrays;

public final class ArrayHelper {

	public static <T> T[] append(final T[] array, final T element) {
		final T[] d = Arrays.copyOf(array, array.length + 1);
		d[d.length - 1] = element;
		return d;
	}

	public static String merge(final String[] array) {
		String output = "";
		for (final String a : array)
			output += a;
		return output;
	}

	public static char[] rest(final char[] array) {
		return array.length > 1 ? Arrays.copyOfRange(array, 1, array.length)
				: null;
	}

	public static <T> T[] rest(final T[] array) {
		return array.length > 1 ? Arrays.copyOfRange(array, 1, array.length)
				: null;
	}
}
