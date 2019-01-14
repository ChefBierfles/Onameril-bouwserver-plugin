package com.hotseflots.bouwserver.events;

import com.hotseflots.bouwserver.Main;
import com.hotseflots.bouwserver.UserManagerSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerJoin implements Listener {
    /*
    Everything that happends when a player joins.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

        /*
        Send a message on player join.
         */
        event.setJoinMessage("");

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
            @Override
            public void run() {
                event.getPlayer().sendMessage("\n\n\n\n\n");
                event.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Welkom!");
                event.getPlayer().sendMessage("");
                event.getPlayer().sendMessage(ChatColor.WHITE + "" + ChatColor.RESET + "Welkom op de bouwserver van het Kingdom " + ChatColor.GOLD + ChatColor.BOLD + "Onameril" + ChatColor.WHITE + "" + ChatColor.RESET + "! Deze");
                event.getPlayer().sendMessage("server heeft een nultolerantie beleid, dus wees altijd hoffelijk, hou je aan de regels, is het voor iedereen eens zo fijn.");event.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Contact personen!");
                event.getPlayer().sendMessage("");
                event.getPlayer().sendMessage(ChatColor.RESET + "EnkelNick");event.getPlayer().sendMessage(ChatColor.RESET + "Tqqn");
                event.getPlayer().sendMessage(ChatColor.RESET + "MijnKreftMetDani");
                event.getPlayer().sendMessage("");
                event.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "------------------------------------------");
                event.getPlayer().sendMessage("");
            }
        },20*5);

        /*
        Generate User-Manager data for joined player
        */
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        if (Main.plugin.config.get("players-history." + event.getPlayer().getUniqueId().toString()) == null) {
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString(), "");
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".name", event.getPlayer().getName());
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".last-login", dateFormat.format(date));
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".ip-adress", event.getPlayer().getAddress().getHostString());
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".kicks", "");
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".kicks.amount", 0);
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".bans", "");
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".bans.amount", 0);
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".mutes", "");
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".mutes.amount", 0);
            Main.plugin.saveConfig();
        }

        /*
        Check if player is banned.
         */
    }
}
