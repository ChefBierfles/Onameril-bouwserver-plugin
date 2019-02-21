package nl.hotseflots.onabouwserver.utils;

import nl.hotseflots.onabouwserver.Main;
import org.bukkit.ChatColor;

public enum Messages
{
    SERVER_TAG("Messages.SERVER_TAG"),
    JOIN_MSG("Messages.JOIN_MSG"),
    QUIT_MSG("Messages.QUIT_MSG");

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
