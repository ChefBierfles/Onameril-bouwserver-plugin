package nl.hotseflots.onabouwserver.modules;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WelcomeMessage {

    public static void sendDelayedMOTD(Player player) {

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                String motdMessage = Messages.MOTD_MSG.getMessage();
                motdMessage = motdMessage.replace("%player%", player.getName());
                motdMessage = motdMessage.replace("%newline%", ChatColor.translateAlternateColorCodes('&', "&r "));
                player.sendMessage(motdMessage);
            }
        }, 20 * 4); // 20 * 4 = 80ticks : 4 sec
    }
}
