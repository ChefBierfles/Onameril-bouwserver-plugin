package com.hotseflots.bouwserver;

import com.hotseflots.bouwserver.events.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserManagerSystem implements CommandExecutor {

    /*
    User-Manager is an admin tool to manage players on the server by being able to:
        - Kick them
        - Ban them (Choice to IP or normal ban them for a given time.
        - Mute them
        - Freeze them (Completly disabling movement + interaction).
     */
    public static ArrayList<UUID> mutedPlayers = new ArrayList<UUID>();
    public static ArrayList<UUID> frozenPlayers = new ArrayList<UUID>();
    public static ArrayList<UUID> bannedPlayers = new ArrayList<UUID>();

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String alias, String[] args) {

        /*
        Enkel spelers kunnen gebruik maken van User-Manager en niet de console.
         */
        if (!(cmdSender instanceof Player)) {
            cmdSender.sendMessage(Messages.USERMANAGER_TAG + " Je moet wel ingelogd zijn om deze command uit te kunnen voeren!");
            return false;
        }

        if (cmd.getName().equalsIgnoreCase("usermanager")) {
            if (args.length < 1) {
                cmdSender.sendMessage(Messages.USERMANAGER_TAG);
                cmdSender.sendMessage("     Maak gebruik van de volgende argumenten:");
                cmdSender.sendMessage(ChatColor.ITALIC + "     ban, unban, kick, mute, unmute, freeze, unfreeze");
                return false;
            }

            if (args.length < 2) {
                cmdSender.sendMessage(Messages.USERMANAGER_TAG + ChatColor.RED + "je moet wel een speler definiëren!");
                return false;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                cmdSender.sendMessage(Messages.USERMANAGER_TAG + ChatColor.RED + " kan de gedefineerde speler niet vinden.");
                return false;
            }

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            switch (args[0].toLowerCase()) {
                case "ban":
                    banPlayer((Player) cmdSender, target, getReason(args), dateFormat.format(date));
                    break;
                case "unban":
                    unbanPlayer((Player) cmdSender, target, getReason(args), dateFormat.format(date));
                    break;
                case "kick":
                    kickPlayer((Player) cmdSender, target, getReason(args), dateFormat.format(date));
                    break;
                case "mute":
                    mutePlayer((Player) cmdSender, target, getReason(args), dateFormat.format(date));
                    break;
                case "unmute":
                    unmutePlayer((Player) cmdSender, target, getReason(args), dateFormat.format(date));
                    break;
                case "freeze":
                    freezePlayer((Player) cmdSender, target, dateFormat.format(date));
                    break;
                case "unfreeze":
                    unfreezePlayer((Player) cmdSender, target, dateFormat.format(date));
                    break;
                default:
                    cmdSender.sendMessage(Messages.USERMANAGER_TAG + "");
                    cmdSender.sendMessage(Messages.USERMANAGER_TAG);
                    cmdSender.sendMessage("     Maak gebruik van de volgende argumenten:");
                    cmdSender.sendMessage(ChatColor.ITALIC + "     ban, unban, kick, mute, unmute, freeze, unfreeze");
                    break;
            }
        }
        return false;
    }


    public static void banPlayer(Player executor, Player target, String reason, String date) {

    }

    public static void unbanPlayer(Player executor, Player target, String reason, String date) {

    }

    public static void kickPlayer(Player executor, Player target, String reason, String date) {
        //Kicked person message
        target.kickPlayer(Messages.KICKED_MSG(executor.getName(), reason));

        //Broadcast message
        Bukkit.broadcastMessage(Messages.KICK_MSG(executor.getName(), target.getName(), reason));

        int kickAmount = Integer.parseInt(Main.plugin.config.getString("players-history." + target.getUniqueId().toString() + ".kicks.amount")) + 1;

        //Increase kick amount
        Main.plugin.config.set("players-history." + target.getUniqueId().toString() + ".kicks.amount", kickAmount);

        //Additional info
        Main.plugin.config.set("players-history." + target.getUniqueId().toString() + ".kicks." + kickAmount, "");

        //Add reason
        Main.plugin.config.set("players-history." + target.getUniqueId().toString() + ".kicks." + kickAmount + ".reason", reason);

        //Add banned by
        Main.plugin.config.set("players-history." + target.getUniqueId().toString() + ".kicks." + kickAmount + ".by", executor.getName());

        //Add Date
        Main.plugin.config.set("players-history." + target.getUniqueId().toString() + ".kicks." + kickAmount + ".date", date);

        Main.plugin.saveConfig();
    }

    public static void mutePlayer(Player executor, Player target, String reason, String date) {

    }

    public static void unmutePlayer(Player executor, Player target, String reason, String date) {

    }

    public static void freezePlayer(Player executor, Player target, String date) {

    }

    public static void unfreezePlayer(Player executor, Player target, String date) {

    }

    public static String getReason(String[] strings) {
        String reason;

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < strings.length; i++) {
            sb.append(strings[i]).append(" ");
        }
        reason = sb.toString().trim();

        if (reason.isEmpty()) {
            reason = "Geen reden";
        }

        return reason;
    }
}
