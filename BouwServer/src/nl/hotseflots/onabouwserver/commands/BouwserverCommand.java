package nl.hotseflots.onabouwserver.commands;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.PlayerStats;
import nl.hotseflots.onabouwserver.utils.UUIDTool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BouwserverCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender.hasPermission("bouwserver.command.bouwserver")) {
            /*
            Check if the commandSender is not a player
            */
            if (!(commandSender instanceof Player)) {

                return false;
            }

            /*
            Casting commandSender to player object
             */
            Player sender = (Player) commandSender;


            /*
            Check if the command has an argument
            /bouwserver strings[0] strings[1]
             */
            if (strings.length > 0) {

                /*
                Check if a player name is entered
                 */
                if (strings.length > 1) {
                    OfflinePlayer target;
                    try {
                        target = Bukkit.getOfflinePlayer(UUIDTool.getUUIDFromPlayerName(strings[1].toLowerCase()));
                    } catch (NullPointerException exc) {
                        sender.sendMessage(ChatColor.RED + "Speler niet gevonden in onze database!");
                        return false;
                    }

                    if (strings[0].equalsIgnoreCase("stats")) {
                        if (sender.hasPermission("bouwserver.commands.stats")) {
                            PlayerStats.createInvenory(sender, target);
                            return true;
                        }
                    }
                } else if (strings.length == 1) {
                    if (strings[0].equalsIgnoreCase("stats")) {
                        if (sender.hasPermission("bouwserver.commands.stats")) {
                            PlayerStats.createInvenory(sender, sender);
                            return true;
                        }
                    }

                    if (strings[0].equalsIgnoreCase("leaderboard")) {
                        if (sender.hasPermission("bouwserver.commands.leaderboard")) {
                            PlayerStats.createLeaderboardInventory(sender);
                            return true;
                        }
                    }

                    if (strings[0].equalsIgnoreCase("reload")) {
                        if (sender.hasPermission("bouwserver.commands.reload")) {
                            Main.getInstance().saveConfig();
                            Main.getInstance().reloadConfig();
                            commandSender.sendMessage(ChatColor.GREEN + "Config file van de Bouwserver plugin is succesvol herladen!");
                        }
                        return true;
                    }
                } else if (strings.length == 0) {
                    if (sender.hasPermission("bouwserver.commands.stats")) {
                        PlayerStats.createInvenory(sender, sender);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
