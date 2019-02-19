package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.commands.StaffMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        /*
        When the player quits he will be set out of StaffMode so his inventory will be restored.
         */
        if (StaffMode.staffModeList.contains(event.getPlayer().getUniqueId().toString())) {
            StaffMode.LeaveStaffMode(event.getPlayer());
        }
    }
}
