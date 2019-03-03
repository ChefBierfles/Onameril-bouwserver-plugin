package nl.hotseflots.onabouwserver.events;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.commands.StaffMode;
import nl.hotseflots.onabouwserver.modules.WelcomeMessage;
import nl.hotseflots.onabouwserver.modules.PlayerCache;
import nl.hotseflots.onabouwserver.modules.PlayerStats;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.io.File;
import java.util.UUID;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        /*
        Check if the JOIN_MSG module is enabled
         */
        if (Main.getInstance().getConfig().getString("Modules.JOIN_MSG").equalsIgnoreCase("enabled")) {
            if (!event.getPlayer().hasPermission("bouwserver.commands.staffmode")) {
                event.setJoinMessage(Messages.SERVER_TAG.getMessage() + Messages.JOIN_MSG.getMessage().replace("%player%", event.getPlayer().getName()));
            }
        }

        /*
        Register player data
         */
        PlayerCache.registerPlayerData(event.getPlayer());

        /*
        Retrieve the playerstats from the server storage and load it into the ram (cache)
         */
        PlayerStats.savePlayerStatsToCache(event.getPlayer());

        /*
        Note the join tme
         */
        PlayerStats.setJoinTime(event.getPlayer());

        /*
        Player with this permission will auto join in staffmode
         */
        if (event.getPlayer().hasPermission("bouwserver.commands.staffmode")) {
            if (TwoFA.hasTwofactorauth(event.getPlayer().getUniqueId())) {
                StaffMode.EnterStaffMode(event.getPlayer());
                ActionBarAPI.sendActionBar(event.getPlayer(), ChatColor.RED + "You joined silently in staffmode");
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (!event.getPlayer().getName().equalsIgnoreCase(players.getName())) {
                        if (players.hasPermission("bouwserver.commands.staffmode")) {
                            players.sendMessage(ChatColor.GOLD + event.getPlayer().getName() + ChatColor.GRAY + " joined silently and he vanished");
                        }
                    }
                }
            } else {
                for (String vanishedPlayers : StaffMode.vanishedList) {
                    event.getPlayer().hidePlayer(Main.getInstance(), Bukkit.getPlayer(UUID.fromString(vanishedPlayers)));
                }
            }
        }

        /*
        Players that can use 2fa should be forced to use it when they have the permission to do so
        What to do: Check if the file exists in the data folder if not let them setup 2fa.
         */
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

        /*
        Verify 2fa before we continue
         */
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

        /*
        Send the player the servers WelcomeMessage if the WelcomeMessage Module is enabled
         */
        if (Main.getInstance().getConfig().getString("Modules.MOTD_MSG").equalsIgnoreCase("enabled")) {
            WelcomeMessage.sendDelayedMOTD(event.getPlayer());
        }
    }
}
