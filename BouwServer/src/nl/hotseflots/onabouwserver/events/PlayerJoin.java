package nl.hotseflots.onabouwserver.events;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.commands.StaffMode;
import nl.hotseflots.onabouwserver.twofactorauth.MCAuth;
import nl.hotseflots.onabouwserver.utils.Messages;
import nl.hotseflots.onabouwserver.utils.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.util.UUID;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        /*
        Verify 2fa before we continue
         */
        MCAuth.attemptDataLoad(event.getPlayer().getUniqueId());
        if (MCAuth.hasTwofactorauth(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(Messages.MCAUTH_LOGIN);
        }

        /*
        Player with this permission will auto join in staffmode
         */
        if (event.getPlayer().hasPermission("bouwserver.commands.staffmode")) {
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
                event.getPlayer().hidePlayer(Main.plugin , Bukkit.getPlayer(UUID.fromString(vanishedPlayers)));
            }
        }

        /*
        Create or update Player Data
         */
        registerPlayerDataForCommandSpy(event.getPlayer());
    }

    public void registerPlayerDataForCommandSpy(Player player) {

        //See if the player's UUID is registered UUID > NAME
        if (Main.plugin.getPlayerCache().getString("UUID2Player." + player.getUniqueId().toString()) == null) {
            Main.plugin.getPlayerCache().set("UUID2Player." + player.getUniqueId().toString(), player.getName().toLowerCase());
        }
        //See if the player name is registered NAME > UUID
        if (Main.plugin.getPlayerCache().getString("Player2UUID." + player.getName().toLowerCase()) == null) {
            Main.plugin.getPlayerCache().set("Player2UUID." + player.getName().toLowerCase(), player.getUniqueId().toString());
        }

        //See if the UUID exists in the logs
        if (Main.plugin.getPlayerCommandLogs().getString("PlayerCommandLogs." + player.getUniqueId().toString()) == null) {
            Main.plugin.getPlayerCommandLogs().set("PlayerCommandLogs." + player.getUniqueId().toString(), "");
            Main.plugin.getPlayerCommandLogs().set("PlayerCommandLogs." + player.getUniqueId().toString() + ".Name", player.getName().toLowerCase());
            Main.plugin.getPlayerCommandLogs().set("PlayerCommandLogs." + player.getUniqueId().toString() + ".UUID", player.getUniqueId().toString());
            Main.plugin.getPlayerCommandLogs().set("PlayerCommandLogs." + player.getUniqueId().toString() + ".IP-Adress", player.getAddress().getAddress().getHostAddress());
        }

        try {
            Main.plugin.getPlayerCommandLogs().save(Main.plugin.getPlayerCommandLogsFile());
            Main.plugin.getPlayerCache().save(Main.plugin.getPlayerCacheFile());
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
