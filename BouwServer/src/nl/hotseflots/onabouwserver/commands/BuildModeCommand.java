package nl.hotseflots.onabouwserver.commands;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import nl.hotseflots.onabouwserver.modules.BuildMode;
import nl.hotseflots.onabouwserver.modules.StaffUtils.StaffMode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        /*
        Check whatever the commandSender is a player since the functions
        we use will only be available by a Player and not the console
        */
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Deze commando kan enkel in-game uitgevoerd worden!");
            return false;
        }
        Player player = (Player) commandSender;

        /*
        Toggle the staffmode when /staffmode is peformed
         */
        if (command.getName().equalsIgnoreCase("buildmode")) {
            if (commandSender.hasPermission("bouwserver.commands.buildmode")) {
                if (BuildMode.buildModeList.contains(player.getUniqueId())) {
                    BuildMode.LeaveBuildMode(player);
                    return true;
                } else {
                    BuildMode.EnterBuildMode(player);
                    return true;
                }
            }
        }
        return false;
    }
}
