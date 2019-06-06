package nl.hotseflots.onabouwserver.utils;

import nl.hotseflots.onabouwserver.Main;
import org.bukkit.ChatColor;

public enum Messages
{
    STAFF_TAG("Messages.STAFF_TAG"),
    STAFF_CHAT("Messages.STAFF_CHAT"),
    SERVER_TAG("Messages.SERVER_TAG"),
    JOIN_MSG("Messages.JOIN_MSG"),
    QUIT_MSG("Messages.QUIT_MSG"),
    MOTD_MSG("Messages.WELCOME_MSG"),
    MCAUTH_LOGIN("Messages.TwoFA_LOGIN_MSG"),
    MCAUTH_FAIL_MESSAGE("Messages.TwoFA_FAIL_MSG"),
    MCAUTH_INVALID_CODE("Messages.TwoFA_INVALID_MSG"),
    MCAUTH_VALID_CODE("Messages.TwoFA_VALID_MSG"),
    MCAUTH_SETUP_VALIDATE("Messages.TwoFA_SETUP_VALIDATE_MSG"),
    MCAUTH_SETUP_ALREADY_ENABLED("Messages.TwoFA_SETUP_ALREADY_ENABLED_MSG"),
    MCAUTH_SETUP_FAIL("Messages.TwoFA_SETUP_FAIL_MSG"),
    MCAUTH_SETUP_QRMAP("Messages.TwoFA_SETUP_QRMAP_MSG"),
    MCAUTH_SETUP_CODE("Messages.TwoFA_SETUP_MSG"),
    MCAUTH_QRMAP_NAME("Messages.TwoFA_QRMAP_NAME"),
    SERVER_CLOSING_MSG("Messages.SERVERCLOSING_MSG"),
    STAFF_CHAT_FORMAT("");

    private static Main main;
    private String path;

    Messages(String string) {
        path = string;
    }

    public static void init(Main main) {
        Messages.main = main;
    }

    public String getMessage() {
        if (Main.getInstance().getConfig().getString(path) != null) {
            return ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString(path));
        }
        return null;
    }
}
