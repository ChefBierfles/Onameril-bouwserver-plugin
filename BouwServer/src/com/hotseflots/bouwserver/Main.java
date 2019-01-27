package com.hotseflots.bouwserver;

import com.hotseflots.bouwserver.events.PlayerChat;
import com.hotseflots.bouwserver.events.PlayerInteract;
import com.hotseflots.bouwserver.events.PlayerJoin;
import com.hotseflots.bouwserver.events.PlayerMove;
import com.hotseflots.bouwserver.modules.UserManagerSystem;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    public static Main plugin;
    public FileConfiguration config = this.getConfig();

    public Main() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChat(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);
        Bukkit.getPluginCommand("usermanager").setExecutor(new UserManagerSystem());

        setupConfigFile();
    }

    @Override
    public void onDisable() {

    }

    public void setupConfigFile() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }
    }
}
