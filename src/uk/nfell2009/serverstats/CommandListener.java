package uk.nfell2009.serverstats;

import net.foxgenesis.serverstats.PMCStats;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
//import net.foxgenesis.serverstats.PMCStats;

public class CommandListener implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("serverstats")) {
			if (args[0].isEmpty()) {
				sender.sendMessage(ChatColor.GOLD + "--------[ ServerStats Help ]--------");
				sender.sendMessage(ChatColor.GOLD + "/serverstats pmc");
				sender.sendMessage(ChatColor.GOLD + "/serverstats minestatus");
				sender.sendMessage(ChatColor.GOLD + "/serverstats mcsl");
				sender.sendMessage(ChatColor.GOLD + "/serverstats minecraft-mp");
				sender.sendMessage(ChatColor.GOLD + "/serverstats all");
				return false;
			} else if (args[0].equalsIgnoreCase("pmc")) {
				PMCStats stats = ServerStats.getInstance().getPMC();
				sender.sendMessage(ChatColor.GOLD + "[" + stats.getTitle() + " - " + ChatColor.GREEN + "PMC" + ChatColor.GOLD + "]");
				sender.sendMessage(ChatColor.GOLD + "" + stats.getViews() + ChatColor.WHITE + " total views | " + ChatColor.GOLD + "" + stats.getViewsToday() + ChatColor.WHITE + " today");
				sender.sendMessage(ChatColor.GOLD + "Todays views: " + stats.getViewsToday());
				sender.sendMessage(ChatColor.GOLD + "Diamonds: " + stats.getUpvotes());
				sender.sendMessage(ChatColor.GOLD + "Server votes: " + stats.getServerVotes());
				sender.sendMessage(ChatColor.GOLD + "Server score: " + stats.getServerScore());
				sender.sendMessage(ChatColor.GOLD + "Favourites: " + stats.getFavorites());
				sender.sendMessage(ChatColor.GOLD + "Comments: " + stats.getComments());
				sender.sendMessage(ChatColor.GOLD + "Posted by: " + stats.getAuthor());
				return true;
			}
		}
		return false;
	}
	
}