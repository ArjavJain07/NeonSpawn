package com.neonstudios.neonspawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class NeonSpawn extends JavaPlugin {

    private static NeonSpawn instance;
    private Location spawnLocation;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Messages.setup(this);
        SpawnHandler.loadSpawn();
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("spawnmenu").setExecutor(new SpawnMenuCommand());
        Bukkit.getPluginManager().registerEvents(new MoveCancelListener(), this);
    }

    public static NeonSpawn getInstance() {
        return instance;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
        SpawnHandler.saveSpawn(location);
    }
}
