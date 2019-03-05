package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.commands.StaffMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class EntityPickUpItemEvent implements Listener {

    @EventHandler
    public void onEntityPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            if (StaffMode.staffModeList.contains(event.getEntity().getUniqueId().toString())) {
                event.setCancelled(true);
            }
        }
    }
}
