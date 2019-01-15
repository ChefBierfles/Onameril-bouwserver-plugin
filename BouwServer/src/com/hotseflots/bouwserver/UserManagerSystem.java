package com.hotseflots.bouwserver;

import com.hotseflots.bouwserver.utils.ConfigPaths;
import com.hotseflots.bouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.rmi.ConnectIOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class UserManagerSystem implements CommandExecutor {
    /*
    User-Manager is an admin tool to manage players on the server by being able to:
        - Kick them
        - Ban them (Choice to IP or normal ban them for a given time.
        - Mute them
        - Freeze them (Completly disabling movement + interaction).
     */
    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String alias, String[] args) {
        /*
        Enkel spelers kunnen gebruik maken van User-Manager en niet de console.
         */
        if (!(cmdSender instanceof Player)) {
            cmdSender.sendMessage(Messages.USERMANAGER_TAG + " Je moet wel ingelogd zijn om deze command uit te kunnen voeren!");
            return false;
        }

        /*
        Command Layout
            /usermanager ban player time reason
                          0     1     2    3
         */


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

            String expireDate;

            if (args[2].equalsIgnoreCase("perm")) {
                expireDate = args[2];
            } else {
                expireDate = getExpireDate(args[2]);
            }

            switch (args[0].toLowerCase()) {
                case "ban":
                    banPlayer((Player) cmdSender, target, expireDate, getReason(args));
                    break;
                case "unban":
                    unbanPlayer((Player) cmdSender, target, getReason(args));
                    break;
                case "kick":
                    kickPlayer((Player) cmdSender, target, getReason(args));
                    break;
                case "mute":
                    mutePlayer((Player) cmdSender, target, expireDate, getReason(args));
                    break;
                case "unmute":
                    unmutePlayer((Player) cmdSender, target, getReason(args));
                    break;
                case "freeze":
                    freezePlayer((Player) cmdSender, target);
                    break;
                case "unfreeze":
                    unfreezePlayer((Player) cmdSender, target);
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


    public static void banPlayer(Player executor, Player target, String expireDate, String reason) {
        //Broadcast message
        Bukkit.broadcastMessage(Messages.BAN_MSG(executor.getName(), target.getName(), expireDate, reason));

        //Target message
        target.kickPlayer(Messages.BANNED_MSG(executor.getName(), reason, expireDate));

        //Get current date in format : dd/MM/yyyy HH:mm:ss
        String date = getCurrentDate();

        //Get banamount from the cfg and up it by one
        int banAmount = Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(target.getUniqueId().toString(), "bans"))) + 1;

        //Increase kick amount
        Main.plugin.config.set(ConfigPaths.amountPath(target.getUniqueId().toString(), "bans"), banAmount);

        //Add banID
        Main.plugin.config.set(ConfigPaths.punishmentIDPath(target.getUniqueId().toString(), "bans", banAmount), "");

        //Add reason
        Main.plugin.config.set(ConfigPaths.GetDetailPath(target.getUniqueId().toString(), "bans", banAmount, "reasons"), reason);

        //Add banned by
        Main.plugin.config.set(ConfigPaths.GetDetailPath(target.getUniqueId().toString(), "bans", banAmount, "by"), executor.getName());

        //Add Date
        Main.plugin.config.set(ConfigPaths.GetDetailPath(target.getUniqueId().toString(), "bans", banAmount, "date"), date);

        //Add expiredate
        if (expireDate.equalsIgnoreCase("perm")) {
            expireDate = "Permanent";
        }
        Main.plugin.config.set(ConfigPaths.GetDetailPath(target.getUniqueId().toString(), "bans", banAmount, "expires"), expireDate);

        Main.plugin.saveConfig();
    }

    public static void unbanPlayer(Player executor, Player target, String reason) {

    }

    public static void kickPlayer(Player executor, Player target, String reason) {
        //Kicked person message
        target.kickPlayer(Messages.KICKED_MSG(executor.getName(), reason));

        //Broadcast message
        Bukkit.broadcastMessage(Messages.KICK_MSG(executor.getName(), target.getName(), reason));

        //Get current date in format : dd/MM/yyyy HH:mm:ss
        String date = getCurrentDate();

        //Get kickamount from the cfg and increase it by one
        int kickAmount = Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(target.getUniqueId().toString(), "kicks"))) + 1;

        //Increase kick amount
        Main.plugin.config.set(ConfigPaths.amountPath(target.getUniqueId().toString(), "kicks"), kickAmount);

        //Additional info
        Main.plugin.config.set(ConfigPaths.punishmentIDPath(target.getUniqueId().toString(), "kicks", kickAmount), "");

        //Add reason
        Main.plugin.config.set(ConfigPaths.GetDetailPath(target.getUniqueId().toString(), "kicks", kickAmount, "reason"), reason);

        //Add banned by
        Main.plugin.config.set(ConfigPaths.GetDetailPath(target.getUniqueId().toString(), "kicks", kickAmount, "by"), executor.getName());

        //Add Date
        Main.plugin.config.set(ConfigPaths.GetDetailPath(target.getUniqueId().toString(), "kicks", kickAmount, "date"), date);

        Main.plugin.saveConfig();
    }

    public static void mutePlayer(Player executor, Player target, String expireDate ,String reason) {
        //Broadcast message
        Bukkit.broadcastMessage(Messages.MUTE_MSG(executor.getName(), target.getName(), expireDate, reason));

        //Get current date in format : dd/MM/yyyy HH:mm:ss
        String date = getCurrentDate();

        //Get muteAmount from the cfg and increase it by one
        int muteAmount = Integer.parseInt(Main.plugin.config.getString(ConfigPaths.amountPath(target.getUniqueId().toString(), "mutes"))) + 1;

        //Increase kick amount
        Main.plugin.config.set(ConfigPaths.amountPath(target.getUniqueId().toString(), "mutes"), muteAmount);

        //Additional info
        Main.plugin.config.set(ConfigPaths.punishmentIDPath(target.getUniqueId().toString(), "mutes", muteAmount), "");

        //Add reason
        Main.plugin.config.set(ConfigPaths.GetDetailPath(target.getUniqueId().toString(), "mutes", muteAmount, "reason"), reason);

        //Add banned by
        Main.plugin.config.set(ConfigPaths.GetDetailPath(target.getUniqueId().toString(), "mutes", muteAmount, "by"), executor.getName());

        //Add Date
        Main.plugin.config.set(ConfigPaths.GetDetailPath(target.getUniqueId().toString(), "mutes", muteAmount, "date"), date);

        //Add expiredate
        if (expireDate.equalsIgnoreCase("perm")) {
            expireDate = "Permanent";
        }
        Main.plugin.config.set(ConfigPaths.GetDetailPath(target.getUniqueId().toString(), "mutes", muteAmount, "expires"), expireDate);

        Main.plugin.saveConfig();
    }

    public static void unmutePlayer(Player executor, Player target, String reason) {

    }

    public static void freezePlayer(Player executor, Player target) {

    }

    public static void unfreezePlayer(Player executor, Player target) {

    }

    public static String getReason(String[] strings) {
        String reason;

        StringBuilder sb = new StringBuilder();
        for (int i = 3; i < strings.length; i++) {
            sb.append(strings[i]).append(" ");
        }
        reason = sb.toString().trim();

        if (reason.isEmpty()) {
            reason = "Geen reden";
        }

        return reason;
    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getExpireDate(String durationString) {
        Duration duration = Duration.parse("PT" + durationString);
        long timesToSeconds = (duration.toMillis() / 1000);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        date.setSeconds(date.getSeconds() + (int) timesToSeconds);
        return dateFormat.format(date);
    }
}
