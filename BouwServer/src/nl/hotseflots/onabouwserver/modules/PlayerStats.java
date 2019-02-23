package nl.hotseflots.onabouwserver.modules;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStats {

    private static HashMap<UUID, Integer> placedBlocksList = new HashMap<>();
    private static HashMap<UUID, Integer> brokenBlocksList = new HashMap<>();
    @Deprecated
    private static HashMap<UUID, Long> playedMilisecondsList = new HashMap<>();

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

    @Deprecated
    public static Long getPlayedMiliseconds(Player player) {

        if (playedMilisecondsList.containsKey(player.getUniqueId())) {
            return playedMilisecondsList.get(player.getUniqueId());
        } else {
            return null;
        }
    }

    public static Integer getPlacedBlocks(Player player) {

        if (placedBlocksList.containsKey(player.getUniqueId())) {
            return placedBlocksList.get(player.getUniqueId());
        } else {
            return 0;
        }
    }

    public static Integer setBrokenBlocks(Player player, int upBy) {
        return brokenBlocksList.put(player.getUniqueId(), getBrokenBlocks(player) + upBy);
    }

    @Deprecated
    public static Long setPlayedMiliseconds(Player player, long ms) {
        return playedMilisecondsList.put(player.getUniqueId(), playedMilisecondsList.get(player.getUniqueId()) - ms);
    }

    public static Integer setPlacedBlocks(Player player, int upBy) {
        return placedBlocksList.put(player.getUniqueId(), getPlacedBlocks(player) + upBy);
    }
}
