package com.neonstudios.neonspawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class SpawnHandler {

    public static void loadSpawn() {
        FileConfiguration config = NeonSpawn.getInstance().getConfig();
        if (!config.contains("spawn")) return;
        String worldName = config.getString("spawn.world", "");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            NeonSpawn.getInstance().getLogger().warning("Spawn world '" + worldName + "' does not exist or is not loaded.");
            return;
        }

        Location loc = new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("spawn.world"))),

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