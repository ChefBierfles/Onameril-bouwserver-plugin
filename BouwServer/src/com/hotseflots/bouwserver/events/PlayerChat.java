package com.hotseflots.bouwserver.events;

import com.hotseflots.bouwserver.Main;
import com.hotseflots.bouwserver.UserManagerSystem;
import com.hotseflots.bouwserver.utils.ConfigPaths;
import com.hotseflots.bouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {


        /*
        Check if player is muted
         */
        if (Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(event.getPlayer().getUniqueId().toString(), "mutes"))) > 0) {
            String expireDateString = Main.plugin.config.getString(ConfigPaths.GetDetailPath(event.getPlayer().getUniqueId().toString(), "mutes", Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(event.getPlayer().getUniqueId().toString(), "mutes"))), "expires"));
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            Date expireDate;
            try {
                expireDate = dateFormat.parse(expireDateString);
            } catch (ParseException exc) {
                return;
            }

            if (date.before(expireDate)) {
                event.setCancelled(true);
            }
        }

        /*
        Handle punishment duration input
         */
        if (UserManagerSystem.getDuration) {
            event.setCancelled(true);
            UserManagerSystem.getDuration = false;
            String durationString = event.getMessage();
            UserManagerSystem.peformTask(durationString);
        }
    }
}
