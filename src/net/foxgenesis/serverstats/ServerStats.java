package net.foxgenesis.serverstats;

import static net.foxgenesis.serverstats.Logger.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteStreams;

public class ServerStats extends JavaPlugin {

	private PMCStats pmc;
	
	@Override
	public void onEnable() {
		try {
			log("Loading config...");
			getConfig().load(loadResource(this,"config.yml"));
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
			log("Unloading...");
			this.getPluginLoader().disablePlugin(this);
			return;
		}
		
		log("Creating PMC Stats loader...");
		pmc = new PMCStats(getConfig().getString("settings.pmc.url"));
		pmc.loadSettings(getConfig());
		log("Enabled!");
	}

	@Override
	public void onDisable() {
		pmc = null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("serverstats")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "--------[ ServerStats Help ]--------");
				sender.sendMessage(ChatColor.GOLD + "/serverstats pmc");
				sender.sendMessage(ChatColor.GOLD + "/serverstats minestatus");
				sender.sendMessage(ChatColor.GOLD + "/serverstats mcsl");
				sender.sendMessage(ChatColor.GOLD + "/serverstats minecraft-mp");
				sender.sendMessage(ChatColor.GOLD + "/serverstats all");
				return true;
			} else 
				switch(args[0].toLowerCase()) {
				default: return false;
				case "pmc": pmc.display(sender); return true;
				}
		}
		return false;
	}

	private static File loadResource(Plugin plugin, String resource) {
		File folder = plugin.getDataFolder();
		if (!folder.exists())
			folder.mkdir();
		else {
			File f = new File(folder + resource);
			if(f.exists())
				return f;
		}
		File resourceFile = new File(folder, resource);
		try {
			if (!resourceFile.exists()) {
				resourceFile.createNewFile();
				try (InputStream in = plugin.getResource(resource);
						OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resourceFile;
	}
}