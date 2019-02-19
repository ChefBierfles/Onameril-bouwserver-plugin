package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.commands.StaffMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDrop implements Listener {

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (StaffMode.staffModeList.contains(event.getPlayer().getUniqueId().toString())) {
            event.setCancelled(true);
        }

        /*
        Player will not be able to drop staffitems
         */
        if (StaffMode.isStaffItem(event.getItemDrop().getItemStack())) {
            if (!StaffMode.staffModeList.contains(event.getPlayer().getUniqueId().toString())) {
                event.setCancelled(true);
                event.getItemDrop().remove();
                return;
            }
        }


        if (event.getItemDrop().getItemStack().isSimilar(StaffMode.vanishOnItem ) ) {
            event.setCancelled(true);
            player.updateInventory();
        } else if (event.getItemDrop().getItemStack().isSimilar(StaffMode.vanishOffItem)) {
            event.setCancelled(true);
            player.updateInventory();
        } else if (event.getItemDrop().getItemStack().isSimilar(StaffMode.randomTPItem)) {
            event.setCancelled(true);
            player.updateInventory();
        } else if (event.getItemDrop().getItemStack().isSimilar(StaffMode.lookupItem)) {
            event.setCancelled(true);
            player.updateInventory();
        } else if (event.getItemDrop().getItemStack().isSimilar(StaffMode.freezeItem)) {
            event.setCancelled(true);
            player.updateInventory();
        }
    }
}
