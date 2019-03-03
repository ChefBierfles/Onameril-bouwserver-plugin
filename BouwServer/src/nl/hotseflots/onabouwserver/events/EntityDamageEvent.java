package nl.hotseflots.onabouwserver.events;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.commands.StaffMode;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA;
import nl.hotseflots.onabouwserver.utils.Options;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;

public class EntityDamageEvent implements Listener {

    @EventHandler
    public void onEntityDamage(org.bukkit.event.entity.EntityDamageEvent event) {

        /*
        Check if the entity is a player
         */
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        /*
        Player is a entity so we create
        a new var specially for the player
         */
        Player player = (Player) event.getEntity();

        if (Main.getInstance().getConfig().getString("Modules.TwoFA.Module").equalsIgnoreCase("enabled")) {
            File userPath = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data" + File.separator + player.getUniqueId().toString() + ".yml");
            if (TwoFA.hasTwofactorauth(player.getUniqueId()) || !userPath.exists()) {

                if (!Options.DENY_DAMAGE.getBooleanValue()) {
                    return;
                }

                event.setCancelled(true);
            }
        }

        /*
        When ever the player is in staffmode
         */
        if (StaffMode.staffModeList.contains(event.getEntity().getUniqueId().toString())) {
            event.setCancelled(true);
        }

        /*
        Whenever the player is frozen
         */
        if (StaffMode.frozenPlayerList.contains(player.getUniqueId().toString())) {
            event.setDamage(0);
            event.setCancelled(true);;
        }
    }
}
