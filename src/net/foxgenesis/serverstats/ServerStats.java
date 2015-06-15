package net.foxgenesis.serverstats;

import static net.foxgenesis.serverstats.Logger.error;
import static net.foxgenesis.serverstats.Logger.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.foxgenesis.serverstats.signs.SignData;
import net.foxgenesis.serverstats.signs.SignListener;
import net.foxgenesis.serverstats.signs.SignUpdater;
import net.foxgenesis.serverstats.websites.CustomJSON;
import net.foxgenesis.serverstats.websites.MinecraftServerListStats;
import net.foxgenesis.serverstats.websites.PMCStats;
import net.foxgenesis.serverstats.websites.WebsiteStats;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteStreams;

public class ServerStats extends JavaPlugin {

	public static ServerStats instance;

	/**
	 * Format an array of Strings with WebsiteStats
	 *
	 * @param lines
	 *            - lines to format
	 * @return formated Strings
	 */
	public static String[] format(final String[] lines) {
		for (final WebsiteStats a : ServerStats.instance.stats)
			a.format(lines);
		return lines;
	}

	private WebsiteStats[] stats;

	private CommandHelper helper;

	private File loadResource(final String resource) {
		final File folder = getDataFolder();
		if (!folder.exists())
			folder.mkdir();
		else {
			final File f = new File(folder + resource);
			if (f.exists())
				return f;
		}
		final File resourceFile = new File(folder, resource);
		try {
			if (!resourceFile.exists()) {
				resourceFile.createNewFile();
				try (InputStream in = getResource(resource);
						OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return resourceFile;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd,
			final String label, final String[] args) {
		return helper.onCommand(sender, cmd, label, args);
	}

	@Override
	public void onDisable() {
		log(ExternalStrings.get("sign-data-save"));
		SignData.save();
		log(ExternalStrings.get("removing-task"));
		getServer().getScheduler().cancelTasks(this);
		log(ExternalStrings.get("removing-access-data"));
		stats = null;
		instance = null;
	}

	@Override
	public void onEnable() {
		// init
		Logger.init(getLogger());
		instance = this;

		//load external strings
		if(!ExternalStrings.init(getConfig().getString("lang"))) {
			log("Unloading...");
			getPluginLoader().disablePlugin(this);
			return;
		}
		
		// Load configuration
		try {
			log(ExternalStrings.get("load-config"));
			getConfig().load(loadResource("config.yml"));
		} catch (IOException | InvalidConfigurationException e) {
			error(e.getMessage());
			log("Unloading...");
			getPluginLoader().disablePlugin(this);
			return;
		}

		// Create stat classes
		stats = new WebsiteStats[] { new PMCStats(),
				new MinecraftServerListStats(), new CustomJSON() };
		for (final WebsiteStats a : stats) {
			log(ExternalStrings.get("web-stat-create").replace("%name%", a.getName()));
			a.loadSettings(getConfig());
		}

		// create command listener
		helper = new CommandHelper(stats);

		// Create sign data
		log(ExternalStrings.get("sign-data-load"));
		SignData.file = new File(getDataFolder().toString() + "/.signdata");
		SignData.init(getConfig().getBoolean(Settings.Signs.ALLOW_MARQUEE));

		// create sign listener
		log(ExternalStrings.get("sign-listener-create"));
		getServer().getPluginManager().registerEvents(new SignListener(helper),
				this);

		// Create sign update timer
		getServer().getScheduler().runTaskTimer(
				this,
				new SignUpdater(getConfig().getBoolean(
						Settings.Signs.LOG_UPDATE_TIME)),
				getConfig().getLong(Settings.Signs.UPDATE_TIME),
				getConfig().getLong(Settings.Signs.UPDATE_TIME));
	}
}