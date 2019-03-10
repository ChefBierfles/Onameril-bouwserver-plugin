package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.utils.Options;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LeavesDecayEvent implements Listener {

    @EventHandler
    public void onLeavesDecay(org.bukkit.event.block.LeavesDecayEvent event) {
        if (Options.MODULE_ANTILEAVEDECAY.getStringValue().equalsIgnoreCase("enabled")) {
            event.setCancelled(true);
        }
    }
}
