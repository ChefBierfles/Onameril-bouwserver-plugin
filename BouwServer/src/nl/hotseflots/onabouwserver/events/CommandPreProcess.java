package nl.hotseflots.onabouwserver.events;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CommandPreProcess implements Listener {

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        CommandSpy(event.getMessage(), event.getPlayer());

        if (event.getMessage().equalsIgnoreCase("/pl") || event.getMessage().equalsIgnoreCase("/plugins") || event.getMessage().equals("?")) {
            if (!event.getPlayer().hasPermission("bouwserver.commands.seepluginhider")) {
                event.getPlayer().sendMessage("Plugins (5): " + ChatColor.GREEN + "Fix" + ChatColor.WHITE + ", " + ChatColor.GREEN + "Je" + ChatColor.WHITE + ", " + ChatColor.GREEN + "Eigen" + ChatColor.WHITE + ", " + ChatColor.GREEN + "Plugins" + ChatColor.WHITE + ", " + ChatColor.GREEN + "Loser!");
                event.setCancelled(true);
            }
        }
    }

    /*  CommandSpy function:
        - Staff can see the commands from players with a lower rank.
        - All commands from all players get logged.
        Seeing order:
        Owner > Admin > Mod > Onameril > Rando
        Admin > Mod > Onameril > Rando
        Mod > Onameril > Rando
        Permissions:
        OnamerilBouwServer.Eigenaar
        OnamerilBouwServer.Admin
        OnamerilBouwServer.Moderator
        OnamerilBouwServer.Onameril
        OnamerilBouwServer.Rando
     */

    public void CommandSpy(String peformedcommand, Player sender) {
        //Broadcast the peformed command to the console
        Main.logger.info(Messages.CommandSpyMessage(sender.getName(), peformedcommand));

        //Add command to global logs
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        Calendar calendar = new GregorianCalendar();
        String dateFormat = "[" + simpleDateFormat.format(calendar.getTime()) + "]";

        /*
        SAVE FORMAT:

        [TIME]
            Command:
            Name: PlayerName
            UUID: UniqueID
            WorldName: WorldName
            Position: X, Y, Z Coords
         */
        String playerPos = Math.round(sender.getLocation().getX()) + ", " + Math.round(sender.getLocation().getY()) + ", " + Math.round(sender.getLocation().getZ());
        Main.plugin.getGlobalCommandLogs().set("GlobalCommandLogs." + dateFormat + ".Command", peformedcommand);
        Main.plugin.getGlobalCommandLogs().set("GlobalCommandLogs." + dateFormat + ".Name", sender.getName().toLowerCase());
        Main.plugin.getGlobalCommandLogs().set("GlobalCommandLogs." + dateFormat + ".UUID", sender.getUniqueId().toString());
        Main.plugin.getGlobalCommandLogs().set("GlobalCommandLogs." + dateFormat + ".WorldName", sender.getWorld().getName());
        Main.plugin.getGlobalCommandLogs().set("GlobalCommandLogs." + dateFormat + ".Position", playerPos);
        try {
            Main.plugin.getGlobalCommandLogs().save(Main.plugin.getGlobalCommandLogsFile());
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        /*
        SAVE FORMAT:

        UUID:
            Name: PlayerName
            UUID: UniqueID
            IP-Adress: 192.168.1.1
            CommandData:
                [Time]:
                    Command: /kick nieuwesgeierig
                    WorldName: WorldName
                    Position: X, Y, Z Coords
         */

        //Add command to player logs
        Main.plugin.getPlayerCommandLogs().set("PlayerCommandLogs." + sender.getUniqueId().toString() + ".CommandData." + dateFormat, "");
        Main.plugin.getPlayerCommandLogs().set("PlayerCommandLogs." + sender.getUniqueId().toString() + ".CommandData." + dateFormat + ".Command", peformedcommand);
        Main.plugin.getPlayerCommandLogs().set("PlayerCommandLogs." + sender.getUniqueId().toString() + ".CommandData." + dateFormat + ".WorldName", sender.getLocation().getWorld().getName());
        Main.plugin.getPlayerCommandLogs().set("PlayerCommandLogs." + sender.getUniqueId().toString() + ".CommandData." + dateFormat + ".Position", playerPos);
        try {
            Main.plugin.getPlayerCommandLogs().save(Main.plugin.getPlayerCommandLogsFile());
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.getName() != sender.getName()) {
                if (sender.hasPermission("bouwserver.commandnotifier.ranking.eigenaar")) {
                    if (players.hasPermission("bouwserver.commandnotifier.ranking.eigenaar")) {
                        players.sendMessage(Messages.CommandSpyMessage(sender.getName(), peformedcommand));
                        return;
                    }
                } else if (sender.hasPermission("bouwserver.commandnotifier.ranking.admin")) {
                    if (players.hasPermission("bouwserver.commandnotifier.ranking.eigenaar") || players.hasPermission("bouwserver.commandnotifier.ranking.admin")) {
                        players.sendMessage(Messages.CommandSpyMessage(sender.getName(), peformedcommand));
                        return;
                    }
                } else if (sender.hasPermission("bouwserver.commandnotifier.ranking.moderator")) {
                    if (players.hasPermission("bouwserver.commandnotifier.ranking.eigenaar") || players.hasPermission("bouwserver.commandnotifier.ranking.admin") || players.hasPermission("bouwserver.commandnotifier.ranking.moderator")) {
                        players.sendMessage(Messages.CommandSpyMessage(sender.getName(), peformedcommand));
                        return;
                    }
                } else if (sender.hasPermission("bouwserver.commandnotifier.ranking.onameril")) {
                    if (players.hasPermission("bouwserver.commandnotifier.ranking.eigenaar") || players.hasPermission("bouwserver.commandnotifier.ranking.admin") || players.hasPermission("bouwserver.commandnotifier.ranking.moderator")) {
                        players.sendMessage(Messages.CommandSpyMessage(sender.getName(), peformedcommand));
                        return;
                    }
                } else if (sender.hasPermission("bouwserver.commandnotifier.ranking.rando")) {
                    if (players.hasPermission("bouwserver.commandnotifier.ranking.eigenaar") || players.hasPermission("bouwserver.commandnotifier.ranking.admin") || players.hasPermission("bouwserver.commandnotifier.ranking.moderator")) {
                        players.sendMessage(Messages.CommandSpyMessage(sender.getName(), peformedcommand));
                        return;
                    }
                }
            }
        }
    }
}