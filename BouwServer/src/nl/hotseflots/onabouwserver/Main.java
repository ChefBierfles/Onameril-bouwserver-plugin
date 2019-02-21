package nl.hotseflots.onabouwserver;

import nl.hotseflots.onabouwserver.events.PlayerJoinEvent;
import nl.hotseflots.onabouwserver.events.PlayerQuitEvent;
import nl.hotseflots.onabouwserver.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import sun.plugin2.message.Message;

import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private static Main instance;
    private static Logger logger = Bukkit.getLogger();

    @Override
    public void onEnable() {

        instance = this;
        Messages.init(Main.getInstance());

        /*
        Initialize the config file
         */
        saveDefaultConfig();

        /*
        Register all of the events
         */
        Bukkit.getPluginManager().registerEvents(new PlayerJoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitEvent(), this);

        /*
        Register all of the commands
         */

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

    public static Main getInstance() {
        return instance;
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