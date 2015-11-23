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

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.foxgenesis.serverstats.ExternalStrings;
import net.foxgenesis.serverstats.ServerStatsCommand;

public class SignListener implements Listener {

	@EventHandler
	public void onPlayerClickSign(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (ServerStatsCommand.getPlayers().contains(player) && (event.getClickedBlock().getType() == Material.SIGN
				|| event.getClickedBlock().getType() == Material.SIGN_POST
				|| event.getClickedBlock().getType() == Material.WALL_SIGN)) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				final Sign sign = (Sign) event.getClickedBlock().getState();
				if (!SignData.contains(sign.getLocation())) {
					log(ExternalStrings.get("server-stats-listen-add").replace("%loc%", sign.getLocation().toString()));
					player.sendMessage(ChatColor.GOLD + ExternalStrings.get("server-stats-listen-add").replace("%loc%",
							sign.getLocation().toString()));
					SignData.add(sign.getLocation(), sign.getLines());
				} else
					player.sendMessage(ChatColor.RED + ExternalStrings.get("server-stats-listen-noadd"));
			}
		}
	}
}
