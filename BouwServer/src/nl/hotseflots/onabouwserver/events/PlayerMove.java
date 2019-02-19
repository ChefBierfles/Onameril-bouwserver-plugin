package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.commands.StaffMode;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {

        /*
        If the player has been frozen he will be denied from moving
         */
        if (StaffMode.frozenPlayerList.contains(event.getPlayer().getUniqueId().toString())) {
            String title = ChatColor.DARK_RED + "You have been frozen!";
            String subtitle = ChatColor.RED + "Je kan momenteel niet bewegen!";
            event.getPlayer().sendTitle(title, subtitle, 0, 40, 10);
            event.setCancelled(true);
        }
    }
}
