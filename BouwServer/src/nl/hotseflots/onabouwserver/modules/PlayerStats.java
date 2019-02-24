package nl.hotseflots.onabouwserver.modules;

import nl.hotseflots.onabouwserver.Main;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStats {

    private static HashMap<UUID, Integer> placedBlocksList = new HashMap<>();
    private static HashMap<UUID, Integer> brokenBlocksList = new HashMap<>();
    private static HashMap<UUID, Long> joinedTimeInMiliseconds = new HashMap<>();
    private static HashMap<UUID, Long> quittedTimeInMiliseconds = new HashMap<>();

    public static float milisecondsToHours(Long miliseconds) {
        return Math.round((miliseconds / 3600000) * 100) / 100;
    }

    public static Integer getBrokenBlocks(Player player) {

        if (brokenBlocksList.containsKey(player.getUniqueId())) {
            return brokenBlocksList.get(player.getUniqueId());
        } else {
            return 0;
        }
    }

    public static Integer getPlacedBlocks(Player player) {

        if (placedBlocksList.containsKey(player.getUniqueId())) {
            return placedBlocksList.get(player.getUniqueId());
        } else {
            return 0;
        }
    }

    public static Long getJoinedTimeInMiliseconds(Player player) {
        return joinedTimeInMiliseconds.get(player.getUniqueId());
    }

    public static Long getQuittedTimeInMiliseconds(Player player) {
        return quittedTimeInMiliseconds.get(player.getUniqueId());
    }

    public static void setJoinedTimeInMiliseconds(Player player, long ms) {
        joinedTimeInMiliseconds.put(player.getUniqueId(), ms);
    }

    public static void setQuittedTimeInMiliseconds(Player player, long ms) {
        quittedTimeInMiliseconds.put(player.getUniqueId(), ms);
    }

    public static void setBrokenBlocks(Player player, int upBy) {
        brokenBlocksList.put(player.getUniqueId(), getBrokenBlocks(player) + upBy);
    }

    public static Long getPlayedMiliseconds(Player player) {

        if (quittedTimeInMiliseconds.get(player.getUniqueId()) == null) {
            return System.currentTimeMillis() - getJoinedTimeInMiliseconds(player);
        } else {
            return getQuittedTimeInMiliseconds(player) - getJoinedTimeInMiliseconds(player);
        }
    }

    public static void setPlacedBlocks(Player player, int upBy) {
        placedBlocksList.put(player.getUniqueId(), getPlacedBlocks(player) + upBy);
    }
}
