package net.foxgenesis.serverstats;

import static net.foxgenesis.serverstats.Logger.error;
import static net.foxgenesis.serverstats.Logger.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import net.foxgenesis.helper.ArrayHelper;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteStreams;

public class ServerStats extends JavaPlugin {

	private WebsiteStats[] stats;
	private ArrayList<Player> players = new ArrayList<>();
	protected static ServerStats instance;
	@Override
	public void onEnable() {
		instance = this;
		try {
			log("Loading config...");
			getConfig().load(loadResource("config.yml"));
		} catch (IOException | InvalidConfigurationException e) {
			error(e.getMessage());
			log("Unloading...");
			getPluginLoader().disablePlugin(this);
			return;
		}
		stats = new WebsiteStats[]{new PMCStats()};
		for(WebsiteStats a: stats) {
			log("Creating stats configuration for " + a.getName() + "...");
			a.loadSettings(getConfig());
		}
		log("Creating sign listener...");
		getServer().getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void onPlayerClickSign(PlayerInteractEvent event){
				Player player = event.getPlayer();
				if(players.contains(player) && event.getClickedBlock().getType() == Material.SIGN || 
						event.getClickedBlock().getType() == Material.SIGN_POST ||event.getClickedBlock().getType() == Material.WALL_SIGN){
					if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
						Sign sign = (Sign) event.getClickedBlock().getState();
						if(!SignData.locs.contains(sign.getLocation())) {
							log("Adding sign for: " + sign.getLocation());
							SignData.add(sign.getLocation(),sign.getLines());
						}
					}
				}
			}
		}, this);
		log("Loading sign data...");
		SignData.file = new File(getDataFolder().toString() + "/signs.sd");
		SignData.init();
		log("Enabled!");
		getServer().getScheduler().runTaskTimer(this, new Runnable() {

			@Override
			public void run() {
				Location[] remove = {};
				for(int j=0; j<SignData.locs.size(); j++) {
					Location a = SignData.locs.get(j);
					World w = a.getWorld();
					Block b = w.getBlockAt(a);
					if(b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN) {
						Sign s = (Sign)b.getState();
						for(WebsiteStats d: stats)
							for(int i=0; i<s.getLines().length; i++)
								s.setLine(i,d.format(SignData.lines.get(a)[i]));
						s.update(true);
					}
					else
						remove = ArrayHelper.append(remove,a);
				}
				for(Location a:remove) {
					log("removing location = " + a);
					SignData.locs.remove(a);
				}
				SignData.save();
			}

		}, getConfig().getLong("settings.cache.expiration-time")/20, getConfig().getLong("settings.cache.expiration-time")/20);
	}

	@Override
	public void onDisable() {
		stats = null;
		instance = null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("serverstats")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "--------[ ServerStats Help ]--------");
				for(WebsiteStats a: stats)
					sender.sendMessage(ChatColor.GOLD + "/serverstats " + a.getName());
				return true;
			} else if(args[0].equalsIgnoreCase("listen")) {
				if(players.contains((Player)sender))
					players.remove((Player)sender);
				else
					players.add((Player)sender);
				sender.sendMessage(ChatColor.GOLD + "Listen = " + players.contains((Player)sender));
				return true;
			} else
				for(WebsiteStats a: stats)
					if(args[0].toLowerCase().equalsIgnoreCase(a.getName())) {
						a.display(sender);
						return true;
					}
		}
		return false;
	}

	private File loadResource(String resource) {
		File folder = getDataFolder();
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
				try (InputStream in = getResource(resource);
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