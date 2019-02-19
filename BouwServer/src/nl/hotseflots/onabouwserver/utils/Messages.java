package nl.hotseflots.onabouwserver.utils;

import org.bukkit.ChatColor;

public class Messages {

    public static String SERVER_TAG = ChatColor.GRAY + "[" + ChatColor.GOLD + "Bouwserver" + ChatColor.GRAY + "] " + ChatColor.WHITE;
    public static String SERVER_ERROR_TAG = ChatColor.GRAY + "[" + ChatColor.DARK_RED + "Bouwserver" + ChatColor.GRAY + "] " + ChatColor.RED;
    public static String CommandSpyMessage(String player, String message) {
        return ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + ChatColor.GOLD + "" + ChatColor.ITALIC + player + ChatColor.GRAY + "" + ChatColor.ITALIC + " used the command " + ChatColor.GOLD + "" + ChatColor.ITALIC + message + ChatColor.GRAY + "" + ChatColor.ITALIC + "]";
    }
    public static String STAFF_TAG = ChatColor.GRAY + "[" + ChatColor.GOLD + "Staff" + ChatColor.GRAY + "] " + ChatColor.WHITE;

}
