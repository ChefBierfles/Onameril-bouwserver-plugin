package nl.hotseflots.onabouwserver.modules;

import nl.hotseflots.onabouwserver.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerStats {

    public static ItemStack playerStatsItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    public static ItemStack serverStatsItem = new ItemStack(Material.GOLD_PICKAXE);
    public static ItemStack pluginStatsItem = new ItemStack(Material.NAME_TAG);
    private static HashMap<UUID, Integer> placedBlocksList = new HashMap<>();
    private static HashMap<UUID, Integer> brokenBlocksList = new HashMap<>();
    private static HashMap<UUID, Long> joinedTimeInMiliseconds = new HashMap<>();
    private static HashMap<UUID, Long> quittedTimeInMiliseconds = new HashMap<>();
    private static Inventory bouwServerInventory;

    public static float milisecondsToHours(Long miliseconds) {
        return Math.round((miliseconds / 3600000) * 100) / 100;
    }

    public static Integer getBrokenBlocks(Player player) {

        if (brokenBlocksList.containsKey(player.getUniqueId())) {
            return brokenBlocksList.get(player.getUniqueId());
        } else {
            return 0;
        }
    }

    public static Integer getPlacedBlocks(Player player) {

        if (placedBlocksList.containsKey(player.getUniqueId())) {
            return placedBlocksList.get(player.getUniqueId());
        } else {
            return 0;
        }
    }

    public static Long getJoinedTimeInMiliseconds(Player player) {
        return joinedTimeInMiliseconds.get(player.getUniqueId());
    }

    public static Long getQuittedTimeInMiliseconds(Player player) {
        return quittedTimeInMiliseconds.get(player.getUniqueId());
    }

    public static void setJoinedTimeInMiliseconds(Player player, long ms) {
        joinedTimeInMiliseconds.put(player.getUniqueId(), ms);
    }

    public static void setQuittedTimeInMiliseconds(Player player, long ms) {
        quittedTimeInMiliseconds.put(player.getUniqueId(), ms);
    }

    public static void setBrokenBlocks(Player player, int upBy) {
        brokenBlocksList.put(player.getUniqueId(), getBrokenBlocks(player) + upBy);
    }

    public static Long getPlayedMiliseconds(Player player) {
        return System.currentTimeMillis() - getJoinedTimeInMiliseconds(player);
    }

    public static void setPlacedBlocks(Player player, int upBy) {
        placedBlocksList.put(player.getUniqueId(), getPlacedBlocks(player) + upBy);
    }

    public static void createItems(Player player) {

        /*
        Set player stats
         */
        double hoursPlayed;
        try {
            hoursPlayed = PlayerStats.milisecondsToHours(PlayerStats.getPlayedMiliseconds(player));
        } catch (NullPointerException exc) {
            hoursPlayed = 0;
        }
        int blocksPlaced = PlayerStats.getPlacedBlocks(player);
        int blocksBroken = PlayerStats.getBrokenBlocks(player);
        String pluginDateReleased = "21/02/2019 16:30:00";

        /*
        Player stats
         */
        SkullMeta playerStatsItemMeta = (SkullMeta) playerStatsItem.getItemMeta();
        playerStatsItemMeta.setDisplayName(ChatColor.GOLD + player.getName());
        playerStatsItemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.YELLOW + "Je UUID is: " + ChatColor.GOLD + player.getUniqueId());
        itemLore.add(ChatColor.YELLOW + "Je hebt " + ChatColor.GOLD + hoursPlayed + ChatColor.YELLOW + " uren gespeeld!");
        itemLore.add(ChatColor.YELLOW + "Je hebt " + ChatColor.GOLD + blocksPlaced + ChatColor.YELLOW + " blokken geplaatst!");
        itemLore.add(ChatColor.YELLOW + "Je hebt " + ChatColor.GOLD + blocksBroken + ChatColor.YELLOW + " blokken gebroken!");
        playerStatsItemMeta.setLore(itemLore);
        playerStatsItem.setItemMeta(playerStatsItemMeta);
        itemLore.clear();

        /*
        Server stats
         */
        ItemMeta serverStatsItemItemMeta = serverStatsItem.getItemMeta();
        serverStatsItemItemMeta.setDisplayName(ChatColor.GOLD + "Onameril Bouwserver");
        itemLore.add(ChatColor.YELLOW + "IP: " + ChatColor.GOLD + "OnaBouwserver.nl");
        itemLore.add(ChatColor.YELLOW + "Versie: " + ChatColor.GOLD + player.getServer().getVersion());
        itemLore.add(ChatColor.YELLOW + "Werelden: ");
        for (World world : player.getServer().getWorlds()) {
            itemLore.add(ChatColor.YELLOW + "     - " + ChatColor.GOLD + world.getName());
        }
        serverStatsItemItemMeta.setLore(itemLore);
        serverStatsItem.setItemMeta(serverStatsItemItemMeta);
        itemLore.clear();

        /*
        Plugin stats
         */
        ItemMeta pluginStatsItemMeta = pluginStatsItem.getItemMeta();
        pluginStatsItemMeta.setDisplayName(ChatColor.GOLD + "Plugin info");
        itemLore.add(ChatColor.YELLOW + "Naam: " + ChatColor.GOLD + Main.getInstance().getDescription().getName());
        itemLore.add(ChatColor.YELLOW + "Versie: " + ChatColor.GOLD + Main.getInstance().getDescription().getVersion());
        itemLore.add(ChatColor.YELLOW + "Laaste release: " + ChatColor.GOLD + pluginDateReleased);
        pluginStatsItemMeta.setLore(itemLore);
        pluginStatsItem.setItemMeta(pluginStatsItemMeta);
        itemLore.clear();
    }

    /*
    Create the inventory so it is viewable for the player
     */
    public static void createInvenory(Player player) {

        //Set the quitted time to now so we get the time difference between that we joined and openened the menu
        PlayerStats.setQuittedTimeInMiliseconds(player, System.currentTimeMillis());
        createItems(player);
        bouwServerInventory = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Onameril Bouwserver Plugin");
        bouwServerInventory.setItem(1, playerStatsItem);
        bouwServerInventory.setItem(4, serverStatsItem);
        bouwServerInventory.setItem(7, pluginStatsItem);

        player.openInventory(bouwServerInventory);
    }

    /*
    Get the bouwserver inventory
     */
    public static Inventory getInventory() {
        return bouwServerInventory;
    }
}
