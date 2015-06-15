package net.foxgenesis.serverstats.signs;

import static net.foxgenesis.serverstats.Logger.log;
import net.foxgenesis.serverstats.CommandHelper;
import net.foxgenesis.serverstats.ExternalStrings;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
	private final CommandHelper helper;

	public SignListener(final CommandHelper helper) {
		this.helper = helper;
	}

	@EventHandler
	public void onPlayerClickSign(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if (helper.getPlayers().contains(player)
				&& (event.getClickedBlock().getType() == Material.SIGN
						|| event.getClickedBlock().getType() == Material.SIGN_POST || event
						.getClickedBlock().getType() == Material.WALL_SIGN)) {
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				final Sign sign = (Sign) event.getClickedBlock().getState();
				if (!SignData.contains(sign.getLocation())) {
					log(ExternalStrings.get("server-stats-listen-add").replace("%loc%",sign.getLocation().toString()));
					player.sendMessage(ChatColor.GOLD + ExternalStrings.get("server-stats-listen-add").replace("%loc%",sign.getLocation().toString()));
					SignData.add(sign.getLocation(), sign.getLines());
				} else
					player.sendMessage(ChatColor.RED + ExternalStrings.get("server-stats-listen-noadd"));
			}
		}
	}
}
