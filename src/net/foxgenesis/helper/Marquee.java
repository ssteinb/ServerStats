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
	public Marquee(String s, int n) {
		if (s == null || n < 1)
			throw new IllegalArgumentException("Null string or n < 1");
		b = new StringBuilder(n);
		for (int i = 0; i < n; i++)
			b.append(" ");
		setString(s);
		this.n = n;
	}

	public void setString(String s) {
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
