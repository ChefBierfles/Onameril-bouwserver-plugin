package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.PlayerStats;
import nl.hotseflots.onabouwserver.modules.StaffUtils.StaffMode;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA;
import nl.hotseflots.onabouwserver.utils.Messages;
import nl.hotseflots.onabouwserver.utils.Options;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;

public class PlayerQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        /*
        When the player quits he will be set out of StaffMode so his inventory will be restored.
         */
        if (StaffMode.playersInStaffMode.contains(event.getPlayer().getUniqueId())) {
            StaffMode.leaveStaffMode(event.getPlayer());
            event.setQuitMessage("");
        } else {
            /*
            Check if the QUIT_MSG module is enabled
            */
            if (Options.MODULE_QUIT_MSG.getStringValue().equalsIgnoreCase("enabled")) {
                event.setQuitMessage(Messages.SERVER_TAG.getMessage() + Messages.QUIT_MSG.getMessage().replace("%player%", event.getPlayer().getName()));
            }
        }

        /*
        If the player didnt succesfully setup 2Fa we will remove him once he quits
        so when he rejoins he will be asked to setup again
         */
        File userPath = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data" + File.separator + event.getPlayer().getUniqueId().toString() + ".yml");
        if (!userPath.exists()) {
            TwoFA.unloadAuthenticationDetails(event.getPlayer().getUniqueId());
        }

        /*
        Save PlayerStats
         */
        PlayerStats.setPlayedTime(event.getPlayer());
        PlayerStats.savePlayerStatsToStorage(event.getPlayer());
    }
}
