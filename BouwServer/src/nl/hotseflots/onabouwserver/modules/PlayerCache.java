package nl.hotseflots.onabouwserver.modules;

import nl.hotseflots.onabouwserver.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;

public class PlayerCache {

    public static void registerPlayerData(Player player) {

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                //See if the player's UUID is registered UUID > NAME
                if (Main.getInstance().getPlayerCache().getString("UUID2Player." + player.getUniqueId().toString()) == null) {
                    Main.getInstance().getPlayerCache().set("UUID2Player." + player.getUniqueId().toString(), player.getName().toLowerCase());
                }

                //See if the player name is registered NAME > UUID
                if (Main.getInstance().getPlayerCache().getString("Player2UUID." + player.getName().toLowerCase()) == null) {
                    Main.getInstance().getPlayerCache().set("Player2UUID." + player.getName().toLowerCase(), player.getUniqueId().toString());
                }

                //See if the UUID exists in the logs
                if (Main.getInstance().getPlayerCommandLogs().get("PlayerCommandLogs." + player.getUniqueId().toString()) == null) {
                    Main.getInstance().getPlayerCommandLogs().set("PlayerCommandLogs." + player.getUniqueId().toString(), "");
                }
                Main.getInstance().getPlayerCommandLogs().set("PlayerCommandLogs." + player.getUniqueId().toString() + ".Name", player.getName().toLowerCase());
                Main.getInstance().getPlayerCommandLogs().set("PlayerCommandLogs." + player.getUniqueId().toString() + ".UUID", player.getUniqueId().toString());
                Main.getInstance().getPlayerCommandLogs().set("PlayerCommandLogs." + player.getUniqueId().toString() + ".IP-Adress", player.getAddress().getAddress().getHostAddress());
                if (Main.getInstance().getPlayerCommandLogs().get("PlayerCommandLogs." + player.getUniqueId().toString() + ".CommandData") == null) {
                    Main.getInstance().getPlayerCommandLogs().set("PlayerCommandLogs." + player.getUniqueId().toString() + ".CommandData", "");
                }
                if (Main.getInstance().getPlayerCache().get("Data." + player.getUniqueId().toString() + ".BLOCKS_PLACED") == null) {
                    Main.getInstance().getPlayerCache().set("Data." + player.getUniqueId().toString() + ".BLOCKS_PLACED", 0);
                }
                if (Main.getInstance().getPlayerCache().get("Data." + player.getUniqueId().toString() + ".BLOCKS_BROKEN") == null) {
                    Main.getInstance().getPlayerCache().set("Data." + player.getUniqueId().toString() + ".BLOCKS_BROKEN", 0);
                }
                if (Main.getInstance().getPlayerCache().get("Data." + player.getUniqueId().toString() + ".PLAYED_MILISECONDS") == null) {
                    Main.getInstance().getPlayerCache().set("Data." + player.getUniqueId().toString() + ".PLAYED_MILISECONDS", "0");
                }

                try {
                    Main.getInstance().getPlayerCommandLogs().save(Main.getInstance().getPlayerCommandLogsFile());
                    Main.getInstance().getPlayerCache().save(Main.getInstance().getPlayerCacheFile());
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        });
    }
}
