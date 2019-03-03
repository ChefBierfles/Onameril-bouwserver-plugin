package nl.hotseflots.onabouwserver.commands;

import nl.hotseflots.onabouwserver.modules.PlayerStats;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BouwserverCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {

            Player player = (Player) commandSender;

            if (strings.length == 0) {
                PlayerStats.createInvenory(player);
            }
        } else {

        }
        return false;
    }
}
