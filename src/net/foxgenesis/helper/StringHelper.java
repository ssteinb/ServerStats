package net.foxgenesis.helper;

public final class StringHelper {
	public static int countChar(final char[] array, final char c) {
		if (array == null || array.length == 0)
			return 0;
		return countChar(ArrayHelper.rest(array), c) + (array[0] == c ? 1 : 0);
	}

	public static int countChar(final String line, final char c) {
		return countChar(line.toCharArray(), c);
	}

	public static String extract(final String line, final String start,
			final String end) {
		final int s0 = line.indexOf(start) + start.length();
		final int s1 = line.indexOf(end, s0 + 1);
		return line.substring(s0, s1);
	}
}
