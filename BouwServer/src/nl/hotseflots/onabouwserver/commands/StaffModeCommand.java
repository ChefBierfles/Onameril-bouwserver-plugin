package nl.hotseflots.onabouwserver.commands;

import nl.hotseflots.onabouwserver.modules.StaffUtils.StaffMode;
import nl.hotseflots.onabouwserver.utils.Options;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (Options.MODULE_STAFFMODE.getStringValue().equalsIgnoreCase("enabled")) {
            if (commandSender.hasPermission("bouwserver.commands.staffmode")) {
        /*
        Check whatever the commandSender is a player since the functions
        we use will only be available by a Player and not the console
         */
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(ChatColor.RED + "Dit commando kan enkel in-game uitgevoerd worden!");
                    return false;
                }

                Player player = (Player) commandSender;

        /*
        Toggle the staffmode when /staffmode is peformed
         */
                if (command.getName().equalsIgnoreCase("staffmode")) {
                    if (StaffMode.playersInVanishMode.contains(player.getUniqueId())) {
                        StaffMode.leaveStaffMode(player);
                        return true;
                    } else {
                        StaffMode.enterStaffMode(player);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
