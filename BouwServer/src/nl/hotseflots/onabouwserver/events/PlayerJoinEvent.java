package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        /*
        Send the player the servers MOTD
         */
        if (Main.getInstance().getConfig().getString("Modules.MOTD_MSG").equalsIgnoreCase("enabled")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    String motdMessage = Messages.MOTD_MSG.getMessage();
                    motdMessage = motdMessage.replace("%player%", event.getPlayer().getName());
                    motdMessage = motdMessage.replace("%newline%", ChatColor.translateAlternateColorCodes('&', "&r "));
                    event.getPlayer().sendMessage(motdMessage);
                }
            }, 20*4); // 20 * 4 = 80ticks : 4 sec
        }
    }
}
