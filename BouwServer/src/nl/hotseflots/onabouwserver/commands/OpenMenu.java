package nl.hotseflots.onabouwserver.commands;

import nl.hotseflots.onabouwserver.Main;
import nl.hotseflots.onabouwserver.modules.PlayerCache;
import nl.hotseflots.onabouwserver.utils.Messages;
import nl.hotseflots.onabouwserver.utils.UUIDTool;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OpenMenu implements CommandExecutor {

    public static ItemStack historyItem = new ItemStack(Material.BOOK);
    public static ItemStack customHistoryItem = new ItemStack(Material.ENCHANTED_BOOK);
    public static ItemStack fillItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
    public static ItemStack playerStatsItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    public static ItemStack nextButtonItem = new ItemStack(Material.SIGN, 1);
    public static ItemStack prevButtonItem = new ItemStack(Material.SIGN, 1);
    public static List<String> dates = new ArrayList<>();
    public static Inventory historyInventory;

    public static void handleConsoleHistory(CommandSender commandSender, OfflinePlayer target, Command command, String s, String[] strings) {
         /*
         Check the given option
         */
        if (strings.length == 1) {
            commandSender.sendMessage(ChatColor.RED + "Geef een optie in hoever je terug wilt kijken! BIJV:");
            commandSender.sendMessage(ChatColor.RED + "last10, first13, 07/02/2019, last-monday, today, yesterday");
            return;
        }

        /*
        We need to get the total amount of keys(Dates) before we can return it by order
        */
        int lines = 0;
        for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
            lines++;
        }

        int amount = lines;
        lines = 0;

        /*
        Check what option has been selected
        */
        if (strings[1].contains("last") && !strings[1].contains("monday") && !strings[1].contains("thursday") && !strings[1].contains("wednesday") && !strings[1].contains("tuesday") && !strings[1].contains("friday") && !strings[1].contains("saturday") && !strings[1].contains("sunday")) {
            String[] splitter = strings[1].split("t");

            int orderAmount;
            try {
                orderAmount = Integer.parseInt(splitter[1]);
            } catch (NumberFormatException exc) {
                commandSender.sendMessage(ChatColor.RED + "Geef wel een goede optie in! BIJV:");
                commandSender.sendMessage(ChatColor.RED + "last10, first13, 07/02/2019, last-monday, today, yesterday");
                return;
            }

            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                if (lines >= (amount - (orderAmount))) {
                    commandSender.sendMessage(ChatColor.GOLD + line);
                    commandSender.sendMessage("       Command: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Command"));
                    commandSender.sendMessage("       Worldname: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".WorldName"));
                    commandSender.sendMessage("       Position: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Position"));
                }
                lines++;
            }
        } else if (strings[1].contains("first")) {
            String[] splitter = strings[1].split("t");
            int orderAmount = Integer.parseInt(splitter[1]);

            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                if (lines < orderAmount) {
                    commandSender.sendMessage(ChatColor.GOLD + line);
                    commandSender.sendMessage("       Command: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Command"));
                    commandSender.sendMessage("       Worldname: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".WorldName"));
                    commandSender.sendMessage("       Position: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Position"));
                }
                lines++;
            }
        } else if (strings[1].contains("yesterday")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, -1);
            String dateFormat = simpleDateFormat.format(calendar.getTime());

            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                if (line.contains(dateFormat)) {
                    commandSender.sendMessage(ChatColor.GOLD + line);
                    commandSender.sendMessage("       Command: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Command"));
                    commandSender.sendMessage("       Worldname: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".WorldName"));
                    commandSender.sendMessage("       Position: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Position"));
                }
                lines++;
            }
        } else if (strings[1].contains("last-monday")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = new GregorianCalendar();
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                calendar.add(Calendar.DAY_OF_WEEK, -1);
            }
            String dateFormat = simpleDateFormat.format(calendar.getTime());

            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                if (line.contains(dateFormat)) {
                    commandSender.sendMessage(ChatColor.GOLD + line);
                    commandSender.sendMessage("       Command: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Command"));
                    commandSender.sendMessage("       Worldname: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".WorldName"));
                    commandSender.sendMessage("       Position: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Position"));
                }
                lines++;
            }
        } else if (strings[1].contains("last-tuesday")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = new GregorianCalendar();
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY) {
                calendar.add(Calendar.DAY_OF_WEEK, -1);
            }
            String dateFormat = simpleDateFormat.format(calendar.getTime());

            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                if (line.contains(dateFormat)) {
                    commandSender.sendMessage(ChatColor.GOLD + line);
                    commandSender.sendMessage("       Command: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Command"));
                    commandSender.sendMessage("       Worldname: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".WorldName"));
                    commandSender.sendMessage("       Position: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Position"));
                }
                lines++;
            }
        } else if (strings[1].contains("last-wednesday")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = new GregorianCalendar();
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY) {
                calendar.add(Calendar.DAY_OF_WEEK, -1);
            }
            String dateFormat = simpleDateFormat.format(calendar.getTime());

            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                if (line.contains(dateFormat)) {
                    commandSender.sendMessage(ChatColor.GOLD + line);
                    commandSender.sendMessage("       Command: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Command"));
                    commandSender.sendMessage("       Worldname: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".WorldName"));
                    commandSender.sendMessage("       Position: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Position"));
                }
                lines++;
            }
        } else if (strings[1].contains("last-thursday")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = new GregorianCalendar();
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
                calendar.add(Calendar.DAY_OF_WEEK, -1);
            }
            String dateFormat = simpleDateFormat.format(calendar.getTime());

            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                if (line.contains(dateFormat)) {
                    commandSender.sendMessage(ChatColor.GOLD + line);
                    commandSender.sendMessage("       Command: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Command"));
                    commandSender.sendMessage("       Worldname: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".WorldName"));
                    commandSender.sendMessage("       Position: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Position"));
                }
                lines++;
            }
        } else if (strings[1].contains("last-friday")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = new GregorianCalendar();
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                calendar.add(Calendar.DAY_OF_WEEK, -1);
            }
            String dateFormat = simpleDateFormat.format(calendar.getTime());

            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                if (line.contains(dateFormat)) {
                    commandSender.sendMessage(ChatColor.GOLD + line);
                    commandSender.sendMessage("       Command: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Command"));
                    commandSender.sendMessage("       Worldname: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".WorldName"));
                    commandSender.sendMessage("       Position: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Position"));
                }
                lines++;
            }
        } else if (strings[1].contains("last-saturday")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = new GregorianCalendar();
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                calendar.add(Calendar.DAY_OF_WEEK, -1);
            }
            String dateFormat = simpleDateFormat.format(calendar.getTime());

            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                if (line.contains(dateFormat)) {
                    commandSender.sendMessage(ChatColor.GOLD + line);
                    commandSender.sendMessage("       Command: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Command"));
                    commandSender.sendMessage("       Worldname: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".WorldName"));
                    commandSender.sendMessage("       Position: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Position"));
                }
                lines++;
            }
        } else if (strings[1].contains("last-sunday")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = new GregorianCalendar();
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                calendar.add(Calendar.DAY_OF_WEEK, -1);
            }
            String dateFormat = simpleDateFormat.format(calendar.getTime());

            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                if (line.contains(dateFormat)) {
                    commandSender.sendMessage(ChatColor.GOLD + line);
                    commandSender.sendMessage("       Command: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Command"));
                    commandSender.sendMessage("       Worldname: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".WorldName"));
                    commandSender.sendMessage("       Position: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Position"));
                }
                lines++;
            }
        } else if (strings[1].contains("today")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = new GregorianCalendar();
            String dateFormat = simpleDateFormat.format(calendar.getTime());

            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                if (line.contains(dateFormat)) {
                    commandSender.sendMessage(ChatColor.GOLD + line);
                    commandSender.sendMessage("       Command: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Command"));
                    commandSender.sendMessage("       Worldname: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".WorldName"));
                    commandSender.sendMessage("       Position: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Position"));
                }
                lines++;
            }
        } else if (strings[1].indexOf("/") == 2 || strings[1].indexOf("-") == 2) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = new GregorianCalendar();
            String dateFormat;
            try {
                dateFormat = simpleDateFormat.format(simpleDateFormat.parse(strings[1]));
            } catch (ParseException exc) {
                return;
            }

            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                if (line.contains(dateFormat)) {
                    commandSender.sendMessage(ChatColor.GOLD + line);
                    commandSender.sendMessage("       Command: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Command"));
                    commandSender.sendMessage("       Worldname: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".WorldName"));
                    commandSender.sendMessage("       Position: " + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + line + ".Position"));
                }
                lines++;
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "Geef wel een goede optie in! BIJV:");
            commandSender.sendMessage(ChatColor.RED + "last10, first7, 07/02/2019, last-monday, today, yesterday");
        }
    }

    public static void OpenHistoryInventory(OfflinePlayer target, Player commandSender, int startIndex) {
        dates.clear();
        setupItems(target);
        setupItemLayout(target, startIndex);

        commandSender.openInventory(historyInventory);
    }

    public static void setupItems(OfflinePlayer target) {
        ArrayList<String> lore = new ArrayList<String>();
        /*
        History Item
         */
        ItemMeta historyItemMeta = historyItem.getItemMeta();
        historyItemMeta.setDisplayName(ChatColor.RED + "? ? ?");
        lore.add(ChatColor.DARK_PURPLE + "Geen history te zien hier!");
        historyItemMeta.setLore(lore);
        historyItem.setItemMeta(historyItemMeta);

        lore.clear();
        /*
        fillItem
         */
        ItemMeta fillItemMeta = fillItem.getItemMeta();
        fillItemMeta.setDisplayName(" ");
        fillItem.setItemMeta(fillItemMeta);

        /*
        playerStatsItem
         */
        SkullMeta skullMeta = (SkullMeta) playerStatsItem.getItemMeta();
        skullMeta.setDisplayName(ChatColor.GOLD + target.getName());
        lore.add(ChatColor.DARK_PURPLE + "Heeft " + ChatColor.LIGHT_PURPLE + getCommandAmount(target) + ChatColor.DARK_PURPLE + " geregistreerde commando's");
        skullMeta.setLore(lore);
        skullMeta.setOwningPlayer(target);
        playerStatsItem.setItemMeta(skullMeta);

        lore.clear();
        /*
        Next button
         */
        ItemMeta nextButtonItemMeta = nextButtonItem.getItemMeta();
        nextButtonItemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Next");
        lore.add(ChatColor.DARK_PURPLE + "Klik om de volgende pagina te laden!");
        nextButtonItemMeta.setLore(lore);
        nextButtonItem.setItemMeta(nextButtonItemMeta);

        lore.clear();
        /*
        Previous button
         */
        ItemMeta prevButtonItemMeta = prevButtonItem.getItemMeta();
        prevButtonItemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Previous");
        lore.add(ChatColor.DARK_PURPLE + "Klik om de vorige pagina te laden!");
        prevButtonItemMeta.setLore(lore);
        prevButtonItem.setItemMeta(prevButtonItemMeta);

        lore.clear();
    }

    public static void setupItemLayout(OfflinePlayer target, int startIndex) {

        if (startIndex < 0) {
            startIndex = 0;
        }
        /*
        Create inventory
        */
        String title = ChatColor.DARK_GRAY + "(Most recent" + ChatColor.GOLD + "" + ChatColor.BOLD + " > " + ChatColor.RESET + ChatColor.DARK_GRAY + "Oldest)";
        historyInventory = Bukkit.createInventory(null, 54, title);

        /*
        Get the amount of Keys(Dates) and put the in an array
        */
        try {
            int amountKeys = 0;
            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                amountKeys++;
                dates.add(dates.size(), line);
            }
        } catch (NullPointerException exc) {}

        /*
        Flip the array so we can order it by closest date till now
        */
        Collections.reverse(dates);

        /*
        Add the history;
        */
        int datesPos = startIndex;
        for (int i = 0; i <= 44; i++) {
            if (datesPos < dates.size() && dates.size() != 0) {
                ArrayList<String> lore = new ArrayList<>();
                ItemMeta customHistoryItemMeta = customHistoryItem.getItemMeta();
                customHistoryItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + dates.get(datesPos));
                lore.add(ChatColor.DARK_PURPLE + "Command: " + ChatColor.YELLOW + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + dates.get(datesPos) + ".Command"));
                lore.add(ChatColor.DARK_PURPLE + "Worldname: " + ChatColor.YELLOW + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + dates.get(datesPos) + ".WorldName"));
                lore.add(ChatColor.DARK_PURPLE + "Position: " + ChatColor.YELLOW + Main.getInstance().getPlayerCommandLogs().getString("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData." + dates.get(datesPos) + ".Position"));
                customHistoryItemMeta.setLore(lore);
                customHistoryItem.setItemMeta(customHistoryItemMeta);
                historyInventory.setItem(i, customHistoryItem);
                datesPos++;
            } else {
                historyInventory.setItem(i, historyItem);
                datesPos++;
            }
        }

        /*
        Set items for the lowest row.
        */
        historyInventory.setItem(45, fillItem);
        historyInventory.setItem(46, prevButtonItem);
        historyInventory.setItem(47, fillItem);
        historyInventory.setItem(48, fillItem);
        historyInventory.setItem(49, playerStatsItem);
        historyInventory.setItem(50, fillItem);
        historyInventory.setItem(51, fillItem);
        historyInventory.setItem(52, nextButtonItem);
        historyInventory.setItem(53, fillItem);
    }

    public static int getCommandAmount(OfflinePlayer target) {
        int lines = 0;
        try {
            for (String line : Main.getInstance().getPlayerCommandLogs().getConfigurationSection("PlayerCommandLogs." + target.getUniqueId().toString() + ".CommandData").getKeys(false)) {
                lines++;
            }
            return lines;
        } catch (NullPointerException exc) { return 0; }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        //Check if it is the correct command
        if (command.getName().equalsIgnoreCase("commandhistory")) {
            if (commandSender.hasPermission("bouwserver.commands.commandhistory")) {

                //Check if a player is submitted
                if (strings.length == 0) {
                    commandSender.sendMessage(Messages.STAFF_TAG.getMessage() + ChatColor.RED + "Je moet wel een speler opgeven!");
                    return false;
                }

                OfflinePlayer target = null;

                if (strings.length > 0) {
                    //Check if the player exists and break when its not found
                    try {
                        if (Bukkit.getPlayer(UUIDTool.getUUIDFromPlayerName(strings[0].toLowerCase())) != null) {
                            target = Bukkit.getPlayer(UUIDTool.getUUIDFromPlayerName(strings[0].toLowerCase()));
                        } else if (Bukkit.getOfflinePlayer(UUIDTool.getUUIDFromPlayerName(strings[0].toLowerCase())) != null) {
                            target = Bukkit.getOfflinePlayer(UUIDTool.getUUIDFromPlayerName(strings[0].toLowerCase()));
                        }
                    } catch (NullPointerException exc) {
                        commandSender.sendMessage(Messages.STAFF_TAG.getMessage() + ChatColor.RED + "Kan de opgegeven speler niet vinden. Hij staat mogelijk niet in het systeem geregistreerd");
                        return false;
                    } catch (ArrayIndexOutOfBoundsException exc) {
                        commandSender.sendMessage(Messages.STAFF_TAG.getMessage() + ChatColor.RED + "Kan de opgegeven speler niet vinden. Hij staat mogelijk niet in het systeem geregistreerd");
                        return false;
                    }

                    //Check if it is sended from the console
                    if (!(commandSender instanceof Player)) {
                        handleConsoleHistory(commandSender, target, command, s, strings);
                    } else {

                        OpenHistoryInventory(target, (Player) commandSender, 0);

                    }
                }
                return false;
            }
        }
        return false;
    }
}
