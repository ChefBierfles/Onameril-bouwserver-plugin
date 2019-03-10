package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.CommandHistoryMenu;
import nl.hotseflots.onabouwserver.modules.PlayerStats;
import nl.hotseflots.onabouwserver.modules.StaffUtils.StaffMode;
import nl.hotseflots.onabouwserver.utils.UUIDTool;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryClickEvent implements Listener {

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {

        if (event.getCurrentItem() == null) {
            return;
        }

        if (event.getCursor() == null) {
            return;
        }

        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getInventory() == null) {
            return;
        }

        if (event.getClickedInventory().getName() == null) {
            return;
        }

        if (event.getInventory().getName() == null) {
            return;
        }

        /*
        Check if the clicked inventory is the bouwserver one
         */
        if (!(event.getClickedInventory() instanceof PlayerInventory)) {
            if (event.getClickedInventory().getName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Onameril Bouwserver Plugin")) {
                event.setCancelled(true);
                return;
            } else if (event.getClickedInventory().getName().equals(ChatColor.DARK_GRAY + "(Most recent" + ChatColor.GOLD + "" + ChatColor.BOLD + " > " + ChatColor.RESET + ChatColor.DARK_GRAY + "Oldest)")) {
                if (CommandHistoryMenu.dates.size() != 0) {
                    if (event.getCurrentItem().isSimilar(CommandHistoryMenu.nextButtonItem)) {
                        event.setCancelled(true);

                        OfflinePlayer target = Bukkit.getOfflinePlayer(UUIDTool.getUUIDFromPlayerName(ChatColor.stripColor(event.getInventory().getItem(49).getItemMeta().getDisplayName().toLowerCase())));
                        if (CommandHistoryMenu.dates.indexOf(ChatColor.stripColor(event.getInventory().getItem(44).getItemMeta().getDisplayName())) != -1) {
                            CommandHistoryMenu.OpenHistoryInventory(target, (Player) event.getWhoClicked(), (CommandHistoryMenu.dates.indexOf(ChatColor.stripColor(event.getInventory().getItem(44).getItemMeta().getDisplayName()))));
                        }
                        return;
                    } else if (event.getCurrentItem().isSimilar(CommandHistoryMenu.prevButtonItem)) {
                        event.setCancelled(true);
                        OfflinePlayer target = Bukkit.getOfflinePlayer(UUIDTool.getUUIDFromPlayerName(ChatColor.stripColor(event.getInventory().getItem(49).getItemMeta().getDisplayName().toLowerCase())));
                        CommandHistoryMenu.OpenHistoryInventory(target, (Player) event.getWhoClicked(), (CommandHistoryMenu.dates.indexOf(ChatColor.stripColor(event.getInventory().getItem(0).getItemMeta().getDisplayName()))) - 46);
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
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        } else {
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
            }
        }
    }

    public void setCancelled(Player player, ItemStack item, int slot) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            public void run() {
                player.getInventory().removeItem(item);
                player.getInventory().setItem(slot, item);
            }
        }, (long) 0.01);
    }
}
