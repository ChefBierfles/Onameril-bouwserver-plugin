package com.hotseflots.bouwserver.events;

import com.hotseflots.bouwserver.Main;
import com.hotseflots.bouwserver.utils.ConfigPaths;
import com.hotseflots.bouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerJoin implements Listener {

    private static Date date;
    private static DateFormat dateFormat;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        //Exact date when the player joins
        setCurrentDate();

        //Generate or update User-Manager data
        if (event.getPlayer().hasPlayedBefore()) {
            createUserManagerData(event.getPlayer());
        } else {
            updateUserManagerData(event.getPlayer());
        }

        //Check if player is banned.
        checkIfPlayerIsBanned(event.getPlayer());

        //Send a message on player join.
        sendPlayerWelcomeMessage(event.getPlayer());
    }

    public static void setCurrentDate() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        date = new Date();
    }

    public static void createUserManagerData(Player player) {
        if (Main.plugin.config.get("players-history." + player.getUniqueId().toString()) == null) {
            Main.plugin.config.set("players-history." + player.getUniqueId().toString(), "");
            Main.plugin.config.set("players-history." + player.getUniqueId().toString() + ".name", player.getName());
            Main.plugin.config.set("players-history." + player.getUniqueId().toString() + ".last-login", dateFormat.format(date));
            Main.plugin.config.set("players-history." + player.getUniqueId().toString() + ".ip-adress", player.getAddress().toString());
            Main.plugin.config.set("players-history." + player.getUniqueId().toString() + ".kicks", "");
            Main.plugin.config.set("players-history." + player.getUniqueId().toString() + ".kicks.amount", 0);
            Main.plugin.config.set("players-history." + player.getUniqueId().toString() + ".bans", "");
            Main.plugin.config.set("players-history." + player.getUniqueId().toString() + ".bans.amount", 0);
            Main.plugin.config.set("players-history." + player.getUniqueId().toString() + ".mutes", "");
            Main.plugin.config.set("players-history." + player.getUniqueId().toString() + ".mutes.amount", 0);

            Main.plugin.saveConfig();
        }
    }

    public static void updateUserManagerData(Player player) {
        Main.plugin.config.set("players-history." + player.getUniqueId().toString() + ".last-login", dateFormat.format(date));
        //Update name
        Main.plugin.config.set("players-history." + player.getUniqueId().toString() + ".name", player.getName());
        //Update last ip and notify if logged on with another one.
        if (Main.plugin.config.getString(ConfigPaths.ipAdressPath(player.getUniqueId().toString())) != player.getAddress().getHostString()) {
            Bukkit.broadcastMessage(Messages.SERVER_TAG + " Speler " + player.getName() + " is met een ander ip dan zijn laatste sessie gejoined!");
        }
        Main.plugin.config.set("players-history." + player.getUniqueId().toString() + ".ip-adress", player.getAddress().getHostString());
        Main.plugin.saveConfig();
    }

    public static void checkIfPlayerIsBanned(Player player) {
        if (Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(player.getUniqueId().toString(), "bans"))) > 0) {
            String expireDateString = Main.plugin.config.getString(ConfigPaths.GetDetailPath(player.getUniqueId().toString(), "bans", Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(player.getUniqueId().toString(), "bans"))), "expires"));
            Date expireDate;
            try {
                expireDate = dateFormat.parse(expireDateString);
            } catch (ParseException exc) {
                return;
            }

            if (date.before(expireDate)) {
                player.kickPlayer(
                        Messages.BANNED_MSG(
                                Main.plugin.config.getString(ConfigPaths.GetDetailPath(player.getUniqueId().toString(), "bans", Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(player.getUniqueId().toString(), "bans"))), "by")),
                                Main.plugin.config.getString(ConfigPaths.GetDetailPath(player.getUniqueId().toString(), "bans", Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(player.getUniqueId().toString(), "bans"))), "reasons")),
                                Main.plugin.config.getString(ConfigPaths.GetDetailPath(player.getUniqueId().toString(), "bans", Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(player.getUniqueId().toString(), "bans"))), "expires"))
                        ));
                return;
            }
        }
    }

    public static void sendPlayerWelcomeMessage(Player player) {
//        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
//            @Override
//            public void run() {
//                player.sendMessage("\n\n\n\n\n");
//                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Welkom!");
//                player.sendMessage("");
//                player.sendMessage(ChatColor.WHITE + "" + ChatColor.RESET + "Welkom op de bouwserver van het Kingdom " + ChatColor.GOLD + ChatColor.BOLD + "Onameril" + ChatColor.WHITE + "" + ChatColor.RESET + "! Deze");
//                player.sendMessage("server heeft een nultolerantie beleid, dus wees altijd hoffelijk, hou je aan de regels, is het voor iedereen eens zo fijn.");event.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Contact personen!");
//                player.sendMessage("");
//                player.sendMessage(ChatColor.RESET + "EnkelNick");
//                player.sendMessage(ChatColor.RESET + "Tqqn");
//                player.sendMessage(ChatColor.RESET + "MijnKreftMetDani");
//                player.sendMessage("");
//                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "------------------------------------------");
//                player.sendMessage("");
//            }
//        },20*5);
    }
}
