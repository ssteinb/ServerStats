package net.foxgenesis.helper;

import java.util.Arrays;

public final class ArrayHelper {
	public static <T> T[] rest(T[] array) {
		return Arrays.copyOfRange(array, 1, array.length - 1);
	}
	
	public static <T> T[] append(T[] array, T element) {
		T[] d = Arrays.copyOf(array, array.length + 1);
		d[d.length - 1] = element;
		return d;
	}
}
