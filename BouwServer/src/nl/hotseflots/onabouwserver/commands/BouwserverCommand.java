package nl.hotseflots.onabouwserver.commands;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.PlayerStats;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BouwserverCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        /*
        Check if the CommandSender is a player since we are opening
        an inventory and a console can't do that
         */
        if (commandSender instanceof Player) {

            /*
            Casting CommandSender as player
             */
            Player player = (Player) commandSender;

            /*
            Check if the args length is zero
            This will be true if the player only entered the command
             */
            if (strings.length == 0) {
                PlayerStats.createInvenory(player);
            }
        } else {
            /*
            Bouwserver commands that also work in console
             */
            if (strings.length == 1) {

                /*
                Reload the config file
                 */
                if (strings[0].equalsIgnoreCase("reload")) {
                    Main.getInstance().saveConfig();
                }
            }
        }
        return false;
    }
}
