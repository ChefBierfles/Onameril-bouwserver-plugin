package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.modules.PlayerStats;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockPlaceEvent implements Listener {

    @EventHandler
    public void onBlockPlace(org.bukkit.event.block.BlockPlaceEvent event) {

        /*
        If the player breaks a block we save it.
        */
        PlayerStats.setPlacedBlocks(event.getPlayer(), PlayerStats.getPlacedBlocks(event.getPlayer()) + 1);
    }
}
