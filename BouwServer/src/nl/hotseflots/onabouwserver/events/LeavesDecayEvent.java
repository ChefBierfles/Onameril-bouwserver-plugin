package nl.hotseflots.onabouwserver.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LeavesDecayEvent implements Listener {

    @EventHandler
    public void onLeavesDecay(org.bukkit.event.block.LeavesDecayEvent event) {
        event.setCancelled(true);
    }
}
