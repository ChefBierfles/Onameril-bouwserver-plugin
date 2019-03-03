package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.commands.StaffMode;
import nl.hotseflots.onabouwserver.modules.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class BlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlace(org.bukkit.event.block.BlockPlaceEvent event) {

        /*
        If the player is in staffmode he will not be able to place blocks
         */
        if (StaffMode.staffModeList.contains(event.getPlayer().getUniqueId().toString())) {
            event.setBuild(false);
            event.setCancelled(true);
        } else {
            /*
            If the player breaks a block we save it.
            */
            PlayerStats.setPlacedBlocks(event.getPlayer(), PlayerStats.getPlacedBlocks(event.getPlayer()) + 1);;
        }
    }
}
