package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.commands.OpenMenu;
import nl.hotseflots.onabouwserver.commands.StaffMode;
import nl.hotseflots.onabouwserver.utils.Messages;
import nl.hotseflots.onabouwserver.utils.PlayerCache;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getInventory().getName() == null) {
            return;
        }

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getClickedInventory() instanceof PlayerInventory) {
            Player player = (Player) event.getWhoClicked();
            ItemStack item = null;

            if (event.getAction().name().contains("HOTBAR")) {
                item = event.getView().getBottomInventory().getItem(event.getHotbarButton());
            }

            if (item == null) {
                item = event.getCurrentItem();
            }

            if (StaffMode.isStaffItem(event.getCurrentItem())) {
                if (player.getGameMode() == GameMode.CREATIVE) {
                    player.closeInventory();
                    setCancelled(player, item, event.getSlot());
                    player.updateInventory();
                } else {
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        } else if (event.getClickedInventory().getName().equals(OpenMenu.historyInventory.getName())) {
            if (event.getWhoClicked() instanceof Player) {
                if (OpenMenu.dates.size() != 0) {
                    if (event.getCurrentItem().isSimilar(OpenMenu.nextButtonItem)) {
                        event.setCancelled(true);

                        OfflinePlayer target = Bukkit.getOfflinePlayer(PlayerCache.getUUIDFromPlayerName(ChatColor.stripColor(event.getInventory().getItem(49).getItemMeta().getDisplayName().toLowerCase())));
                        if (OpenMenu.dates.indexOf(ChatColor.stripColor(event.getInventory().getItem(44).getItemMeta().getDisplayName())) != -1) {
                            OpenMenu.OpenHistoryInventory(target, (Player) event.getWhoClicked(), (OpenMenu.dates.indexOf(ChatColor.stripColor(event.getInventory().getItem(44).getItemMeta().getDisplayName()))));
                        }
                        return;
                    } else if (event.getCurrentItem().isSimilar(OpenMenu.prevButtonItem)) {
                        event.setCancelled(true);
                        OfflinePlayer target = Bukkit.getOfflinePlayer(PlayerCache.getUUIDFromPlayerName(ChatColor.stripColor(event.getInventory().getItem(49).getItemMeta().getDisplayName().toLowerCase())));
                        OpenMenu.OpenHistoryInventory(target, (Player) event.getWhoClicked(), (OpenMenu.dates.indexOf(ChatColor.stripColor(event.getInventory().getItem(0).getItemMeta().getDisplayName()))) - 46);
                        return;
                    } else if (event.getCurrentItem().getType() == Material.ENCHANTED_BOOK && event.getCurrentItem().getItemMeta().hasLore()) {
                        event.setCancelled(true);
                        String positionLore = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getLore().get(2));
                        String worldPos = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getLore().get(1));

                        while (positionLore.contains(",")) {
                            positionLore = positionLore.replace(",", "");
                        }

                        String[] splitterPos = positionLore.split(" ");
                        String[] splitterWorld = worldPos.split(" ");
                        event.getWhoClicked().teleport(new Location(Bukkit.getWorld(splitterWorld[1]), Double.valueOf(splitterPos[1]), Double.valueOf(splitterPos[2]), Double.valueOf(splitterPos[3])));
                        return;
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    public void setCancelled(Player player, ItemStack item, int slot)
    {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable()
        {
            public void run()
            {
                player.getInventory().removeItem(item);
                player.getInventory().setItem(slot ,item);
            }
        }, (long) 0.01);
    }
}
