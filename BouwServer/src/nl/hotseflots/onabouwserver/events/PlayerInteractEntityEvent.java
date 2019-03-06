package nl.hotseflots.onabouwserver.events;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import nl.hotseflots.onabouwserver.modules.CommandHistoryMenu;
import nl.hotseflots.onabouwserver.modules.StaffUtils.StaffMode;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteractEntityEvent implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(org.bukkit.event.player.PlayerInteractEntityEvent event) {

        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }

        Player targetPlayer = (Player) event.getRightClicked();

        if (StaffMode.playersInStaffMode.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }

        /*
        When player clicks another player with the freeze item
        in staffmode
         */
        if (event.getHand() == EquipmentSlot.HAND) {
            if (event.getPlayer().getInventory().getItemInMainHand().isSimilar(StaffMode.freezeItem)) {
                if (event.getPlayer().hasPermission("bouwserver.staffmode.use.freezeitem")) {
                    event.setCancelled(true);
                    if (!StaffMode.frozenPlayers.contains(targetPlayer.getUniqueId())) {
                        StaffMode.frozenPlayers.add(targetPlayer.getUniqueId());
                        ActionBarAPI.sendActionBar(event.getPlayer(), "You froze " + ChatColor.GOLD + targetPlayer.getName());
                        ActionBarAPI.sendActionBar(targetPlayer.getPlayer(), "You got frozen by " + ChatColor.GOLD + event.getPlayer().getName());
                        return;
                    } else if (StaffMode.frozenPlayers.contains(targetPlayer.getUniqueId())) {
                        ActionBarAPI.sendActionBar(event.getPlayer(), "You unfroze " + ChatColor.GOLD + targetPlayer.getName());
                        ActionBarAPI.sendActionBar(targetPlayer.getPlayer(), "You got unfrozen by " + ChatColor.GOLD + event.getPlayer().getName());
                        StaffMode.frozenPlayers.remove(targetPlayer.getUniqueId());
                    }
                }
            } else if (event.getPlayer().getInventory().getItemInMainHand().isSimilar(StaffMode.lookupItem)) {
                if (event.getPlayer().hasPermission("bouwserver.staffmode.use.lookupitem")) {
                    event.setCancelled(true);
                    CommandHistoryMenu.OpenHistoryInventory((OfflinePlayer) event.getRightClicked(), event.getPlayer(), 0);
                }
            } else if (event.getPlayer().getInventory().getItemInMainHand().isSimilar(StaffMode.inventorySeeItem)) {
                if (event.getPlayer().hasPermission("bouwserver.staffmode.use.inventoryseeitem")) {
                    event.setCancelled(true);
                    event.getPlayer().openInventory(targetPlayer.getInventory());
                }
            }
        }
    }
}
