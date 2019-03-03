package nl.hotseflots.onabouwserver.modules;

import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sun.plugin2.message.Message;

import java.util.HashMap;
import java.util.UUID;

public class StaffChat {

    public static void SendMessageToStaff(Player player, String msg) {
        for (Player staffPlayers : Bukkit.getOnlinePlayers()) {
            if (staffPlayers.hasPermission("bouwserver.commands.staffmode")) {
                msg = msg.replaceFirst("!", "");
                String formattedMessage = Messages.STAFF_CHAT.getMessage();
                formattedMessage = formattedMessage.replace("%rank%", RankDetector.getRank(player.getUniqueId()));
                formattedMessage = formattedMessage.replace("%playername%", player.getName());
                formattedMessage = formattedMessage.replace("%message%", msg);

                staffPlayers.sendMessage(formattedMessage);
            }
        }
    }
}
