package com.hotseflots.bouwserver;

import com.hotseflots.bouwserver.events.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;

    public Main() {
        instance = this;
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
    }

    public void onDisable() {

    }
}
