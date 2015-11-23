package net.foxgenesis.serverstats;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import command.AbstractCommand;
import net.foxgenesis.serverstats.websites.WebsiteStats;

public class ServerStatsCommand extends AbstractCommand {
	private final WebsiteStats[] stats;
	private static final ArrayList<Player> players = new ArrayList<>();

	public ServerStatsCommand(WebsiteStats[] stats) {
		super("serverstats", "", "Server Stats commands", Arrays.asList(new String[] { "ss" }));
		ArrayList<WebsiteStats> t = new ArrayList<>();
		Arrays.asList(stats).stream().filter(a -> a.isEnabled()).forEach(t::add);
		this.stats = t.toArray(new WebsiteStats[] {});
		this.usage = createUsage(stats, "server-stats-listen", "server-stats-update");
		register();
	}

	private String createUsage(WebsiteStats[] s, String... toAdd) {
		String output = "<";
		for (String a : toAdd)
			output += ExternalStrings.get(a) + " | ";
		for (WebsiteStats a : s)
			output += a.getName() + " | ";
		return output.substring(0, output.length() - 3) + ">";
	}

	public static ArrayList<Player> getPlayers() {
		return players;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("Usage: serverstats " + usage);
			return true;
		} else if (args[0].equalsIgnoreCase(ExternalStrings.get("server-stats-listen"))) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + ExternalStrings.get("console-rest"));
				return true;
			}
			if (players.contains(sender))
				players.remove(sender);
			else
				players.add((Player) sender);
			sender.sendMessage(ChatColor.GOLD + ExternalStrings.get("server-stats-listen-toggle")
					+ ExternalStrings.get(players.contains(sender) + ""));
			return true;
		} else if (args[0].equalsIgnoreCase(ExternalStrings.get("server-stats-update"))) {
			sender.sendMessage(ChatColor.GOLD + ExternalStrings.get("server-stats-forceupdate-notify"));
			Arrays.asList(stats).forEach(a -> a.forceUpdate());
			sender.sendMessage(ChatColor.GREEN + ExternalStrings.get("server-stats-forceupdate-complete"));
			return true;
		} else {
			if (Arrays.asList(stats).stream().anyMatch(a -> args[0].toLowerCase().equalsIgnoreCase(a.getName()))) {
				Arrays.asList(stats).stream().filter(a -> args[0].toLowerCase().equalsIgnoreCase(a.getName()))
						.forEach(a -> a.display(sender));
			} else
				sender.sendMessage("Usage: serverstats " + usage);
			return true;
		}
	}
}
