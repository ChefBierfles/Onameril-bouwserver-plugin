package nl.hotseflots.onabouwserver.commands;

import nl.hotseflots.onabouwserver.modules.CommandHistoryMenu;
import nl.hotseflots.onabouwserver.modules.PlayerCache;
import nl.hotseflots.onabouwserver.utils.UUIDTool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sun.plugin2.message.Message;

public class CommandHistoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        //Check if it is the correct command
        if (command.getName().equalsIgnoreCase("commandhistory")) {
            if (commandSender.hasPermission("bouwserver.commands.commandhistory")) {

                //Check if a player is submitted
                if (strings.length == 0) {
                    commandSender.sendMessage(ChatColor.RED + "Je moet wel een speler opgeven!");
                    return false;
                }

                OfflinePlayer target = null;

                if (strings.length > 0) {
                    //Check if the player exists and break when its not found
                    try {
                        if (Bukkit.getPlayer(UUIDTool.getUUIDFromPlayerName(strings[0].toLowerCase())) != null) {
                            target = Bukkit.getPlayer(UUIDTool.getUUIDFromPlayerName(strings[0].toLowerCase()));
                        } else if (Bukkit.getOfflinePlayer(UUIDTool.getUUIDFromPlayerName(strings[0].toLowerCase())) != null) {
                            target = Bukkit.getOfflinePlayer(UUIDTool.getUUIDFromPlayerName(strings[0].toLowerCase()));
                        }
                    } catch (NullPointerException exc) {
                        commandSender.sendMessage(ChatColor.RED + "Kan de opgegeven speler niet vinden. Hij staat mogelijk niet in het systeem geregistreerd");
                        return false;
                    } catch (ArrayIndexOutOfBoundsException exc) {
                        commandSender.sendMessage(ChatColor.RED + "Kan de opgegeven speler niet vinden. Hij staat mogelijk niet in het systeem geregistreerd");
                        return false;
                    }

                    //Check if it is sended from the console
                    if (!(commandSender instanceof Player)) {
                        CommandHistoryMenu.handleConsoleHistory(commandSender, target, command, s, strings);
                    } else {

                        CommandHistoryMenu.OpenHistoryInventory(target, (Player) commandSender, 0);

                    }
                }
                return false;
            }
        }
        return false;
    }
}
