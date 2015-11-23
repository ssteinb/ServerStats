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

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import net.foxgenesis.serverstats.ExternalStrings;
import net.foxgenesis.serverstats.ServerStatsAPI;

public class SignUpdater implements Runnable {

	private long avgTime;
	private final boolean signUpdateTime;
	public ArrayList<SignData> toRemove = new ArrayList<>();
	private Thread thread = new Thread(() -> {
		while(true) {
			synchronized (SignData.locs) {
				toRemove.forEach(SignData.locs::remove);
				toRemove.clear();
			}
			try {Thread.sleep(1000);} catch (InterruptedException e){}
		}
	}, "Sign Remover Thread");

	public SignUpdater(boolean printTime) {
		signUpdateTime = printTime;
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public void run() {
		synchronized (SignData.locs) {
			long t = System.nanoTime();
			SignData.locs.forEach(a -> {
				a.update();
				final World w = a.getLocation().getWorld();
				final Block b = w.getBlockAt(a.getLocation());
				if (b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST
						|| b.getType() == Material.WALL_SIGN) {
					final Sign s = (Sign) b.getState();
					final String[] g = a.callback(ServerStatsAPI.format(a.getLines().clone()));
					for (int i = 0; i < g.length; i++)
						s.setLine(i, g[i]);
					s.update(true);
				} else
					toRemove.add(a);
			});
			if (signUpdateTime) {
				avgTime = ((System.nanoTime() - t) + avgTime) / 2;
				log(ExternalStrings.get("sign-update-time-notify") + " " + avgTime + " "
						+ ExternalStrings.get("sign-update-time-nanos"));
			}
		}
	}
}
