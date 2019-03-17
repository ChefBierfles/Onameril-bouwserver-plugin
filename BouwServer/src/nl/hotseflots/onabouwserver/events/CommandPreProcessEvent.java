package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.CommandSpy;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA;
import nl.hotseflots.onabouwserver.utils.Messages;
import nl.hotseflots.onabouwserver.utils.Options;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.File;

public class CommandPreProcessEvent implements Listener {

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {

        if (event.getMessage().equalsIgnoreCase("/2fa")) {
            return;
        }

        if (Main.getInstance().getConfig().getString("Modules.TwoFA.Module").equalsIgnoreCase("enabled")) {
            File userPath = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data" + File.separator + event.getPlayer().getUniqueId().toString() + ".yml");
            if (TwoFA.hasTwofactorauth(event.getPlayer().getUniqueId()) || (!userPath.exists() && !event.getMessage().equalsIgnoreCase("/2fa") && event.getPlayer().hasPermission("bouwserver.2fa.use"))) {
                System.out.println("3");
                if (Options.DENY_COMMANDS.getBooleanValue()) {
                    event.setCancelled(true);
                }
            }
        }

        CommandSpy.CommandSpy(event.getMessage(), event.getPlayer());


        /*
        Kick all players to save PlayerStats properly when the server closes
         */
        if (event.getMessage().equalsIgnoreCase("/stop")) {
            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                onlinePlayers.kickPlayer(Messages.SERVER_CLOSING_MSG.getMessage());
            }
        }

        /*
        Plugin hider
         */
        if (event.getMessage().equalsIgnoreCase("/pl") || event.getMessage().equalsIgnoreCase("/plugins") || event.getMessage().equals("?")) {
            if (!event.getPlayer().hasPermission("bouwserver.commands.seepluginhider")) {
                event.getPlayer().sendMessage("Plugins (5): " + ChatColor.GREEN + "Fix" + ChatColor.WHITE + ", " + ChatColor.GREEN + "Je" + ChatColor.WHITE + ", " + ChatColor.GREEN + "Eigen" + ChatColor.WHITE + ", " + ChatColor.GREEN + "Plugins" + ChatColor.WHITE + ", " + ChatColor.GREEN + "Loser!");
                event.setCancelled(true);
            }
        }
    }
}
