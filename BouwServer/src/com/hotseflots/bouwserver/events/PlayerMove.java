package com.hotseflots.bouwserver.events;

import com.hotseflots.bouwserver.modules.UserManagerSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        /*
        Check if player is frozen
         */
        if (UserManagerSystem.frozenPlayers.contains(event.getPlayer().getUniqueId().toString())) {
            event.setCancelled(true);
        }
    }
}
