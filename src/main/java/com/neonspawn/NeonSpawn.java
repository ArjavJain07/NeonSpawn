package com.neonspawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class NeonSpawn extends JavaPlugin implements CommandExecutor {
    private Location spawnLocation;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadSpawnLocation();
        getCommand("spawn").setExecutor(this);
        getCommand("setspawn").setExecutor(this);
        getLogger().info("NeonSpawn enabled! Multi-world support + Configurable Cooldown added.");
    }

    @Override
    public void onDisable() {
        getLogger().info("NeonSpawn disabled!");
    }

    private void loadSpawnLocation() {
        FileConfiguration config = getConfig();
        if (config.contains("spawn.world")) {
            String worldName = config.getString("spawn.world");
            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                getLogger().warning("World " + worldName + " is not loaded! Attempting to load...");
                world = Bukkit.createWorld(new org.bukkit.WorldCreator(worldName));
                if (world == null) {
                    getLogger().severe("Failed to load world " + worldName + "! Spawn teleport may not work.");
                    return;
                }
            }

            double x = config.getDouble("spawn.x");
            double y = config.getDouble("spawn.y");
            double z = config.getDouble("spawn.z");
            float yaw = (float) config.getDouble("spawn.yaw");
            float pitch = (float) config.getDouble("spawn.pitch");

            spawnLocation = new Location(world, x, y, z, yaw, pitch);
            getLogger().info("Spawn location loaded successfully in world: " + worldName);
        }
    }

    private void saveSpawnLocation(Location location) {
        getConfig().set("spawn.world", location.getWorld().getName());
        getConfig().set("spawn.x", location.getX());
        getConfig().set("spawn.y", location.getY());
        getConfig().set("spawn.z", location.getZ());
        getConfig().set("spawn.yaw", location.getYaw());
        getConfig().set("spawn.pitch", location.getPitch());
        saveConfig();
        spawnLocation = location;
        getLogger().info("Spawn location set to: " + location.toString());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        FileConfiguration config = getConfig();
        int cooldownTime = config.getInt("cooldown.spawn", 5); // Default 5 seconds

        if (command.getName().equalsIgnoreCase("spawn")) {
            if (cooldowns.containsKey(playerUUID)) {
                long timeElapsed = (System.currentTimeMillis() - cooldowns.get(playerUUID)) / 1000;
                if (timeElapsed < cooldownTime) {
                    long timeLeft = cooldownTime - timeElapsed;
                    String cooldownMsg = config.getString("cooldown-message", "You must wait {time} seconds before using /spawn again!");
                    cooldownMsg = cooldownMsg.replace("{time}", String.valueOf(timeLeft));
                    player.sendMessage(cooldownMsg);
                    return true;
                }
            }

            if (spawnLocation != null) {
                player.teleport(spawnLocation);
                player.sendMessage(config.getString("spawn-message", "You have been teleported to spawn!"));
                cooldowns.put(playerUUID, System.currentTimeMillis());
            } else {
                player.sendMessage("Spawn is not set!");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("setspawn")) {
            if (!player.hasPermission("neonspawn.setspawn")) {
                player.sendMessage(config.getString("no-permission", "You do not have permission to set spawn!"));
                return true;
            }

            saveSpawnLocation(player.getLocation());
            player.sendMessage(config.getString("setspawn-message", "Spawn location set successfully!"));
            return true;
        }

        return false;
    }
}
