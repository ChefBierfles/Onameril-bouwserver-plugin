package com.hotseflots.bouwserver.events;

import com.hotseflots.bouwserver.UserManagerSystem;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        /*
        Check if player is frozen
         */
        if (UserManagerSystem.frozenPlayers.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BELL, 1.0F , 1.0F);
        }
    }
}
