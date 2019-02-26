package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.PlayerStats;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;

public class PlayerQuitEvent implements Listener {

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {

        /*
        Check if the QUIT_MSG module is enabled
         */
        if (Main.getInstance().getConfig().getString("Modules.QUIT_MSG").equalsIgnoreCase("enabled")) {
            event.setQuitMessage(Messages.SERVER_TAG.getMessage() + Messages.QUIT_MSG.getMessage().replace("%player%", event.getPlayer().getName()));
        }

        /*
        Note the time in miliseconds that the player quitted
         */
        PlayerStats.setQuittedTimeInMiliseconds(event.getPlayer(), System.currentTimeMillis());

        /*
        If the player didnt succesfully setup 2Fa we will remove him once he quits
        so when he rejoins he will be asked to setup again
         */
        File userPath = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data" + File.separator + event.getPlayer().getUniqueId().toString() + ".yml");
        if (!userPath.exists()) {
            TwoFA.unloadAuthenticationDetails(event.getPlayer().getUniqueId());
        }
    }
}
