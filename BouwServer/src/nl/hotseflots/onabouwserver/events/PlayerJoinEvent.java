package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.MOTD;
import nl.hotseflots.onabouwserver.modules.PlayerStats;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;

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
        Note the time in miliseconds that the player joined
         */
        PlayerStats.setJoinedTimeInMiliseconds(event.getPlayer(), System.currentTimeMillis());

        /*
        Players that can use 2fa should be forced to use it when they have the permission to do so
        What to do: Check if the file exists in the data folder if not let them setup 2fa.
         */
        if (Main.getInstance().getConfig().getString("Modules.TwoFA.Module").equalsIgnoreCase("enabled")) {
            if (event.getPlayer().hasPermission("bouwserver.2fa.setup")) {
                File userPath = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data" + File.separator + event.getPlayer().getUniqueId().toString() + ".yml");
                if (!userPath.exists()) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            event.getPlayer().chat("/2fa");
                        }
                    }, 20 * 1);
                    return;
                }
            }
        }

        /*
        Verify 2fa before we continue
         */
        if (Main.getInstance().getConfig().getString("Modules.TwoFA.Module").equalsIgnoreCase("enabled")) {
            TwoFA.attemptDataLoad(event.getPlayer().getUniqueId());
            if (TwoFA.hasTwofactorauth(event.getPlayer().getUniqueId())) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        event.getPlayer().sendMessage(Messages.MCAUTH_LOGIN.getMessage());
                    }
                }, 20 * 1);
                return;
            }
        }

        /*
        Send the player the servers MOTD if the MOTD Module is enabled
         */
        if (Main.getInstance().getConfig().getString("Modules.MOTD_MSG").equalsIgnoreCase("enabled")) {
            MOTD.sendDelayedMOTD(event.getPlayer());
        }
    }
}
