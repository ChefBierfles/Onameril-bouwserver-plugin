package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.PlayerStats;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

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

        /*
        Check if the clicked inventory is the bouwserver one
         */
        if (event.getClickedInventory().getName().equals(PlayerStats.getInventory().getName())) {
            event.setCancelled(true);
            return;
        }
    }

    public void setCancelled(Player player, ItemStack item, int slot)
    {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable()
        {
            public void run()
            {
                player.getInventory().removeItem(item);
                player.getInventory().setItem(slot ,item);
            }
        }, (long) 0.01);
    }
}
