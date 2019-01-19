package com.hotseflots.bouwserver.events;

import com.hotseflots.bouwserver.Main;
import com.hotseflots.bouwserver.utils.ConfigPaths;
import com.hotseflots.bouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        //Exact date when the player joins
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        /*
        Generate User-Manager data for joined player
        */
        if (Main.plugin.config.get("players-history." + event.getPlayer().getUniqueId().toString()) == null) {
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString(), "");
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".name", event.getPlayer().getName());
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".last-login", dateFormat.format(date));
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".ip-adress", event.getPlayer().getAddress().toString());
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".kicks", "");
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".kicks.amount", 0);
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".bans", "");
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".bans.amount", 0);
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".mutes", "");
            Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".mutes.amount", 0);
        }

        //Update last joined
        Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".last-login", dateFormat.format(date));
        //Update name
        Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".name", event.getPlayer().getName());
        //Update last ip and notify if logged on with another one.
        if (Main.plugin.config.getString("players-history." + event.getPlayer().getUniqueId().toString() + ".ip-adress") != event.getPlayer().getAddress().getHostString()) {
            Bukkit.broadcastMessage(Messages.SERVER_TAG + " Speler " + event.getPlayer().getName() + " is met een ander ip dan zijn laatste sessie gejoined!");
        }
        Main.plugin.config.set("players-history." + event.getPlayer().getUniqueId().toString() + ".ip-adress", event.getPlayer().getAddress().getHostString());
        Main.plugin.saveConfig();

        /*
        Check if player is banned.
         */
        if (Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(event.getPlayer().getUniqueId().toString(), "bans"))) > 0) {
            String expireDateString = Main.plugin.config.getString(ConfigPaths.GetDetailPath(event.getPlayer().getUniqueId().toString(), "bans", Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(event.getPlayer().getUniqueId().toString(), "bans"))), "expires"));
            Date expireDate;
            try {
                expireDate = dateFormat.parse(expireDateString);
            } catch (ParseException exc) {
                return;
            }

            if (date.before(expireDate)) {
                event.getPlayer().kickPlayer(
                        Messages.BANNED_MSG(
                                Main.plugin.config.getString(ConfigPaths.GetDetailPath(event.getPlayer().getUniqueId().toString(), "bans", Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(event.getPlayer().getUniqueId().toString(), "bans"))), "by")),
                                Main.plugin.config.getString(ConfigPaths.GetDetailPath(event.getPlayer().getUniqueId().toString(), "bans", Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(event.getPlayer().getUniqueId().toString(), "bans"))), "reasons")),
                                Main.plugin.config.getString(ConfigPaths.GetDetailPath(event.getPlayer().getUniqueId().toString(), "bans", Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(event.getPlayer().getUniqueId().toString(), "bans"))), "expires"))
                        ));
                return;
            }
        }

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
    }
}
