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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerStats {

    public static ItemStack playerStatsItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    public static ItemStack serverStatsItem = new ItemStack(Material.GOLD_PICKAXE);
    public static ItemStack pluginStatsItem = new ItemStack(Material.NAME_TAG);
    private static HashMap<UUID, Integer> placedBlocksList = new HashMap<>();
    private static HashMap<UUID, Integer> brokenBlocksList = new HashMap<>();
    private static HashMap<UUID, Long> joinTime = new HashMap<>();
    private static HashMap<UUID, Long> playedTime = new HashMap<>();
    private static Inventory bouwServerInventory;

    private static int itemUpdaterTask;

    public static void setJoinTime(Player player) {
        joinTime.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public static Long getJoinTime(Player player) {
        return joinTime.get(player.getUniqueId());
    }

    public static void setPlayedTime(Player player) {

        Long playedTimeInMs = (System.currentTimeMillis() - getJoinTime(player)) + getPlayedTime(player);
        setJoinTime(player);
        playedTime.put(player.getUniqueId(), playedTimeInMs);
    }

    public static Long getPlayedTime(Player player) {
        return playedTime.get(player.getUniqueId());
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

    public static void setBrokenBlocks(Player player, int amount) {
        brokenBlocksList.put(player.getUniqueId(), amount);
    }

    public static void setPlacedBlocks(Player player, int amount) {
        placedBlocksList.put(player.getUniqueId(), amount);
    }

    public static void createItems(Player player) {

        /*
        Set player stats
         */
        String hoursPlayed = PlayerStats.playedTimeToHours(player);
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
        itemLore.add(ChatColor.YELLOW + "Uw UUID is: " + ChatColor.GOLD + player.getUniqueId());
        itemLore.add(ChatColor.YELLOW + "U hebt " + ChatColor.GOLD + blocksPlaced + ChatColor.YELLOW + " blokken geplaatst!");
        itemLore.add(ChatColor.YELLOW + "U hebt " + ChatColor.GOLD + blocksBroken + ChatColor.YELLOW + " blokken gebroken!");
        itemLore.add(ChatColor.YELLOW + "U speeltijd is: " + ChatColor.GOLD + hoursPlayed + ChatColor.YELLOW + "!");
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

    public static void savePlayerStatsToStorage(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
            @Override
            public void run() {

                Main.getInstance().getPlayerCache().set("Data." + player.getUniqueId().toString() + ".BLOCKS_PLACED", PlayerStats.getPlacedBlocks(player));
                Main.getInstance().getPlayerCache().set("Data." + player.getUniqueId().toString() + ".BLOCKS_BROKEN", PlayerStats.getBrokenBlocks(player));
                Main.getInstance().getPlayerCache().set("Data." + player.getUniqueId().toString() + ".PLAYED_MILISECONDS", PlayerStats.getPlayedTime(player).toString());

                PlayerStats.playedTime.remove(player.getUniqueId());
                PlayerStats.placedBlocksList.remove(player.getUniqueId());
                PlayerStats.brokenBlocksList.remove(player.getUniqueId());
                try {
                    Main.getInstance().getPlayerCache().save(Main.getInstance().getPlayerCacheFile());
                } catch (IOException exc) {}
            }
        });
    }

    public static void savePlayerStatsToCache(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                PlayerStats.playedTime.put(player.getUniqueId(), Long.parseLong(Main.getInstance().getPlayerCache().getString("Data." + player.getUniqueId().toString() + ".PLAYED_MILISECONDS")));
                PlayerStats.setBrokenBlocks(player, (int) Main.getInstance().getPlayerCache().get("Data." + player.getUniqueId().toString() + ".BLOCKS_BROKEN"));
                PlayerStats.setPlacedBlocks(player, (int) Main.getInstance().getPlayerCache().get("Data." + player.getUniqueId().toString() + ".BLOCKS_PLACED"));
            }
        });
    }

    public static String playedTimeToHours(Player player) {
        setPlayedTime(player);
        Long millis = getPlayedTime(player);
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static void savePlayerStatsInterval(int interval){
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player players: Bukkit.getOnlinePlayers()) {
                    Main.getInstance().getPlayerCache().set("Data." + players.getUniqueId().toString() + ".BLOCKS_BROKEN", PlayerStats.getBrokenBlocks(players));
                    Main.getInstance().getPlayerCache().set("Data." + players.getUniqueId().toString() + ".BLOCKS_PLACED", PlayerStats.getPlacedBlocks(players));
                    PlayerStats.setPlayedTime(players);
                    Main.getInstance().getPlayerCache().set("Data." + players.getUniqueId().toString() + ".PLAYED_MILISECONDS", PlayerStats.getPlayedTime(players).toString());

                    try {
                        Main.getInstance().getPlayerCache().save(Main.getInstance().getPlayerCacheFile());
                    } catch (IOException exc) { exc.printStackTrace(); }
                }
            }
        }, 20 * interval, 20 * interval);
    }
}
