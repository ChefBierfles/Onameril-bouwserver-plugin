package nl.hotseflots.onabouwserver.utils;

import nl.hotseflots.onabouwserver.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerCache {

    public static UUID getUUIDFromPlayerName(String playerName) {
        return UUID.fromString(Main.plugin.getPlayerCache().getString("Player2UUID." + playerName));
    }

    public static String getPlayerNameFromUUID(String UUIDString) {
        return Main.plugin.getPlayerCache().getString("UUID2Player." + UUIDString);
    }
}
