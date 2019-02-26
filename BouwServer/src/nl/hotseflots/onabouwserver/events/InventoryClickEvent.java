package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.modules.PlayerStats;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getCursor() == null) {
            return;
        }

        if (event.getInventory().getName() == null || event.getClickedInventory().getName() == null) {
            return;
        }

        if (event.getCurrentItem().getItemMeta() == null || event.getCursor().getItemMeta() == null) {
            return;
        }

        /*
        Check if the clicked inventory is the bouwserver one
         */
        if (event.getClickedInventory().getName().equals(PlayerStats.getInventory().getName())) {
            event.setCancelled(true);
        }
    }
}
