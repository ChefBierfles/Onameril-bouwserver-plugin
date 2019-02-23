package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.modules.Bouwserver;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {

        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getInventory() == null) {
            return;
        }

        if (event.getCursor() == null) {
            return;
        }

        /*
        Check if the clicked inventory is the bouwserver one
         */
        if (event.getClickedInventory().getName().equals(Bouwserver.getInventory().getName())) {
            event.setCancelled(true);
        }
    }
}
