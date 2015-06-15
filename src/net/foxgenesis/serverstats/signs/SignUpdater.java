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
package net.foxgenesis.serverstats.signs;

import static net.foxgenesis.serverstats.Logger.log;
import net.foxgenesis.helper.ArrayHelper;
import net.foxgenesis.serverstats.ExternalStrings;
import net.foxgenesis.serverstats.ServerStats;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class SignUpdater implements Runnable {

	private long avgTime;
	private final boolean signUpdateTime;

	public SignUpdater(final boolean printTime) {
		signUpdateTime = printTime;
	}

	@Override
	public void run() {
		final long t = System.nanoTime();
		SignData[] remove = {};
		for (final SignData a : SignData.locs) {
			a.update();
			final World w = a.getLocation().getWorld();
			final Block b = w.getBlockAt(a.getLocation());
			if (b.getType() == Material.SIGN
					|| b.getType() == Material.SIGN_POST
					|| b.getType() == Material.WALL_SIGN) {
				final Sign s = (Sign) b.getState();
				final String[] g = a.callback(ServerStats.format(a.getLines()
						.clone()));
				for (int i = 0; i < g.length; i++)
					s.setLine(i, g[i]);
				s.update(true);
			} else
				remove = ArrayHelper.append(remove, a);
		}
		for (final SignData a : remove) {
			log(ExternalStrings.get("signs-remove") + " " + a);
			SignData.locs.remove(a);
		}
		if (signUpdateTime) {
			avgTime = ((System.nanoTime() - t) + avgTime) / 2;
			log(ExternalStrings.get("sign-update-time-notify") + " " + avgTime
					+ " " + ExternalStrings.get("sign-update-time-nanos"));
		}
	}
}
