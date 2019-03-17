package nl.hotseflots.onabouwserver.modules;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.utils.Options;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SpawnManager {

    public void onSetSpawn(Location location) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                /*
                Define new spawn coords
                 */
                Main.getInstance().getConfig().set("Modules.SPAWNMANAGER.SPAWN_COORDS", location.toString());
            }
        });
    }

    public void loadSpawnCoordsToMemory() {

    }

}
