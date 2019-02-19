package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.commands.StaffMode;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (StaffMode.staffModeList.contains(event.getPlayer().getUniqueId().toString())) {
            event.setBuild(false);
            event.setCancelled(true);
        }
    }
}
