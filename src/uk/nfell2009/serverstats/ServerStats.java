package uk.nfell2009.serverstats;

import java.net.MalformedURLException;

import net.foxgenesis.serverstats.PMCStats;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerStats extends JavaPlugin {

	private static ServerStats plugin;
	private PMCStats pmc;
	
	@Override
	public void onEnable() {
		plugin = this;
		try {
			pmc = new PMCStats(getConfig().getString("settings.pmc.serverurl"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDisable() {
		plugin = null;
		pmc = null;
	}
	
	public PMCStats getPMC() {
		return pmc;
	}
	
	public static ServerStats getInstance() {
		return plugin;
	}
}