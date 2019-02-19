package nl.hotseflots.onabouwserver;

import nl.hotseflots.onabouwserver.commands.*;
import nl.hotseflots.onabouwserver.events.*;
import nl.hotseflots.onabouwserver.twofactorauth.AuthenticationDetails;
import nl.hotseflots.onabouwserver.twofactorauth.MCAuth;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public static Main plugin;
    public static Logger logger = Bukkit.getLogger();

    private File globalCMDLogsFile;
    private FileConfiguration globalCMDLogs;

    private File playerHistoryFile;
    private FileConfiguration playerHistoryLogs;

    private File playerCacheFile;
    private FileConfiguration playerCache;

    private File TwoFAFile;
    private FileConfiguration TwoFA;

    public Main() {
        plugin = this;
    }

    public void onEnable() {
        pluginMessage("boot");
        createFiles();
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamage(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClick(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDrop(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerKick(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractEntity(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getPluginManager().registerEvents(new EntityPickupItem(), this);
        Bukkit.getPluginManager().registerEvents(new CommandPreProcess(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlace(), this);
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChat(), this);
        Bukkit.getPluginCommand("staffmode").setExecutor(new StaffMode());
        Bukkit.getPluginCommand("buildmode").setExecutor(new Buildmode());
        Bukkit.getPluginCommand("freeze").setExecutor(new FreezePlayer());
        Bukkit.getPluginCommand("unfreeze").setExecutor(new FreezePlayer());
        Bukkit.getPluginCommand("commandhistory").setExecutor(new OpenMenu());
        Bukkit.getPluginCommand("2fa").setExecutor(new TwoFACommand());

        MCAuth.loadedAuthenticationDetails = new HashMap();
        MCAuth.dataGenerator();
    }

    public void onDisable() {
        pluginMessage("shutdown");

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (StaffMode.staffModeList.contains(players.getUniqueId().toString())) {
                StaffMode.LeaveStaffMode(players);
            }
        }

        try {
            playerCache.save(playerCacheFile);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public void pluginMessage(String event) {
        logger.info("----------------------------- " + Messages.SERVER_TAG + "------------------------------");
        logger.info(ChatColor.YELLOW + "  ___ ___         __                 _____.__          __          ");
        logger.info(ChatColor.GOLD + " /   |   \\  _____/  |_  ______ _____/ ____\\  |   _____/  |_  ______");
        logger.info(ChatColor.YELLOW + "/    ~    \\/  _ \\   __\\/  ___// __ \\   __\\|  |  /  _ \\   __\\/  ___/");
        logger.info(ChatColor.GOLD + "\\    Y    (  <_> )  |  \\___ \\\\  ___/|  |  |  |_(  <_> )  |  \\___ \\ ");
        logger.info(ChatColor.YELLOW + " \\___|_  / \\____/|__| /____  >\\___  >__|  |____/\\____/|__| /____  >");
        logger.info(ChatColor.GOLD + "       \\/                  \\/     \\/                            \\/ ");
        if (event == "boot") {
            logger.info(ChatColor.GREEN + "                Bouw server plugin is succesvol geladen!                ");
            logger.info("-------------------------------------------------------------------------");
        } else {
            logger.info(ChatColor.RED + "                Bouw server plugin is succesvol afgesloten!                ");
            logger.info("-------------------------------------------------------------------------");
        }
    }

    private void createFiles() {
        globalCMDLogsFile = new File(plugin.getDataFolder(), "globallogs.yml");
        globalCMDLogs = YamlConfiguration.loadConfiguration(globalCMDLogsFile);

        playerHistoryFile = new File(plugin.getDataFolder(), "playerlogs.yml");
        playerHistoryLogs = YamlConfiguration.loadConfiguration(playerHistoryFile);

        playerCacheFile = new File(plugin.getDataFolder(), "playercache.yml");
        playerCache = YamlConfiguration.loadConfiguration(playerCacheFile);

        TwoFAFile = new File(plugin.getDataFolder(), "2fa-config.yml");
        TwoFA = YamlConfiguration.loadConfiguration(TwoFAFile);

        if (!globalCMDLogsFile.exists()) {
            plugin.saveResource("globallogs.yml", false);
        }

        if (!playerHistoryFile.exists()) {
            plugin.saveResource("playerlogs.yml", false);
        }

        if (!playerCacheFile.exists()) {
            plugin.saveResource("playercache.yml", false);
        }

        if (!TwoFAFile.exists()) {
            plugin.saveResource("2fa-config.yml", false);
        }
    }

    public FileConfiguration getTwoFACFG() {
        return TwoFA;
    }

    public File getTwoFAFile() {
        return TwoFAFile;
    }

    public FileConfiguration getPlayerCommandLogs() {
        return playerHistoryLogs;
    }

    public File getPlayerCommandLogsFile() {
        return playerHistoryFile;
    }

    public FileConfiguration getGlobalCommandLogs() {
        return globalCMDLogs;
    }

    public File getGlobalCommandLogsFile() {
        return globalCMDLogsFile;
    }

    public FileConfiguration getPlayerCache() {
        return playerCache;
    }

    public File getPlayerCacheFile() {
        return playerCacheFile;
    }
}