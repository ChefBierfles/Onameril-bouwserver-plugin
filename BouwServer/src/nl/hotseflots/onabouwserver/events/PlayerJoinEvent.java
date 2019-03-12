package nl.hotseflots.onabouwserver.events;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.StaffUtils.StaffMode;
import nl.hotseflots.onabouwserver.modules.WelcomeMessage;
import nl.hotseflots.onabouwserver.modules.PlayerCache;
import nl.hotseflots.onabouwserver.modules.PlayerStats;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA;
import nl.hotseflots.onabouwserver.utils.Messages;
import nl.hotseflots.onabouwserver.utils.Options;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.UUID;

public class PlayerJoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {

        /*
        Register player data
         */
        PlayerCache.registerPlayerData(event.getPlayer());

        /*
        Note the join tme
         */
        PlayerStats.setJoinTime(event.getPlayer());

        /*
        Retrieve the playerstats from the server storage and load it into the ram (cache)
         */
        PlayerStats.savePlayerStatsToCache(event.getPlayer());

        /*
        Player with this permission will auto join in staffmode
         */
        if (Options.MODULE_STAFFMODE.getStringValue().equalsIgnoreCase("Enabled")) {
            if (event.getPlayer().hasPermission("bouwserver.commands.staffmode")) {
                event.setJoinMessage("");
                    StaffMode.enterStaffMode(event.getPlayer());
                    ActionBarAPI.sendActionBar(event.getPlayer(), ChatColor.RED + "You joined silently in staffmode");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (!event.getPlayer().getName().equalsIgnoreCase(players.getName())) {
                            if (players.hasPermission("bouwserver.commands.staffmode")) {
                                players.sendMessage(ChatColor.GOLD + event.getPlayer().getName() + ChatColor.GRAY + " joined silently and he vanished");
                            }
                        }
                    }
            } else {
                /*
                Hide staff from freshly joined player
                 */
                for (UUID hideStaffFromUUID : StaffMode.playersInVanishMode) {
                    Player hideStaffFromPlayer = Bukkit.getPlayer(hideStaffFromUUID);
                    event.getPlayer().hidePlayer(Main.getInstance(), hideStaffFromPlayer);
                }
                /*
                Check if the JOIN_MSG module is enabled
                */
                if (Options.MODULE_JOIN_MSG.getStringValue().equalsIgnoreCase("enabled")) {
                    event.setJoinMessage(Messages.SERVER_TAG.getMessage() + Messages.JOIN_MSG.getMessage().replace("%player%", event.getPlayer().getName()));
                } else {
                    event.setJoinMessage("");
                }
            }
        }

        /*
        Players that can use 2fa should be forced to use it when they have the permission to do so
        What to do: Check if the file exists in the data folder if not let them setup 2fa.
         */
        if (Options.MODULE_TWOFA.getStringValue().equalsIgnoreCase("enabled")) {
            if (event.getPlayer().hasPermission("bouwserver.2fa.use")) {
                File userPath = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data" + File.separator + event.getPlayer().getUniqueId().toString() + ".yml");
                if ((!userPath.exists() && event.getPlayer().hasPermission("bouwserver.2fa.setup"))) {
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
        Verify 2fa before allowing the player to play
        after that send the player the motd message
         */
        if (Options.MODULE_TWOFA.getStringValue().equalsIgnoreCase("enabled")) {
            TwoFA.attemptDataLoad(event.getPlayer().getUniqueId());
            if (TwoFA.hasTwofactorauth(event.getPlayer().getUniqueId())) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        event.getPlayer().sendMessage(Messages.MCAUTH_LOGIN.getMessage());
                    }
                }, 20 * 1);
                return;
            } else {
            /*
            Send the player the servers WelcomeMessage if the WelcomeMessage Module is enabled
            */
                if (Options.MODULE_WELCOME_MSG.getStringValue().equalsIgnoreCase("enabled")) {
                    WelcomeMessage.sendDelayedMOTD(event.getPlayer());
                }
            }
        }
    }
}
