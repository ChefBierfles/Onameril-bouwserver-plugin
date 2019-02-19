package nl.hotseflots.onabouwserver.commands;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import nl.hotseflots.onabouwserver.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.boss.CraftBossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Buildmode implements CommandExecutor {

    public static List<String> buildModeList = new ArrayList<>();
    public static HashMap<String, ItemStack[]> savedContents = new HashMap<>();

    private static CraftBossBar vanishCrossbar = new CraftBossBar(ChatColor.GREEN + "Skidaddle, Skidoodle your now in BuildMode!", BarColor.BLUE, BarStyle.SOLID);

    public static void LeaveBuildMode(Player player) {
        buildModeList.remove(player.getUniqueId().toString());
        vanishCrossbar.removePlayer(player);

        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayers.hasPermission("bouwserver.commands.staffmode")) {
                onlinePlayers.showPlayer(Main.plugin, player);
                RestoreInventory(player);
            }
        }
    }

    public static void EnterBuildMode(Player player) {
        buildModeList.add(player.getUniqueId().toString());
        vanishCrossbar.addPlayer(player);

        //Toggle buildmode vanish on
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayers.hasPermission("bouwserver.commands.staffmode")) {
                onlinePlayers.hidePlayer(Main.plugin, player);
                SaveInventory(player);
            }
        }
    }

    /*
    Retrieve the players inventory from memory and restore it.
    */
    public static void RestoreInventory(Player player) {
        //Restore inventory and remove it from hashmap
        player.getInventory().clear();
        player.getInventory().setContents(savedContents.get(player.getUniqueId().toString()));
        savedContents.remove(player.getUniqueId().toString(), player.getInventory().getContents());
    }

    /*
    Saving the player's inventory in memory so we can restore it later
    */
    public static void SaveInventory(Player player) {
        if (savedContents.containsKey(player.getUniqueId().toString())) {
            savedContents.remove(player.getUniqueId().toString());
        }
        savedContents.put(player.getUniqueId().toString(), player.getInventory().getContents());
        player.getInventory().clear();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        /*
        Check whatever the commandSender is a player since the functions
        we use will only be available by a Player and not the console
        */
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Deze commando kan enkel in-game uitgevoerd worden!");
            return false;
        }
        Player player = (Player) commandSender;

        /*
        Toggle the staffmode when /staffmode is peformed
         */
        if (command.getName().equalsIgnoreCase("buildmode")) {
            if (commandSender.hasPermission("bouwserver.commands.buildmode")) {
                if (StaffMode.staffModeList.contains(player.getUniqueId().toString())) {
                    ActionBarAPI.sendActionBar(player, ChatColor.RED + "Je kan niet in buildmode als je in staffmode zit!");
                    return false;
                }
                if (buildModeList.contains(player.getUniqueId().toString())) {
                    ActionBarAPI.sendActionBar(player.getPlayer(), "Buildmode " + ChatColor.RED + "disabled" + ChatColor.WHITE + "!");
                    LeaveBuildMode(player);
                    return true;
                } else {
                    ActionBarAPI.sendActionBar(player.getPlayer(), "Buildmode " + ChatColor.GREEN + "enabled" + ChatColor.WHITE + "!");
                    EnterBuildMode(player);
                    return true;
                }
            }
        }
        return false;
    }
}
