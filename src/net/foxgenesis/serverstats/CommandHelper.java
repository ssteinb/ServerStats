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
package net.foxgenesis.serverstats;

import java.util.ArrayList;

import net.foxgenesis.serverstats.websites.WebsiteStats;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHelper {
	private final ArrayList<Player> players;
	private final WebsiteStats[] stats;

	public CommandHelper(final WebsiteStats[] stats) {
		this.stats = stats;
		players = new ArrayList<>();
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public boolean onCommand(final CommandSender sender, final Command cmd,
			final String label, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("serverstats")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GOLD
						+ ExternalStrings.get("command-header"));
				for (final WebsiteStats a : stats)
					if (a.isEnabled())
						sender.sendMessage(ChatColor.GOLD + "/serverstats "
								+ a.getName());
				return true;
			} else if (args[0].equalsIgnoreCase(ExternalStrings
					.get("server-stats-listen"))) {
				if (getPlayers().contains(sender))
					getPlayers().remove(sender);
				else
					getPlayers().add((Player) sender);
				sender.sendMessage(ChatColor.GOLD
						+ ExternalStrings.get("server-stats-listen-toggle")
						+ ExternalStrings.get(getPlayers().contains(sender)
								+ ""));
				return true;
			} else if (args[0].equalsIgnoreCase(ExternalStrings.get("update"))) {
				sender.sendMessage(ChatColor.GOLD
						+ ExternalStrings
								.get("server-stats-forceupdate-notify"));
				for (final WebsiteStats a : stats)
					a.forceUpdate();
				sender.sendMessage(ChatColor.GREEN
						+ ExternalStrings
								.get("server-stats-forceupdate-complete"));
				return true;
			} else
				for (final WebsiteStats a : stats)
					if (args[0].toLowerCase().equalsIgnoreCase(a.getName())) {
						a.display(sender);
						return true;
					}
		}
		return false;
	}
}
