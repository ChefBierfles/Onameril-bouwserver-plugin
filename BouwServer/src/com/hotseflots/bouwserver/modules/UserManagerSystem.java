package com.hotseflots.bouwserver.modules;

import com.hotseflots.bouwserver.Main;
import com.hotseflots.bouwserver.events.PlayerChat;
import com.hotseflots.bouwserver.utils.ConfigPaths;
import com.hotseflots.bouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitScheduler;

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
    public static String[] lastCommandArgs;
    public static Player lastCommandExecutor;
    public static Player lastCommandTarget;

    public static boolean getDuration;
    public static Inventory UserManagerRecord = Bukkit.createInventory(null, 54, ChatColor.RED + "User-Manager Record");
    public static Inventory UserManagerRecord2 = Bukkit.createInventory(null, 54, ChatColor.RED + "User-Manager Record");

    @Override
    public boolean onCommand(CommandSender cmdSender, Command cmd, String alias, String[] args) {
        /*
        Enkel spelers kunnen gebruik maken van User-Manager en niet de console.
         */
        getDuration = false;

        if (!(cmdSender instanceof Player)) {
            cmdSender.sendMessage(Messages.USERMANAGER_TAG + " Je moet wel ingelogd zijn om deze command uit te kunnen voeren!");
            return false;
        }

        /*
        Command Layout
            /usermanager ban player reason
                          0     1     2
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

            lastCommandTarget = target;
            lastCommandExecutor = (Player) cmdSender;
            lastCommandArgs = args;

            switch (args[0].toLowerCase()) {
                case "ban":
                    askDuration((Player) cmdSender);
                    break;
                case "unban":
                    unbanPlayer((Player) cmdSender, target, getReason(args));
                    break;
                case "kick":
                    kickPlayer((Player) cmdSender, target, getReason(args));
                    break;
                case "mute":
                    askDuration((Player) cmdSender);
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
                case "record":
                    CreateRecordInventory(target);
                    ((Player) cmdSender).openInventory(UserManagerRecord);
                    break;
                default:
                    cmdSender.sendMessage(Messages.USERMANAGER_TAG);
                    cmdSender.sendMessage("     Maak gebruik van de volgende argumenten:");
                    cmdSender.sendMessage(ChatColor.ITALIC + "     ban, unban, kick, mute, unmute, freeze, unfreeze");
                    break;
            }
        }
        return false;
    }

    public static void banPlayer(Player executor, UUID targetUUID, String expireDate, String reason) {

        Player target = Bukkit.getPlayer(Main.plugin.config.getString(ConfigPaths.namePath(targetUUID.toString())));

        //Broadcast message
        Bukkit.broadcastMessage(Messages.BAN_MSG(executor.getName(), target.getName(), reason, expireDate));

        //Target message
        if (target.isOnline()) {
            Bukkit.getScheduler().runTask(Main.plugin, new Runnable() {
                public void run() {
                    target.kickPlayer(Messages.BANNED_MSG(executor.getName(), reason, expireDate));
                }
            });
        }

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

    public static void mutePlayer(Player executor, UUID targetUUID, String expireDate ,String reason) {
        Player target = Bukkit.getPlayer(targetUUID);
        //Broadcast message
        Bukkit.broadcastMessage(Messages.MUTE_MSG(executor.getName(), target.getName(), reason, expireDate));

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
        Main.plugin.config.set(ConfigPaths.GetDetailPath(target.getUniqueId().toString(), "mutes", muteAmount, "expires"), expireDate);

        Main.plugin.saveConfig();
    }

    public static void unmutePlayer(Player executor, Player target, String reason) {

    }

    public static void freezePlayer(Player executor, Player target) {

    }

    public static void unfreezePlayer(Player executor, Player target) {

    }

    public static void askDuration(Player executor) {
        getDuration = true;
        executor.sendMessage(Messages.SERVER_TAG + " hoelang moet deze straf duren?");
    }

    public static void peformTask(String durationString) {

        String task = lastCommandArgs[0];
        Player executor = lastCommandExecutor;
        UUID target = lastCommandTarget.getUniqueId();
        String reason = getReason(lastCommandArgs);
        durationString = getExpireDate(durationString);

        if (target == null) {
            executor.sendMessage("Erorr when peforming task, cant find player!");
            return;
        }

        switch (task) {
            case "ban":
                banPlayer(executor, target, durationString, reason);
                break;
            case "mute":
                mutePlayer(executor, target, durationString, reason);
                break;
        }
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

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getExpireDate(String durationString) {
        if (durationString.equalsIgnoreCase("perm")) {
            durationString = "Nooit";
            return durationString;
        }

        durationString = durationString.toLowerCase();
        Duration duration;

        if (durationString.contains("d")) {
            //3d5m3s
            String[] spliter = durationString.split("d");
            duration = Duration.parse("P" + spliter[0] + "d" + "T" + spliter[1]);
        } else {
            duration = Duration.parse("PT" + durationString);
        }

        long timesToSeconds = (duration.toMillis() / 1000);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        date.setSeconds(date.getSeconds() + (int) timesToSeconds);
        return dateFormat.format(date);
    }

    public static void CreateRecordInventory(Player target) {

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta sm = (SkullMeta) skull.getItemMeta();
        sm.setDisplayName(ChatColor.GOLD + target.getName());
        sm.setOwningPlayer(target);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.LIGHT_PURPLE + "Jouw UUID: " + ChatColor.DARK_PURPLE + target.getUniqueId().toString());
        lore.add(ChatColor.LIGHT_PURPLE + "Laaste inlogpoging: " + ChatColor.DARK_PURPLE + Main.plugin.config.getString(ConfigPaths.lastLoginPath(target.getUniqueId().toString())));
        sm.setLore(lore);
        UserManagerRecord.setItem(0, skull);

        for (int i = 1; i < 8; i++) {
            UserManagerRecord.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
        }
    }
}
