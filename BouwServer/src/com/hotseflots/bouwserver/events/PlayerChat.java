package com.hotseflots.bouwserver.events;

import com.hotseflots.bouwserver.UserManagerSystem;
import com.hotseflots.bouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        /*
        Check if player is muted
         */

        /*
        Handle duration
         */
        if (UserManagerSystem.getDuration) {
            event.setCancelled(true);
            UserManagerSystem.getDuration = false;
            UserManagerSystem.peformTask(UserManagerSystem.lastCommandArgs[0], event.getPlayer(), Bukkit.getPlayer(UserManagerSystem.lastCommandArgs[1]), event.getMessage(), UserManagerSystem.getReason(UserManagerSystem.lastCommandArgs));
        }
    }
}
