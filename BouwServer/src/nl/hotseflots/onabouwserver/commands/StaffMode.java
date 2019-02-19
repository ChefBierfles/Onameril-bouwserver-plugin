package nl.hotseflots.onabouwserver.commands;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.boss.CraftBossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StaffMode implements CommandExecutor {

    public static HashMap<String, ItemStack[]> savedContents = new HashMap<>();
    public static List<String> staffModeList = new ArrayList<>();
    public static List<String> vanishedList = new ArrayList<>();
    public static List<String> frozenPlayerList = new ArrayList<>();

    public static ItemStack vanishOffItem = new ItemStack(Material.NETHER_STAR, 1);
    public static ItemStack vanishOnItem = new ItemStack(Material.NETHER_STAR, 1);
    public static ItemStack lookupItem = new ItemStack(Material.WATCH, 1);
    public static ItemStack freezeItem = new ItemStack(Material.LEASH, 1);
    public static ItemStack randomTPItem = new ItemStack(Material.ENDER_PEARL, 1);
    public static ItemStack inventorySeeItem = new ItemStack(Material.CHEST, 1);
    public static ItemStack switchGameModeItem = new ItemStack(Material.IRON_PICKAXE, 1);

    private static CraftBossBar vanishCrossbar = new CraftBossBar(ChatColor.GREEN + "Skidaddle, Skidoodle your vanished!", BarColor.PURPLE, BarStyle.SOLID);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        /*
        Check whatever the commandSender is a player since the functions
        we use will only be available by a Player and not the console
         */
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Dit commando kan enkel in-game uitgevoerd worden!");
            return false;
        }

        Player player = (Player) commandSender;

        /*
        Toggle the staffmode when /staffmode is peformed
         */
        if (command.getName().equalsIgnoreCase("staffmode")) {
            if (commandSender.hasPermission("bouwserver.commands.staffmode")) {
                if (staffModeList.contains(player.getUniqueId().toString())) {
                    player.sendMessage(Messages.STAFF_TAG + "staffmode disabled");
                    LeaveStaffMode(player);
                    return true;
                } else {
                    player.sendMessage(Messages.STAFF_TAG + "staffmode enabled");
                    EnterStaffMode(player);
                    return true;
                }
            }
        }

        return false;
    }

    /*
    Let the player enter staffmode

    Player will be added to the staffmodeList, inventory will be
    cleared and saved and the player will be invisible to others
     */
    public static void EnterStaffMode(Player player) {
        if (!Buildmode.buildModeList.contains(player.getUniqueId().toString())) {
            if (!staffModeList.contains(player.getUniqueId().toString())) {
                staffModeList.add(player.getUniqueId().toString());
            }
            SaveInventory(player);
            GiveStaffItems(player);
            ToggleVanish(player, "on");
        } else {
            ActionBarAPI.sendActionBar(player.getPlayer(), ChatColor.RED + "Je kan niet in staffmode als je in buildmode zit!");
        }
    }

    /*
    Let the player leave staffmode

    Player will be removed from the staffmodeList, inventory will be
    restored and the player will be visible again.
     */
    public static void LeaveStaffMode(Player player) {
        staffModeList.remove(player.getUniqueId().toString());
        ToggleVanish(player, "off");
        RestoreInventory(player);
        player.setGameMode(GameMode.CREATIVE);
    }

    /*
    Generate the staffitems so we can give it to the player
    that went into staff mode
     */
    public static void GiveStaffItems(Player player) {

        //Vanish Off Item
        List<String> vanishOffItemLore = new ArrayList<>();
        vanishOffItemLore.add(ChatColor.DARK_PURPLE + "Left or right click to");
        vanishOffItemLore.add(ChatColor.DARK_PURPLE + "toggle vanish mode");
        ItemMeta vanishOffItemMeta = vanishOffItem.getItemMeta();
        vanishOffItemMeta.setLore(vanishOffItemLore);
        vanishOffItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 10, true);
        vanishOffItemMeta.setDisplayName("Vanish Mode: " + ChatColor.RED + "Off");
        vanishOffItem.setItemMeta(vanishOffItemMeta);

        //Vanish on Item
        ItemMeta vanishOnItemMeta = vanishOnItem.getItemMeta();
        vanishOnItemMeta.setLore(vanishOffItemLore);
        vanishOnItemMeta.addEnchant(Enchantment.ARROW_INFINITE, 10, true);
        vanishOnItemMeta.setDisplayName("Vanish Mode: " + ChatColor.GREEN + "On");
        vanishOnItem.setItemMeta(vanishOnItemMeta);

        //Lookup Player
        List<String> lookupItemLore = new ArrayList<>();
        lookupItemLore.add(ChatColor.DARK_PURPLE + "Left or right click a player to");
        lookupItemLore.add(ChatColor.DARK_PURPLE + "open up the action menu!");
        ItemMeta lookupItemMeta = lookupItem.getItemMeta();
        lookupItemMeta.setDisplayName(ChatColor.GOLD + "Lookup CommandHistory");
        lookupItemMeta.setLore(lookupItemLore);
        lookupItem.setItemMeta(lookupItemMeta);

        //Freeze Player
        List<String> freezeItemLore = new ArrayList<>();
        freezeItemLore.add(ChatColor.DARK_PURPLE + "Left or right click a player to");
        freezeItemLore.add(ChatColor.DARK_PURPLE + "freeze them completly!");
        ItemMeta freezeItemMeta = freezeItem.getItemMeta();
        freezeItemMeta.setDisplayName(ChatColor.AQUA + "Freeze Player");
        freezeItemMeta.setLore(freezeItemLore);
        freezeItem.setItemMeta(freezeItemMeta);

        //Random TP
        List<String> randomTPItemLore = new ArrayList<>();
        randomTPItemLore.add(ChatColor.DARK_PURPLE + "Left or right click to");
        randomTPItemLore.add(ChatColor.DARK_PURPLE + "randomly tp to a player!");
        ItemMeta randomTPItemMeta = randomTPItem.getItemMeta();
        randomTPItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Random tp");
        randomTPItemMeta.setLore(randomTPItemLore);
        randomTPItem.setItemMeta(randomTPItemMeta);

        //Inventorysee Item
        List<String> inventorySeeItemLore = new ArrayList<>();
        inventorySeeItemLore.add(ChatColor.DARK_PURPLE + "Left or right click a player to");
        inventorySeeItemLore.add(ChatColor.DARK_PURPLE + "open-up the players inventory!");
        ItemMeta inventorySeeItemMeta = inventorySeeItem.getItemMeta();
        inventorySeeItemMeta.setDisplayName(ChatColor.GOLD + "Inventory See");
        inventorySeeItemMeta.setLore(inventorySeeItemLore);
        inventorySeeItem.setItemMeta(inventorySeeItemMeta);

        //Gamemode switch Item
        List<String> switchGameModeItemLore = new ArrayList<>();
        switchGameModeItemLore.add(ChatColor.DARK_PURPLE + "Left or right click to");
        switchGameModeItemLore.add(ChatColor.DARK_PURPLE + "switch between gamemodes");
        ItemMeta switchGameModeItemMeta = switchGameModeItem.getItemMeta();
        switchGameModeItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Gamemode Switcher");
        switchGameModeItemMeta.setLore(switchGameModeItemLore);
        switchGameModeItem.setItemMeta(switchGameModeItemMeta);

        //Give staff-items to player
        player.getInventory().setItem(0, vanishOnItem);
        player.getInventory().setItem(1, lookupItem);
        player.getInventory().setItem(2, freezeItem);
        player.getInventory().setItem(3, randomTPItem);
        player.getInventory().setItem(4, inventorySeeItem);
        player.getInventory().setItem(8, switchGameModeItem);
    }

    /*
    Toggle hide the player from other players. Also give the vanished player
    a bossbar to notify if he is vanished and make him invulnerable!
     */
    public static void ToggleVanish(Player player, String mode) {
        if (mode.equalsIgnoreCase("on")) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (!players.getName().equalsIgnoreCase(player.getName())) {
                    if (players.hasPermission("bouwserver.commands.staffmodee")) {
                        players.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GRAY + " has vanished. POOF!");
                    }
                }
            }
            //Add: Player from the vanished list
            vanishedList.add(player.getUniqueId().toString());
            //Add: Player will be set in godmode
            player.setInvulnerable(true);

            //Add: Hide player from online players
            for (Player hideFromPlayer : Bukkit.getOnlinePlayers()) {
                if (!hideFromPlayer.hasPermission("bouwserver.commands.staffmodee")) {
                    if (!hideFromPlayer.getName().equals(player.getName())) {
                        hideFromPlayer.hidePlayer(Main.plugin, player);
                    }
                }
            }
            //Add: Boss-bar for vanished person so he knows he is vanished
            vanishCrossbar.addPlayer(player);

            //Set item to vanish on
            player.getInventory().setItem(0, vanishOnItem);
        } else if (mode.equalsIgnoreCase("off")) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (!players.getName().equalsIgnoreCase(player.getName())) {
                    if (players.hasPermission("bouwserver.commands.staffmode")) {
                        players.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GRAY + " left vanish mode.");
                    }
                }
            }
            //Remove: Player from the vanished list
            vanishedList.remove(player.getUniqueId().toString());

            //Remove; Player will be set in godmode
            player.setInvulnerable(false);

            //Remove: Hiding player for online players
            for (Player showForPlayer : Bukkit.getOnlinePlayers()) {
                if (!showForPlayer.hasPermission("bouwserver.commands.staffmode")) {
                    showForPlayer.showPlayer(Main.plugin, player);
                }
            }
            //Remove: Boss-bar to notify your vanished
            vanishCrossbar.removePlayer(player);

            //Set item to vanish off
            player.getInventory().setItem(0, vanishOffItem);
        }

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
    Check if the given player is currently in staff mode.
     */
    public static boolean isPlayerInStaffMode(Player player) {
        if (staffModeList.contains(player.getUniqueId().toString())) {
            return true;
        }
        return false;
    }

    /*
    Check whatever the item is a staff item
    */
    public static boolean isStaffItem(ItemStack staffItem) {
        List<ItemStack> staffItemList = new ArrayList<>();
        staffItemList.add(vanishOffItem);
        staffItemList.add(vanishOnItem);
        staffItemList.add(randomTPItem);
        staffItemList.add(lookupItem);
        staffItemList.add(freezeItem);
        staffItemList.add(inventorySeeItem);
        staffItemList.add(switchGameModeItem);

        for (ItemStack searchStaffItem : staffItemList) {
            if (searchStaffItem.isSimilar(staffItem)) {
                return true;
            }
        }
        return false;
    }

    /*
    Rehide the player that currently is in vanish
     */
    @Deprecated
    public static void reHidePlayer(Player player) {
        for (Player showForPlayer : Bukkit.getOnlinePlayers()) {
            showForPlayer.showPlayer(Main.plugin, player);
        }
    }
}
