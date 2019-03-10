package nl.hotseflots.onabouwserver.events;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import net.minecraft.server.v1_12_R1.PacketPlayOutMount;
import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.StaffUtils.StaffMode;
import nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA;
import nl.hotseflots.onabouwserver.utils.Options;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PlayerInteractEvent implements Listener {

    private static HashMap<UUID, UUID> lastTPEDPlayer = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {

        if (Main.getInstance().getConfig().getString("Modules.TwoFA.Module").equalsIgnoreCase("enabled")) {
            File userPath = new File(Main.getInstance().getDataFolder() + File.separator + "PlayerData" + File.separator + "TwoFA-Data" + File.separator + event.getPlayer().getUniqueId().toString() + ".yml");
            if (TwoFA.hasTwofactorauth(event.getPlayer().getUniqueId()) || (!userPath.exists() && event.getPlayer().hasPermission("bouwserver.2fa.setup"))) {

                if (!Options.DENY_INTERACTION.getBooleanValue()) {
                    return;
                }

                event.setCancelled(true);
            }
        }

                /*
        When ever the player is in staffmode
         */
        if (StaffMode.playersInStaffMode.contains(event.getPlayer().getUniqueId())) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getItem() == null) {
            return;
        }

        if (StaffMode.frozenPlayers.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }

        if (StaffMode.isStaffItem(event.getItem())) {
            if (!StaffMode.playersInStaffMode.contains(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
                if (StaffMode.isStaffItem(event.getPlayer().getInventory().getItemInMainHand())) {
                    event.getPlayer().getInventory().remove(event.getItem());
                    return;
                } else if (StaffMode.isStaffItem(event.getPlayer().getInventory().getItemInOffHand())) {
                    event.getPlayer().getInventory().remove(event.getItem());
                    return;
                }
            }
        }

        /*
        Toggle vanish mode
         */
        if (event.getItem().isSimilar(StaffMode.vanishOnItem)) {
            if (event.getPlayer().hasPermission("bouwserver.staffmode.use.vanishitem")) {
                event.setCancelled(true);
                StaffMode.ToggleVanish(event.getPlayer(), "off");
            }
        } else if (event.getItem().isSimilar(StaffMode.vanishOffItem)) {
            if (event.getPlayer().hasPermission("bouwserver.staffmode.use.vanishitem")) {
                event.setCancelled(true);
                StaffMode.ToggleVanish(event.getPlayer(), "on");
            }
        }

        /*
        Gamemode swichter item
         */
        if (event.getItem().isSimilar(StaffMode.switchGameModeItem)) {
            if (event.getPlayer().hasPermission("bouwserver.staffmode.use.gamemodeswitcheritem")) {
                if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                    event.getPlayer().setGameMode(GameMode.SPECTATOR);
                } else if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
                    event.getPlayer().setGameMode(GameMode.CREATIVE);
                }
            }
        }

        /*
        Random tp to a player
         */
        if (event.getItem().isSimilar(StaffMode.randomTPItem)) {
            if (event.getPlayer().hasPermission("bouwserver.staffmode.use.randomtpitem")) {
                event.setCancelled(true);

                //Make a list of players
                ArrayList<Player> playersToTP = new ArrayList<>();
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (!players.getUniqueId().equals(event.getPlayer().getUniqueId())) {
                        playersToTP.add(players);
                    }
                }

                //If there are no players then dont continue and let the player know
                if (playersToTP.size() == 0) {
                    ActionBarAPI.sendActionBar(event.getPlayer(), ChatColor.RED + "Geen spelers gevonden om naar toe te teleporteren");
                    return;
                }

                if (playersToTP.size() == 1) {
                    event.getPlayer().sendMessage(ChatColor.AQUA + "Je bent naar " + ChatColor.BLUE + playersToTP.get(0).getName() + ChatColor.AQUA + " geteleporteerd!");
                    event.getPlayer().teleport(playersToTP.get(0));
                }

                if (playersToTP.size() > 1) {

                    //Generate a random int so we can get a random player as index
                    int randomInt = new Random().nextInt(playersToTP.size());
                    Player target = playersToTP.get(randomInt);

                    if (lastTPEDPlayer.containsKey(event.getPlayer().getUniqueId())) {
                        if (lastTPEDPlayer.get(event.getPlayer().getUniqueId()).equals(target.getUniqueId())) {
                            playersToTP.remove(randomInt);
                            randomInt = new Random().nextInt(playersToTP.size());
                            target = playersToTP.get(randomInt);
                        }
                    }

                    lastTPEDPlayer.remove(event.getPlayer().getUniqueId());
                    lastTPEDPlayer.put(event.getPlayer().getUniqueId(), target.getUniqueId());
                    event.getPlayer().teleport(target.getLocation());
                    target.addPassenger(event.getPlayer());
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Je bent naar " + ChatColor.DARK_GREEN + target.getName() + ChatColor.GREEN + " geteleporteerd!");
                }
            }
        }
    }
}
