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

import static net.foxgenesis.serverstats.Logger.error;
import static net.foxgenesis.serverstats.Logger.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteStreams;

import net.foxgenesis.serverstats.signs.SignData;
import net.foxgenesis.serverstats.signs.SignListener;
import net.foxgenesis.serverstats.signs.SignUpdater;
import net.foxgenesis.serverstats.websites.CustomJSON;
import net.foxgenesis.serverstats.websites.MinecraftServerListStats;
import net.foxgenesis.serverstats.websites.PMCStats;
import net.foxgenesis.serverstats.websites.WebsiteStats;

public class ServerStats extends JavaPlugin {

	protected static ServerStats instance;
	protected WebsiteStats[] stats;

	private File loadResource(String resource) {
		File folder = getDataFolder();
		if (!folder.exists())
			folder.mkdir();
		else {
			File f = new File(folder + resource);
			if (f.exists())
				return f;
		}
		File resourceFile = new File(folder, resource);
		try {
			if (!resourceFile.exists()) {
				resourceFile.createNewFile();
				try (InputStream in = getResource(resource); OutputStream out = new FileOutputStream(resourceFile)) {
					ByteStreams.copy(in, out);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resourceFile;
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
		stats = new WebsiteStats[] { new CustomJSON(), new PMCStats(), new MinecraftServerListStats() };
		
		/*log("Loading custom stats...");
		Arrays.asList(getDataFolder().listFiles((file,name) -> name.endsWith(".jar"))).forEach(file -> {
			try {
				String name = file.getName().substring(0, file.getName().lastIndexOf("."));
				byte[] classBytes = FileUtil.readBytes(file.getPath());
			    Class<?> c = ClassLoaderUtil.defineClass(name, classBytes);
			    Object o = c.newInstance();                    // start using it...
				if(o instanceof WebsiteStats) {
					ArrayHelper.append(stats, (WebsiteStats)o);
				} else error(file.getName() + " has not been created correctly");
			} catch(Exception e){
				e.printStackTrace();
			}
		});*/

		// load external strings
		if (!ExternalStrings.init(getConfig().getString("lang"))) {
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
		Arrays.asList(stats).forEach(a -> {
			a.loadSettings(getConfig());
		});

		new ServerStatsCommand(stats);
		// create command listener

		// Create sign data
		log(ExternalStrings.get("sign-data-load"));
		SignData.init(this,getConfig().getBoolean(Settings.Signs.ALLOW_MARQUEE));

		// create sign listener
		log(ExternalStrings.get("sign-listener-create"));
		getServer().getPluginManager().registerEvents(new SignListener(), this);

		// Create sign update timer
		getServer().getScheduler().runTaskTimer(this,
				new SignUpdater(getConfig().getBoolean(Settings.Signs.LOG_UPDATE_TIME)),
				getConfig().getLong(Settings.Signs.UPDATE_TIME), getConfig().getLong(Settings.Signs.UPDATE_TIME));
		log(ExternalStrings.get("loaded"));
	}
}