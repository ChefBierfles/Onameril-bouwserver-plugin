package nl.hotseflots.onabouwserver;

import nl.hotseflots.onabouwserver.commands.*;
import nl.hotseflots.onabouwserver.events.*;
import nl.hotseflots.onabouwserver.modules.PlayerStats;
import nl.hotseflots.onabouwserver.utils.Messages;
import nl.hotseflots.onabouwserver.utils.Options;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Logger logger = Bukkit.getLogger();

    private File globalCMDLogsFile;
    private FileConfiguration globalCMDLogs;

    private File playerLogsFile;
    private FileConfiguration playerLogs;

    private File playerCacheFile;
    private FileConfiguration playerCache;

    @Override
    public void onEnable() {

        instance = this;

        /*
        Generate plugin files
         */
        createFiles();
        nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA.dataGenerator();

        /*
        Initialize Messages cfg settings
         */
        Messages.init(Main.getInstance());
        Options.init(Main.getInstance());

        /*
        Initialize the config file
         */
        saveDefaultConfig();
        nl.hotseflots.onabouwserver.modules.TwoFactorAuth.TwoFA.dataGenerator();

        /*
        Register all of the events
         */
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickEvent(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakEvent(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceEvent(), this);
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatEvent(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMovementEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDropEvent(), this);
        Bukkit.getPluginManager().registerEvents(new CommandPreProcessEvent(), this);
        Bukkit.getPluginManager().registerEvents(new LeavesDecayEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractEntityEvent(), this);
        Bukkit.getPluginManager().registerEvents(new EntityPickUpItemEvent(), this);

        /*
        Register all of the commands
         */
        Bukkit.getPluginCommand("bouwserver").setExecutor(new BouwserverCommand());
        Bukkit.getPluginCommand("2fa").setExecutor(new TwoFactorAuthCommand());
        Bukkit.getPluginCommand("staffmode").setExecutor(new StaffModeCommand());
        Bukkit.getPluginCommand("buildmode").setExecutor(new BuildModeCommand());
        Bukkit.getPluginCommand("commandhistory").setExecutor(new CommandHistoryCommand());

        /*
        Save player stats every 5minutes as per interval
         */
        PlayerStats.savePlayerStatsInterval(60);

        /*
        Notfiying the console that the plugin loaded correctly
         */
        sendPluginState("boot");
    }

    @Override
    public void onDisable() {

        /*
        Notiying the console that the plugin unloaded correctly
         */
        sendPluginState("");
    }

    /*
    Return the plugins instance
     */
    public static Main getInstance() {
        return instance;
    }

    /*
    Create plugin files
     */
    private void createFiles() {

        /*
        Creating directories
         */
        File playerDataDir = new File(getDataFolder(), "PlayerData");

        if (!playerDataDir.exists()) {
            playerDataDir.mkdirs();
        }

        File commandHistoryDir = new File(getDataFolder(), "CommandHistory");

        if (!commandHistoryDir.exists()) {
            commandHistoryDir.mkdirs();
        }

        /*
        Create files
         */
        globalCMDLogsFile = new File(getDataFolder() + File.separator + "CommandHistory" + File.separator + "globallogs.yml");
        globalCMDLogs = YamlConfiguration.loadConfiguration(globalCMDLogsFile);

        playerLogsFile = new File(getDataFolder() + File.separator + "CommandHistory" + File.separator + "playerlogs.yml");
        playerLogs = YamlConfiguration.loadConfiguration(playerLogsFile);

        playerCacheFile = new File(getDataFolder() + File.separator + "PlayerData" + File.separator + "playercache.yml");
        playerCache = YamlConfiguration.loadConfiguration(playerCacheFile);

        if (!globalCMDLogsFile.exists()) {
            try {
                globalCMDLogsFile.createNewFile();
            } catch (IOException exc) { exc.printStackTrace(); }
        }

        if (!playerLogsFile.exists()) {
            try {
                playerLogsFile.createNewFile();
            } catch (IOException exc) { exc.printStackTrace(); }
        }

        if (!playerCacheFile.exists()) {
            try {
                playerCacheFile.createNewFile();
            } catch (IOException exc) { exc.printStackTrace(); }
        }
    }

    public FileConfiguration getPlayerCommandLogs() {
        return playerLogs;
    }

    public File getPlayerCommandLogsFile() {
        return playerLogsFile;
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


    /*
    Function to show that the plugin is loaded or unloaded
     */
    public void sendPluginState(String event) {
        logger.info("-------------------------------------------------------------------");
        logger.info(ChatColor.YELLOW + "  ___ ___         __                 _____.__          __          ");
        logger.info(ChatColor.GOLD + " /   |   \\  _____/  |_  ______ _____/ ____\\  |   _____/  |_  ______");
        logger.info(ChatColor.YELLOW + "/    ~    \\/  _ \\   __\\/  ___// __ \\   __\\|  |  /  _ \\   __\\/  ___/");
        logger.info(ChatColor.GOLD + "\\    Y    (  <_> )  |  \\___ \\\\  ___/|  |  |  |_(  <_> )  |  \\___ \\ ");
        logger.info(ChatColor.YELLOW + " \\___|_  / \\____/|__| /____  >\\___  >__|  |____/\\____/|__| /____  >");
        logger.info(ChatColor.GOLD + "       \\/                  \\/     \\/                            \\/ ");
        if (event == "boot") {
            logger.info(ChatColor.GREEN + "             Bouw server plugin is succesvol geladen!              ");
            logger.info("-------------------------------------------------------------------");
        } else {
            logger.info(ChatColor.RED + "            Bouw server plugin is succesvol afgesloten!            ");
            logger.info("-------------------------------------------------------------------");
        }
    }
}