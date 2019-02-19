package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.commands.StaffMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class PlayerKick implements Listener {

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {

        /*
        If player is in staffmode and he gets kick his inventory will restored.
         */
        if (StaffMode.staffModeList.contains(event.getPlayer().getUniqueId().toString())) {
            StaffMode.LeaveStaffMode(event.getPlayer());
        }
    }
}
