package nl.hotseflots.onabouwserver.utils;

import nl.hotseflots.onabouwserver.Main;
import org.apache.logging.log4j.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;

public enum Messages
{
    SERVER_TAG("Messages.SERVER_TAG");

    private static Main main;
    private String path;

    Messages(String string) {
        path = string;
    }

    public static void init(Main main) {
        Messages.main = main;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString(path));
    }
}
