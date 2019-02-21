package nl.hotseflots.onabouwserver.modules;

import jdk.nashorn.internal.ir.CatchNode;
import net.minecraft.server.v1_12_R1.ItemMapEmpty;
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
import java.util.List;

public class Bouwserver {

    public static ItemStack playerHead = new ItemStack(Material.SKULL_ITEM);
    public static ItemStack serverStatsItem = new ItemStack(Material.GOLD_PICKAXE);
    public static ItemStack pluginStatsItem = new ItemStack(Material.NAME_TAG);

    public static void createItems(Player player) {

        /*
        Place holders
         */
        int hoursPlayed = 1;
        int blocksPlaced = 1;
        int blocksBroken = 1;
        String pluginDateReleased = "21/02/2019 16:30:00";

        /*
        Player stats
         */
        SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
        playerHeadMeta.setDisplayName(ChatColor.GOLD + player.getName());
        playerHeadMeta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.YELLOW + "Je UUID is: " + ChatColor.GOLD + player.getUniqueId());
        itemLore.add(ChatColor.YELLOW + "Je hebt " + ChatColor.GOLD + hoursPlayed + ChatColor.YELLOW + " uren gespeeld!");
        itemLore.add(ChatColor.YELLOW + "Je hebt " + ChatColor.GOLD + blocksPlaced + ChatColor.YELLOW + " blokken geplaatst!");
        itemLore.add(ChatColor.YELLOW + "Je hebt " + ChatColor.GOLD + blocksPlaced + ChatColor.YELLOW + " blokken gebroken!");
        playerHeadMeta.setLore(itemLore);
        playerHead.setItemMeta(playerHeadMeta);
        itemLore.clear();

        /*
        Server stats
         */
        ItemMeta serverStatsItemItemMeta = Bouwserver.serverStatsItem.getItemMeta();
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
    }

    public static void createInvenory() {
        Inventory bouwServerInventory = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Onameril Bouwserver Plugin");
    }
}
