package nl.hotseflots.onabouwserver.events;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import nl.hotseflots.onabouwserver.commands.OpenMenu;
import nl.hotseflots.onabouwserver.commands.StaffMode;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractEntity implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }

        if (StaffMode.staffModeList.contains(event.getPlayer().getUniqueId().toString())) {
            event.setCancelled(true);
        }

        Player targetPlayer = (Player) event.getRightClicked();

        /*
        When player clicks another player with the freeze item
        in staffmode
         */
        if (event.getHand() == EquipmentSlot.HAND) {
            if (event.getPlayer().getInventory().getItemInMainHand().isSimilar(StaffMode.freezeItem)) {
                if (event.getPlayer().hasPermission("bouwserver.staffmode.use.freezeitem")) {
                    event.setCancelled(true);
                    if (!StaffMode.frozenPlayerList.contains(targetPlayer.getUniqueId().toString())) {
                        StaffMode.frozenPlayerList.add(targetPlayer.getUniqueId().toString());
                        ActionBarAPI.sendActionBar(event.getPlayer(), "You froze " + ChatColor.GOLD + targetPlayer.getName());
                        ActionBarAPI.sendActionBar(targetPlayer.getPlayer(), "You got frozen by " + ChatColor.GOLD + event.getPlayer().getName());
                        return;
                    } else if (StaffMode.frozenPlayerList.contains(targetPlayer.getUniqueId().toString())) {
                        ActionBarAPI.sendActionBar(event.getPlayer(), "You unfroze " + ChatColor.GOLD + targetPlayer.getName());
                        ActionBarAPI.sendActionBar(targetPlayer.getPlayer(), "You got unfrozen by " + ChatColor.GOLD + event.getPlayer().getName());
                        StaffMode.frozenPlayerList.remove(targetPlayer.getUniqueId().toString());
                    }
                }
            } else if (event.getPlayer().getInventory().getItemInMainHand().isSimilar(StaffMode.lookupItem)) {
                if (event.getPlayer().hasPermission("bouwserver.staffmode.use.lookupitem")) {
                    event.setCancelled(true);
                    OpenMenu.OpenHistoryInventory((OfflinePlayer) event.getRightClicked(), event.getPlayer(), 0);
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
