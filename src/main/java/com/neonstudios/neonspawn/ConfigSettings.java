package com.neonstudios.neonspawn;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigSettings {

    private final JavaPlugin plugin;

    public ConfigSettings(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isEffectEnabled(String effectType, String key) {
        return plugin.getConfig().getBoolean("effects." + effectType + "." + key, false);
    }

    public boolean isSettingEnabled(String settingKey) {
        return plugin.getConfig().getBoolean(settingKey, false);
    }

    public Particle getParticle(String key) {
        try {
            String name = plugin.getConfig().getString("effects.particles." + key);
            return name != null ? Particle.valueOf(name.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("[NeonSpawn] Invalid particle name in config: " + key);
            return null;
        }
    }

    public Sound getSound(String key) {
        try {
            String name = plugin.getConfig().getString("effects.sounds." + key);
            return name != null ? Sound.valueOf(name.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("[NeonSpawn] Invalid sound name in config: " + key);
            return null;
        }
    }

    public int getTitleFade(String phase) {
        return plugin.getConfig().getInt("effects.title.teleport-success." + phase, 10);
    }

    public int getTeleportDelay() {
        return plugin.getConfig().getInt("teleport-delay", 5);
    }

    public boolean cancelOnMove() {
        return plugin.getConfig().getBoolean("cancel-on-move", true);
    }

    public boolean cancelOnBlockChangeOnly() {
        return plugin.getConfig().getBoolean("cancel-on-block-change-only", true);
    }
}
