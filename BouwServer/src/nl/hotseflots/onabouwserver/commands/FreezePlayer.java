package nl.hotseflots.onabouwserver.commands;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezePlayer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (command.getName().equalsIgnoreCase("freeze")) {
            if (commandSender.hasPermission("bouwserver.commands.freeze")) {

                if (strings.length == 0) {
                    commandSender.sendMessage(Messages.STAFF_TAG + "Je moet wel een speler opgeven!");
                    return false;
                }

                if (Bukkit.getPlayer(strings[0]) == null) {
                    commandSender.sendMessage(Messages.STAFF_TAG + "De opgegeven speler kan niet gevonden worden!!");
                    return false;
                }

                Player player = Bukkit.getPlayer(strings[0]);

                if (!StaffMode.frozenPlayerList.contains(player.getUniqueId().toString())) {
                    StaffMode.frozenPlayerList.add(player.getUniqueId().toString());
                    ActionBarAPI.sendActionBar((Player) commandSender, "You froze " + ChatColor.GOLD + player.getName());
                    ActionBarAPI.sendActionBar(player.getPlayer(), "You got frozen by " + ChatColor.GOLD + commandSender);
                } else if (StaffMode.frozenPlayerList.contains(player.getUniqueId().toString())) {
                    ActionBarAPI.sendActionBar((Player) commandSender, "You unfroze " + ChatColor.GOLD + player.getName());
                    ActionBarAPI.sendActionBar(player, "You got unfrozen by " + ChatColor.GOLD + commandSender.getName());
                    StaffMode.frozenPlayerList.remove(player.getUniqueId().toString());
                }
            }
        }

        if (command.getName().equalsIgnoreCase("unfreeze")) {
            if (commandSender.hasPermission("bouwserver.commands.unfreeze")) {
                if (strings.length == 0) {
                    commandSender.sendMessage(Messages.STAFF_TAG + "Je moet wel een speler opgeven!");
                    return false;
                }

                if (Bukkit.getPlayer(strings[0]) == null) {
                    commandSender.sendMessage(Messages.STAFF_TAG + "De opgegeven speler kan niet gevonden worden!!");
                    return false;
                }

                Player player = Bukkit.getPlayer(strings[0]);

                if (StaffMode.frozenPlayerList.contains(player.getUniqueId().toString())) {
                    ActionBarAPI.sendActionBar((Player) commandSender, "You unfroze " + ChatColor.GOLD + player.getName());
                    ActionBarAPI.sendActionBar(player, "You got unfrozen by " + ChatColor.GOLD + commandSender.getName());
                    StaffMode.frozenPlayerList.remove(player.getUniqueId().toString());
                }
            }
        }
        return false;
    }
}
