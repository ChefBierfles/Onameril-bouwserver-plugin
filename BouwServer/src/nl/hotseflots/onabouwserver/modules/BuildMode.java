package nl.hotseflots.onabouwserver.modules;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.StaffUtils.StaffMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.craftbukkit.v1_12_R1.boss.CraftBossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BuildMode {

    public static List<UUID> buildModeList = new ArrayList<>();
    public static HashMap<UUID, ItemStack[]> savedContents = new HashMap<>();
    private static int task;

    private static CraftBossBar vanishCrossbar = new CraftBossBar(ChatColor.AQUA + "Skidaddle, Skidoodle your now in BuildMode!", BarColor.BLUE, BarStyle.SOLID);

    public static void LeaveBuildMode(Player player) {
        buildModeList.remove(player.getUniqueId());
        ActionBarAPI.sendActionBar(player.getPlayer(), ChatColor.RED + "");
        vanishCrossbar.removePlayer(player);

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            onlinePlayers.showPlayer(Main.getInstance(), player);
            RestoreInventory(player);
        }
    }

    public static void EnterBuildMode(Player player) {
        if (!StaffMode.playersInStaffMode.contains(player.getUniqueId())) {
            buildModeList.add(player.getUniqueId());
            vanishCrossbar.addPlayer(player);
            task = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (buildModeList.contains(player.getUniqueId())) {
                        ActionBarAPI.sendActionBar(player, ChatColor.GOLD + "! ! !" + ChatColor.AQUA + " Je zit momenteel in buildmode " + ChatColor.GOLD + "! ! !");
                    } else {
                        Bukkit.getScheduler().cancelTask(task);
                    }
                }
            }, 0, 20*2);
        } else {
            ActionBarAPI.sendActionBar(player.getPlayer(), ChatColor.RED + "");
            ActionBarAPI.sendActionBar(player.getPlayer(), ChatColor.RED + "Je kan niet in buildmode als je in staffmode zit!");
            return;
        }

        //Toggle buildmode vanish on
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayers.hasPermission("bouwserver.commands.staffmode")) {
                onlinePlayers.hidePlayer(Main.getInstance(), player);
            }
        }

        SaveInventory(player);
    }

    /*
    Retrieve the players inventory from memory and restore it.
    */
    public static void RestoreInventory(Player player) {
        //Restore inventory and remove it from hashmap
        player.getInventory().clear();
        player.getInventory().setContents(savedContents.get(player.getUniqueId()));
        savedContents.remove(player.getUniqueId(), player.getInventory().getContents());
    }

    /*
    Saving the player's inventory in memory so we can restore it later
    */
    public static void SaveInventory(Player player) {
        if (savedContents.containsKey(player.getUniqueId())) {
            savedContents.remove(player.getUniqueId());
        }
        savedContents.put(player.getUniqueId(), player.getInventory().getContents());
        player.getInventory().clear();
    }

}
