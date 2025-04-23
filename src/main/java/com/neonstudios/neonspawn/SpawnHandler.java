package com.neonstudios.neonspawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class SpawnHandler {

    public static void loadSpawn() {
        FileConfiguration config = NeonSpawn.getInstance().getConfig();
        if (!config.contains("spawn")) return;

        Location loc = new Location(
                Bukkit.getWorld(config.getString("spawn.world")),
                config.getDouble("spawn.x"),
                config.getDouble("spawn.y"),
                config.getDouble("spawn.z"),
                (float) config.getDouble("spawn.yaw"),
                (float) config.getDouble("spawn.pitch")
        );
        NeonSpawn.getInstance().setSpawnLocation(loc);
    }

    public static void saveSpawn(Location loc) {
        FileConfiguration config = NeonSpawn.getInstance().getConfig();
        config.set("spawn.world", loc.getWorld().getName());
        config.set("spawn.x", loc.getX());
        config.set("spawn.y", loc.getY());
        config.set("spawn.z", loc.getZ());
        config.set("spawn.yaw", loc.getYaw());
        config.set("spawn.pitch", loc.getPitch());
        NeonSpawn.getInstance().saveConfig();
    }
}