package com.hotseflots.bouwserver.events;

import com.hotseflots.bouwserver.modules.UserManagerSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        /*
        Check if player is frozen
         */
        if (UserManagerSystem.frozenPlayers.contains(event.getPlayer().getUniqueId().toString())) {
            event.setCancelled(true);
        }
    }
}
