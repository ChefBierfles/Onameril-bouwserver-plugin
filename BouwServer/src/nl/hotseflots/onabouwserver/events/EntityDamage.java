package nl.hotseflots.onabouwserver.events;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import nl.hotseflots.onabouwserver.commands.StaffMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        if (StaffMode.staffModeList.contains(event.getEntity().getUniqueId().toString())) {
            event.setCancelled(true);
        }

        if (event.getEntity() instanceof EntityPlayer) {
            Player player = (Player) event.getEntity();
            if (StaffMode.frozenPlayerList.contains(player.getUniqueId().toString())) {
                event.setDamage(0);
                event.setCancelled(true);
                return;
            }
        }
    }
}
