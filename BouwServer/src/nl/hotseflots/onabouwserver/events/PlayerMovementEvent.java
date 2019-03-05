package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA;
import nl.hotseflots.onabouwserver.utils.Options;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.File;

public class PlayerMovementEvent implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        /*
        When the player has to fill a 2fa code before playiny
        If so disable movement until he is verified
         */
        if (Main.getInstance().getConfig().getString("Modules.TwoFA.Module").equalsIgnoreCase("enabled")) {
            File userPath = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data" + File.separator + event.getPlayer().getUniqueId().toString() + ".yml");
            if (TwoFA.hasTwofactorauth(event.getPlayer().getUniqueId()) || (!userPath.exists() && event.getPlayer().hasPermission("bouwserver.2fa.setup"))) {
                if (((event.getFrom().getBlockX() != event.getTo().getBlockX()) || (event.getFrom().getBlockY() != event.getTo().getBlockY()) || (event.getFrom().getBlockZ() != event.getTo().getBlockZ()))) {
                    if (!Options.DENY_MOVEMENT.getBooleanValue()) {
                        return;
                    }
                    event.getPlayer().teleport(event.getFrom());
                }
            }
        }
    }
}
