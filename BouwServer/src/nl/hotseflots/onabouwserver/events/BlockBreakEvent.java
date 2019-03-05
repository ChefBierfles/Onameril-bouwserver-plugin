package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BlockBreakEvent implements Listener {

    @EventHandler
    public void onBlockBreak(org.bukkit.event.block.BlockBreakEvent event) {

        /*
        If the player breaks a block we save it async so it has little
        inpact on the server
         */
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                PlayerStats.setBrokenBlocks(event.getPlayer(), 1);
            }
        });
    }
}
