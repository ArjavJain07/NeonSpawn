package com.neonstudios.neonspawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class NeonSpawn extends JavaPlugin {

    private static NeonSpawn instance;
    private Location spawnLocation;
    private final ReloadCommand reloadCommand = new ReloadCommand();

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("messages.yml", false);

        Messages.setup(this);

        SpawnHandler.loadSpawn();

        // Register gameplay commands
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("neonspawn").setExecutor(reloadCommand); // Register reload command
        // Register the neonspawn base command with reload subcommand
        getCommand("neonspawn").setExecutor(new NeonSpawnCommand());

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(new MoveCancelListener(), this);
        Bukkit.getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        // Initialize Metrics with pluginId = 26222
        int pluginId = 26222;
        try {
            Metrics metrics = new Metrics(this, pluginId);
            getLogger().info("§aMetrics initialized successfully");
        } catch (Exception e) {
            getLogger().warning("§cFailed to initialize metrics: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("NeonSpawn is disabling...");

        try {
            if (spawnLocation != null) {
                SpawnHandler.saveSpawn(spawnLocation);
                getLogger().info("Spawn location saved.");
            }
        } catch (Exception e) {
            getLogger().severe("Failed to save spawn location on disable: " + e.getMessage());
        }

        // If you have any custom schedulers or tasks, cancel them here:
        // Bukkit.getScheduler().cancelTasks(this);

        // Clear any static references if needed:
        instance = null;

        getLogger().info("NeonSpawn disabled successfully.");
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
