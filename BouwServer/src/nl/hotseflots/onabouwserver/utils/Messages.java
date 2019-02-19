package nl.hotseflots.onabouwserver.utils;

import nl.hotseflots.onabouwserver.Main;
import org.bukkit.ChatColor;

public class Messages {

    public static String SERVER_TAG = ChatColor.GRAY + "[" + ChatColor.GOLD + "Bouwserver" + ChatColor.GRAY + "] " + ChatColor.WHITE;
    public static String SERVER_ERROR_TAG = ChatColor.GRAY + "[" + ChatColor.DARK_RED + "Bouwserver" + ChatColor.GRAY + "] " + ChatColor.RED;
    public static String STAFF_TAG = ChatColor.GRAY + "[" + ChatColor.GOLD + "Staff" + ChatColor.GRAY + "] " + ChatColor.WHITE;
    public static String MCAUTH_LOGIN = ChatColor.translateAlternateColorCodes('&', Main.plugin.getTwoFACFG().getString("messages.login"));
    public static String MCAUTH_FAIL_MESSAGE = ChatColor.translateAlternateColorCodes('&', Main.plugin.getTwoFACFG().getString("messages.fail-message"));
    public static String MCAUTH_INVALID_CODE = ChatColor.translateAlternateColorCodes('&', Main.plugin.getTwoFACFG().getString("messages.invalid-code"));
    public static String MCAUTH_VALID_CODE = ChatColor.translateAlternateColorCodes('&', Main.plugin.getTwoFACFG().getString("messages.valid-code"));
    public static String MCAUTH_SETUP_VALIDATE = ChatColor.translateAlternateColorCodes('&', Main.plugin.getTwoFACFG().getString("messages.setup-validate"));
    public static String MCAUTH_SETUP_ALREADY_ENABLED = ChatColor.translateAlternateColorCodes('&', Main.plugin.getTwoFACFG().getString("messages.setup-already-enabled"));
    public static String MCAUTH_SETUP_FAIL = ChatColor.translateAlternateColorCodes('&', Main.plugin.getTwoFACFG().getString("messages.setup-fail"));
    public static String MCAUTH_SETUP_QRMAP = ChatColor.translateAlternateColorCodes('&', Main.plugin.getTwoFACFG().getString("messages.setup-qrmap"));
    public static String MCAUTH_SETUP_CODE = ChatColor.translateAlternateColorCodes('&', Main.plugin.getTwoFACFG().getString("messages.setup-code"));
}
