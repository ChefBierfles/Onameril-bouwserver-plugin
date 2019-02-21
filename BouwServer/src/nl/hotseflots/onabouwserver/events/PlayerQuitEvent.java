package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {

        /*
        Check if the QUIT_MSG module is enabled
         */
        if (Main.getInstance().getConfig().getString("Modules.QUIT_MSG").equalsIgnoreCase("enabled")) {
            event.setQuitMessage(Messages.SERVER_TAG.getMessage() + Messages.QUIT_MSG.getMessage().replace("%player%", event.getPlayer().getName()));
        }
    }
}
