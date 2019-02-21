package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {

        /*
        Check if the JOIN_MSG module is enabled
         */
        if (Main.getInstance().getConfig().getString("Modules.JOIN_MSG").equalsIgnoreCase("enabled")) {
            event.setJoinMessage(Messages.SERVER_TAG.getMessage() + Messages.JOIN_MSG.getMessage().replace("%player%", event.getPlayer().getName()));
        }
    }
}
