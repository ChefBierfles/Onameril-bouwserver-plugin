package nl.hotseflots.onabouwserver.utils;

import nl.hotseflots.onabouwserver.Main;

import java.util.UUID;

public class UUIDTool {

    public static UUID getUUIDFromPlayerName(String playerName) {
        return UUID.fromString(Main.getInstance().getPlayerCache().getString("Player2UUID." + playerName));
    }

    public static String getPlayerNameFromUUID(String UUIDString) {
        return Main.getInstance().getPlayerCache().getString("UUID2Player." + UUIDString);
    }
}
