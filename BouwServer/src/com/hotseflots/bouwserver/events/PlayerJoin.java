package com.hotseflots.bouwserver.events;

import com.hotseflots.bouwserver.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoin implements Listener {

    String SERVER_TAG = ChatColor.GRAY + "[" + ChatColor.GOLD + "Bouwserver" + ChatColor.GRAY + "] ";

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {

        event.setJoinMessage("");

        event.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Welkom!");
        event.getPlayer().sendMessage("");
        event.getPlayer().sendMessage(ChatColor.WHITE + "" + ChatColor.RESET + "Welkom op de bouwserver van het Kingdom " + ChatColor.GOLD + ChatColor.BOLD + "Onameril" + ChatColor.WHITE + "" + ChatColor.RESET + "! Deze");
        event.getPlayer().sendMessage("server heeft een nultolerantie beleid, dus wees altijd hoffelijk, hou je aan de regels, is het voor iedereen eens zo fijn.");event.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Contact personen!");
        event.getPlayer().sendMessage("");
        event.getPlayer().sendMessage(ChatColor.RESET + "EnkelNick");event.getPlayer().sendMessage(ChatColor.RESET + "Tqqn");
        event.getPlayer().sendMessage(ChatColor.RESET + "MijnKreftMetDani");
        event.getPlayer().sendMessage("");
        event.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "----------------------------------------------");
        event.getPlayer().sendMessage("");

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.instance, new Runnable() {
            @Override
            public void run() {
                event.getPlayer().sendMessage("Hallo!");
            }
        },1000 * 5);
    }
}
