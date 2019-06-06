package nl.hotseflots.onabouwserver.modules;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.utils.Options;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CommandSpy {

        /*  CommandSpy function:
        - Staff can see the commands from players with a lower rank.
        - All commands from all players get logged.
        Seeing order:
        Owner > Admin > Mod > Onameril > Rando
        Admin > Mod > Onameril > Rando
        Mod > Onameril > Rando
     */

    public static void CommandSpy(String peformedcommand, Player sender) {

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                String commandSpyMessage = ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + ChatColor.GOLD + "" + ChatColor.ITALIC + sender.getName() + ChatColor.GRAY + "" + ChatColor.ITALIC + " used the command " + ChatColor.GOLD + "" + ChatColor.ITALIC + peformedcommand + ChatColor.GRAY + "" + ChatColor.ITALIC + "]";

                if (Options.MODULE_COMMANDLOGGING.getStringValue().equalsIgnoreCase("Enabled")) {
                    //Broadcast the peformed command to the console
                    Main.getInstance().getLogger().info(commandSpyMessage);
                }

                if (Options.MODULE_COMMANDSPY.getStringValue().equalsIgnoreCase("Enabled")) {
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
                    Main.getInstance().getGlobalCommandLogs().set("GlobalCommandLogs." + dateFormat + ".Command", peformedcommand);
                    Main.getInstance().getGlobalCommandLogs().set("GlobalCommandLogs." + dateFormat + ".Name", sender.getName().toLowerCase());
                    Main.getInstance().getGlobalCommandLogs().set("GlobalCommandLogs." + dateFormat + ".UUID", sender.getUniqueId().toString());
                    Main.getInstance().getGlobalCommandLogs().set("GlobalCommandLogs." + dateFormat + ".WorldName", sender.getWorld().getName());
                    Main.getInstance().getGlobalCommandLogs().set("GlobalCommandLogs." + dateFormat + ".Position", playerPos);
                    try {
                        Main.getInstance().getGlobalCommandLogs().save(Main.getInstance().getGlobalCommandLogsFile());
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
                    Main.getInstance().getPlayerCommandLogs().set("PlayerCommandLogs." + sender.getUniqueId().toString() + ".CommandData." + dateFormat, "");
                    Main.getInstance().getPlayerCommandLogs().set("PlayerCommandLogs." + sender.getUniqueId().toString() + ".CommandData." + dateFormat + ".Command", peformedcommand);
                    Main.getInstance().getPlayerCommandLogs().set("PlayerCommandLogs." + sender.getUniqueId().toString() + ".CommandData." + dateFormat + ".WorldName", sender.getLocation().getWorld().getName());
                    Main.getInstance().getPlayerCommandLogs().set("PlayerCommandLogs." + sender.getUniqueId().toString() + ".CommandData." + dateFormat + ".Position", playerPos);
                    try {
                        Main.getInstance().getPlayerCommandLogs().save(Main.getInstance().getPlayerCommandLogsFile());
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }

                    try {
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (!players.getName().equals(sender.getName())) {
                                switch (RankDetector.getRank(sender.getUniqueId())) {
                                    case "Eigenaar":
                                        if (RankDetector.getRank(players.getUniqueId()) == "Eigenaar") {
                                            players.sendMessage(commandSpyMessage);
                                        }
                                        break;
                                    case "Admin":
                                        if (RankDetector.getRank(players.getUniqueId()) == "Eigenaar" || RankDetector.getRank(players.getUniqueId()) == "Admin") {
                                            players.sendMessage(commandSpyMessage);
                                        }
                                        break;
                                    case "Moderator":
                                        if (RankDetector.getRank(players.getUniqueId()) == "Eigenaar" || RankDetector.getRank(players.getUniqueId()) == "Admin" || RankDetector.getRank(players.getUniqueId()) == "Moderator") {
                                            players.sendMessage(commandSpyMessage);
                                        }
                                        break;
                                    case "Onameril":
                                        if (RankDetector.getRank(players.getUniqueId()) == "Eigenaar" || RankDetector.getRank(players.getUniqueId()) == "Admin" || RankDetector.getRank(players.getUniqueId()) == "Moderator") {
                                            players.sendMessage(commandSpyMessage);
                                        }
                                        break;
                                    case "Rando":
                                        if (RankDetector.getRank(players.getUniqueId()) == "Eigenaar" || RankDetector.getRank(players.getUniqueId()) == "Admin" || RankDetector.getRank(players.getUniqueId()) == "Moderator") {
                                            players.sendMessage(commandSpyMessage);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    } catch (NullPointerException exc) { exc.printStackTrace(); Bukkit.getLogger().info(ChatColor.RED + "De speler heeft niet een van de volgende rangen: Eigenaar, Admin, Moderator, Onameril of Rando");}
                }
            }
        });
    }
}
