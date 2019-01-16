package com.hotseflots.bouwserver.utils;

import org.bukkit.ChatColor;

public class Messages {

    public static String SERVER_TAG = ChatColor.GRAY + "[" + ChatColor.GOLD + "Bouwserver" + ChatColor.GRAY + "] " + ChatColor.WHITE;
    public static String USERMANAGER_TAG = ChatColor.GRAY + "[" + ChatColor.GOLD + "User-Manager" + ChatColor.GRAY + "] " + ChatColor.WHITE;

    public static String KICKED_MSG(String executor, String reason) {
        return ChatColor.WHITE + "U bent van de server gekickt door " + ChatColor.GOLD + executor + ChatColor.WHITE + " met als reden:\n" + "\"" + ChatColor.GOLD + reason + ChatColor.WHITE +"\".";
    }

    public static String KICK_MSG(String executor, String target, String reason) {
        return "\nSpeler '" + ChatColor.GOLD + target + ChatColor.WHITE + "' is van de server gekickt door '" + ChatColor.GOLD + executor + ChatColor.WHITE + "' \nReden: \"" + ChatColor.GOLD + reason + ChatColor.WHITE + "\".";
    }

    public static String MUTE_MSG(String executor, String target, String reason ,String expireDate) {
        return "\nSpeler '" + ChatColor.GOLD + target + ChatColor.WHITE + "' is gemute door '" + ChatColor.GOLD + executor + ChatColor.WHITE + "' \nReden: \"" + ChatColor.GOLD + reason + ChatColor.WHITE + "\".\nVerloopt op: " + expireDate;
    }

    public static String UNMUTE_MSG(String target) {
        return "\nSpeler '" + ChatColor.GOLD + target + ChatColor.WHITE + "' kan weer praten.";
    }

    public static String FREEZED_MSG(String executor, String target, String reason) {
        return "\nSpeler '" + ChatColor.GOLD + target + ChatColor.WHITE + "' is bevroren door '" + ChatColor.GOLD + executor + ChatColor.WHITE + ".";
    }

    public static String UNFREEZED_MSG(String target) {
        return "\nSpeler '" + ChatColor.GOLD + target + ChatColor.WHITE + "' kan weer bewegen.";
    }

    public static String BANNED_MSG(String executor, String reason, String timeString) {
        return ChatColor.WHITE + "U bent van de server verbannen door " + ChatColor.GOLD + executor + ChatColor.WHITE + " met als reden:\n" + "\"" + ChatColor.GOLD + reason + ChatColor.WHITE +"\". Deze ban verloopt op: " + ChatColor.GOLD + timeString + ChatColor.WHITE + ".";
    }

    public static String BAN_MSG(String executor, String target, String reason, String timeString) {
        return "\nSpeler '" + ChatColor.GOLD + target + ChatColor.WHITE + "' is van de server verbannen door '" + ChatColor.GOLD + executor + ChatColor.WHITE + "' \nReden: \"" + ChatColor.GOLD + reason + ChatColor.WHITE + "\".\nVerloopt op: " + ChatColor.GOLD + timeString + ChatColor.WHITE + ".";
    }

    public static String UNBAN_MSG(String target) {
        return "\nSpeler '" + ChatColor.GOLD + target + ChatColor.WHITE + "' is weer toegelaten tot de server";
    }
}
