package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA;
import nl.hotseflots.onabouwserver.utils.Options;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;

public class PlayerInteractEvent implements Listener {

    @EventHandler
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {

        if (Main.getInstance().getConfig().getString("Modules.TwoFA.Module").equalsIgnoreCase("enabled")) {
            File userPath = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data" + File.separator + event.getPlayer().getUniqueId().toString() + ".yml");
            if (TwoFA.hasTwofactorauth(event.getPlayer().getUniqueId()) || !userPath.exists()) {

                if (!Options.DENY_INTERACTION.getBooleanValue()) {
                    return;
                }

                event.setCancelled(true);
            }
        }
    }
}
