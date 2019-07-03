package nl.hotseflots.onabouwserver.modules;

import com.comphenix.protocol.PacketType;
import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.utils.UUIDTool;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.security.KeyStore;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PlayerStats {

    public static ItemStack playerStatsItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    public static ItemStack serverStatsItem = new ItemStack(Material.GOLD_PICKAXE);
    public static ItemStack pluginStatsItem = new ItemStack(Material.NAME_TAG);
    public static ItemStack timePlayedLabelItem = new ItemStack(Material.WATCH);
    public static ItemStack blocksBrokenLabelItem = new ItemStack(Material.DIAMOND_PICKAXE);
    public static ItemStack blocksPlacedLabelItem = new ItemStack(Material.BRICK);
    private static HashMap<UUID, Integer> placedBlocksList = new HashMap<>();
    private static HashMap<UUID, Integer> brokenBlocksList = new HashMap<>();
    private static HashMap<UUID, Long> joinTime = new HashMap<>();
    private static HashMap<UUID, Long> playedTime = new HashMap<>();
    private static Inventory bouwServerInventory;
    private static Inventory getTop10Inventory;

    /*
    Save the time in milliseconds that the player joined
     */
    public static void setJoinTime(OfflinePlayer player) {
        joinTime.put(player.getUniqueId(), System.currentTimeMillis());
    }

    /*
    Get the time that the player joined
     */
    public static Long getJoinTime(OfflinePlayer player) {
        return joinTime.get(player.getUniqueId());
    }

    /*
    Set the played time
     */
    public static void setPlayedTime(OfflinePlayer player) {
        Long playedTimeInMs = (System.currentTimeMillis() - getJoinTime(player)) + getPlayedTime(player);

        /*
        Reset the joined time
         */
        setJoinTime(player);

        /*
        Save the played time in HashMap
         */
        playedTime.put(player.getUniqueId(), playedTimeInMs);
    }

    /*
    Get the played time in milliseconds
     */
    public static Long getPlayedTime(OfflinePlayer player) {
        return playedTime.get(player.getUniqueId());
    }

    /*
    Get the amount of broken blocks
     */
    public static Integer getBrokenBlocks(OfflinePlayer player) {

        if (brokenBlocksList.containsKey(player.getUniqueId())) {
            return brokenBlocksList.get(player.getUniqueId());
        } else {
            return 0;
        }
    }

    /*
    Get the amount of placed blocks
     */
    public static Integer getPlacedBlocks(OfflinePlayer player) {

        if (placedBlocksList.containsKey(player.getUniqueId())) {
            return placedBlocksList.get(player.getUniqueId());
        } else {
            return 0;
        }
    }

    /*
    Set the amount of broken blocks
     */
    public static void setBrokenBlocks(OfflinePlayer player, int amount) {
        brokenBlocksList.put(player.getUniqueId(), amount);
    }

    /*
    Set the amount of placed blocks
     */
    public static void setPlacedBlocks(OfflinePlayer player, int amount) {
        placedBlocksList.put(player.getUniqueId(), amount);
    }

    /*
    Create the inventory items
     */
    public static void createItems(OfflinePlayer Offlineplayer) {

        if (!Offlineplayer.isOnline()) {
            savePlayerStatsToCache(Offlineplayer);
            setJoinTime(Offlineplayer);
        }

        String hoursPlayed = PlayerStats.playedTimeToHours(Offlineplayer);
        int blocksPlaced = PlayerStats.getPlacedBlocks(Offlineplayer);
        int blocksBroken = PlayerStats.getBrokenBlocks(Offlineplayer);
        int commandsPeformed = CommandHistoryMenu.getCommandAmount(Offlineplayer);


        String pluginDateReleased = "23/03/2019 23:33:53";

        /*
        Player stats
         */
        SkullMeta playerStatsItemMeta = (SkullMeta) playerStatsItem.getItemMeta();
        playerStatsItemMeta.setDisplayName(ChatColor.GOLD + Offlineplayer.getName());
        playerStatsItemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(Offlineplayer.getUniqueId()));
        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.YELLOW + "UUID: " + ChatColor.GOLD + Offlineplayer.getUniqueId());
        itemLore.add(ChatColor.YELLOW + "U hebt " + ChatColor.GOLD + blocksPlaced + ChatColor.YELLOW + " blokken geplaatst!");
        itemLore.add(ChatColor.YELLOW + "U hebt " + ChatColor.GOLD + blocksBroken + ChatColor.YELLOW + " blokken gebroken!");
        itemLore.add(ChatColor.YELLOW + "U hebt " + ChatColor.GOLD + commandsPeformed + ChatColor.YELLOW + " commando's uitgevoerd!");
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
        itemLore.add(ChatColor.YELLOW + "Versie: " + ChatColor.GOLD + Bukkit.getServer().getVersion());
        itemLore.add(ChatColor.YELLOW + "Werelden: ");
        for (World world : Bukkit.getServer().getWorlds()) {
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

    public static void createLeaderboardInventory(Player sender) {
        /*
        Get data Async since we are aquiring alot of data
         */
                /*
                Load data of all players
                */

        HashMap<UUID, Long> playedTime = new HashMap<>();
        HashMap<UUID, Integer> blocksPlaced = new HashMap<>();
        HashMap<UUID, Integer> blocksBroken = new HashMap<>();

        for (String uuid : Main.getInstance().getPlayerCache().getConfigurationSection("Data").getKeys(false)) {
            playedTime.put(UUID.fromString(uuid), Long.parseLong(Main.getInstance().getPlayerCache().getString("Data." + uuid + ".PLAYED_MILISECONDS")));
            blocksPlaced.put(UUID.fromString(uuid), (int) Main.getInstance().getPlayerCache().get("Data." + uuid + ".BLOCKS_PLACED"));
            blocksBroken.put(UUID.fromString(uuid), (int) Main.getInstance().getPlayerCache().get("Data." + uuid + ".BLOCKS_BROKEN"));
        }

                /*
                Sort hashmaps
                */
        HashMap<UUID, Long> sortedPlayedTime = playedTime.entrySet().stream()
                .sorted((Map.Entry.<UUID, Long>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        HashMap<UUID, Integer> sortedBlocksBroken = blocksBroken.entrySet().stream()
                .sorted((Map.Entry.<UUID, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        HashMap<UUID, Integer> sortedBlocksPlaced = blocksPlaced.entrySet().stream()
                .sorted((Map.Entry.<UUID, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        ItemMeta playedTimeLabelMeta = timePlayedLabelItem.getItemMeta();
        playedTimeLabelMeta.setDisplayName(ChatColor.GOLD + "Gespeelde tijd");
        timePlayedLabelItem.setItemMeta(playedTimeLabelMeta);

        ItemMeta placedBlocksLabelMEta = blocksPlacedLabelItem.getItemMeta();
        placedBlocksLabelMEta.setDisplayName(ChatColor.GOLD + "Geplaatste blokken");
        blocksPlacedLabelItem.setItemMeta(placedBlocksLabelMEta);

        ItemMeta blocksBrokenLabelMeta = blocksBrokenLabelItem.getItemMeta();
        blocksBrokenLabelMeta.setDisplayName(ChatColor.GOLD + "Gebroken blokken");
        blocksBrokenLabelItem.setItemMeta(blocksBrokenLabelMeta);

        getTop10Inventory = Bukkit.createInventory(null, 54, ChatColor.GOLD + "" + ChatColor.BOLD + "Leaderboard");

        getTop10Inventory.setItem(0, timePlayedLabelItem);
        getTop10Inventory.setItem(9, timePlayedLabelItem);
        getTop10Inventory.setItem(18, blocksPlacedLabelItem);
        getTop10Inventory.setItem(27, blocksPlacedLabelItem);
        getTop10Inventory.setItem(36, blocksBrokenLabelItem);
        getTop10Inventory.setItem(45, blocksBrokenLabelItem);

        int i = 1;
        for (UUID key : sortedPlayedTime.keySet()) {

            if (i == 9) {
                i = 10;
            }

            if (i == 18) {
                break;
            }

            OfflinePlayer player = Bukkit.getOfflinePlayer(key);
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            headMeta.setOwningPlayer(player);
            headMeta.setDisplayName(ChatColor.GOLD + "#" + i + ": " + ChatColor.YELLOW + player.getName());
            List<String> lore = new ArrayList<>();
            Long millis = sortedPlayedTime.get(key);
            lore.add(ChatColor.GOLD + "Gespeeld: " + ChatColor.YELLOW + String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));
            headMeta.setLore(lore);
            head.setItemMeta(headMeta);
            getTop10Inventory.setItem(i, head);

            i++;
        }

        for (UUID key : sortedBlocksPlaced.keySet()) {

            if (i == 27) {
                i = 28;
            }

            if (i == 18) {
                i = 19;
            }

            if (i == 36) {
                break;
            }

            OfflinePlayer player = Bukkit.getOfflinePlayer(key);
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            headMeta.setOwningPlayer(player);
            headMeta.setDisplayName(ChatColor.GOLD + "#" + (i - 18) + ": " + ChatColor.YELLOW + player.getName());
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Blokken geplaatst: " + ChatColor.YELLOW + sortedBlocksPlaced.get(key));
            headMeta.setLore(lore);
            head.setItemMeta(headMeta);
            getTop10Inventory.setItem(i, head);
            i++;
        }

        for (UUID key : sortedBlocksBroken.keySet()) {

            if (i == 45) {
                i = 46;
            }

            if (i == 36) {
                i = 37;
            }

            if (i == 54) {
                break;
            }

            OfflinePlayer player = Bukkit.getOfflinePlayer(key);
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            headMeta.setOwningPlayer(player);
            headMeta.setDisplayName(ChatColor.GOLD + "#" + (i - 36) + ": " + ChatColor.YELLOW + player.getName());
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Blokken gebroken: " + ChatColor.YELLOW + sortedBlocksBroken.get(key));
            headMeta.setLore(lore);
            head.setItemMeta(headMeta);
            getTop10Inventory.setItem(i, head);
            i++;
        }


        sender.openInventory(getTop10Inventory);
    }

    /*
    Create the inventory so it is viewable for the player
     */
    public static void createInvenory(Player sender, OfflinePlayer target) {

        /*
        If Player is offline
         */
        if (!target.isOnline()) {
            PlayerStats.playedTime.put(target.getUniqueId(), Long.parseLong(Main.getInstance().getPlayerCache().getString("Data." + target.getUniqueId().toString() + ".PLAYED_MILISECONDS")));
            PlayerStats.setBrokenBlocks(target, (int) Main.getInstance().getPlayerCache().get("Data." + target.getUniqueId().toString() + ".BLOCKS_BROKEN"));
            PlayerStats.setPlacedBlocks(target, (int) Main.getInstance().getPlayerCache().get("Data." + target.getUniqueId().toString() + ".BLOCKS_PLACED"));
        }
        /*
        Create the itemes for the inventory
         */
        createItems(target);

        /*
        Assign the inventory instance to the variabel
         */
        bouwServerInventory = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Onameril Bouwserver Plugin");

        /*
        Add the items to the inventory
         */
        bouwServerInventory.setItem(1, playerStatsItem);
        bouwServerInventory.setItem(4, serverStatsItem);
        bouwServerInventory.setItem(7, pluginStatsItem);

        /*
        Open the bouwserver inventory so it is
        viewable for the player
         */
        sender.openInventory(bouwServerInventory);
    }

    /*
    Get the bouwserver inventory
     */
    public static Inventory getInventory() {
        return bouwServerInventory;
    }

    /*
    Save the player stats to the playercache.yml
     */
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
                } catch (IOException exc) {
                }
            }
        });
    }

    /*
    Retrieve the PlayerStats from playercache.yml to the Storage
     */
    public static void savePlayerStatsToCache(OfflinePlayer player) {

        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                PlayerStats.playedTime.put(player.getUniqueId(), Long.parseLong(Main.getInstance().getPlayerCache().getString("Data." + player.getUniqueId().toString() + ".PLAYED_MILISECONDS")));
                PlayerStats.setBrokenBlocks(player, (int) Main.getInstance().getPlayerCache().get("Data." + player.getUniqueId().toString() + ".BLOCKS_BROKEN"));
                PlayerStats.setPlacedBlocks(player, (int) Main.getInstance().getPlayerCache().get("Data." + player.getUniqueId().toString() + ".BLOCKS_PLACED"));
            }
        }, 10 * 1);
    }

    /*
    Convert the played miliseconds into a
    hour:minute:second time-structure
     */
    public static String playedTimeToHours(OfflinePlayer player) {

        setPlayedTime(player);
        Long millis = getPlayedTime(player);
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    /*
    Interval to save the player stats to the playercache.yml
     */
    public static void savePlayerStatsInterval(int interval) {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    Main.getInstance().getPlayerCache().set("Data." + players.getUniqueId().toString() + ".BLOCKS_BROKEN", PlayerStats.getBrokenBlocks(players));
                    Main.getInstance().getPlayerCache().set("Data." + players.getUniqueId().toString() + ".BLOCKS_PLACED", PlayerStats.getPlacedBlocks(players));
                    PlayerStats.setPlayedTime(players);
                    Main.getInstance().getPlayerCache().set("Data." + players.getUniqueId().toString() + ".PLAYED_MILISECONDS", PlayerStats.getPlayedTime(players).toString());

                    try {
                        Main.getInstance().getPlayerCache().save(Main.getInstance().getPlayerCacheFile());
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        }, 20 * interval, 20 * interval);
    }
}
