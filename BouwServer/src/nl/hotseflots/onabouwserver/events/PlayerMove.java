package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.commands.StaffMode;
import nl.hotseflots.onabouwserver.twofactorauth.Options;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent event) {
        /*
        When ever the player is in 2FA he won't be able to move
         */
        if (!Options.DENY_MOVEMENT.getBooleanValue()) {
            return;
        }

        if (((event.getFrom().getBlockX() != event.getTo().getBlockX()) || (event.getFrom().getBlockY() != event.getTo().getBlockY()) || (event.getFrom().getBlockZ() != event.getTo().getBlockZ())) &&
                (Main.plugin.hasTwofactorauth(event.getPlayer().getUniqueId()))) {
            event.getPlayer().teleport(event.getFrom());
            return;
        }

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
