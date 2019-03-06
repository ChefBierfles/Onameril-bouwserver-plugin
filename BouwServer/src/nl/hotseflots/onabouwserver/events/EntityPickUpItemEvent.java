package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.modules.StaffUtils.StaffMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.List;

public class EntityPickUpItemEvent implements Listener {

    @EventHandler
    public void onItemPickUp(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            if (StaffMode.playersInStaffMode.contains(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }
}
