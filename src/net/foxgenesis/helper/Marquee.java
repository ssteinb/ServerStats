package net.foxgenesis.helper;

/**
 * Helper class to create text marquees
 *
 * @author fox_news
 */
public class Marquee {
	private String s;
	private final int n;
	private int index;
	private String output;
	private final StringBuilder b;

	/**
	 * Create a new Marquee with given String and capacity
	 *
	 * @param s
	 *            - String to use
	 * @param n
	 *            - capacity of the Marquee
	 */
	public Marquee(final String s, final int n) {
		if (s == null || n < 1)
			throw new IllegalArgumentException("Null string or n < 1");
		b = new StringBuilder(n);
		for (int i = 0; i < n; i++)
			b.append(" ");
		setString(s);
		this.n = n;
	}

	public void setString(final String s) {
		this.s = b + s + b;
	}

	@Override
	public String toString() {
		return output;
	}

	/**
	 * Update the marquee position
	 */
	public void update() {
		if (++index > s.length() - n)
			index = 0;
		output = s.substring(index, index + n);
	}
}
