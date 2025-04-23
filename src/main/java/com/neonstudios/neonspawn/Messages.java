package com.neonstudios.neonspawn;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Messages {
    private static JavaPlugin plugin;
    private static FileConfiguration messages;
    private static File messagesFile;

    public static void setup(JavaPlugin plugin) {
        Messages.plugin = plugin;
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
            plugin.getLogger().info("Created new messages.yml file");
        }

        reload();
    }

    public static void reload() {
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        // Set default messages if they don't exist
        setDefault("spawn-not-set", "&cSpawn location has not been set!");
        setDefault("teleport-start", "&aTeleporting to spawn in %time% seconds...");
        setDefault("teleport-cancelled", "&cTeleport cancelled due to movement!");
        setDefault("teleport-success", "&aYou have been teleported to spawn!");
        setDefault("no-permission", "&cYou don't have permission!");
        setDefault("only-players", "&cThis command can only be run by players!");
        setDefault("menu-title", "&b&lSpawn Menu");

        save();
    }

    private static void setDefault(String path, String value) {
        if (!messages.contains(path)) {
            messages.set(path, value);
        }
    }

    private static void save() {
        try {
            messages.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save messages.yml");
        }
    }

    public static String get(String key, Object... replacements) {
        String message = messages.getString(key);

        if (message == null) {
            plugin.getLogger().warning("Missing message key: " + key);
            return ChatColor.RED + "[" + key + "]";
        }

        // Apply replacements
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(
                        String.valueOf(replacements[i]),
                        String.valueOf(replacements[i + 1])
                );
            }
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String get(String key) {
        return get(key, new Object[0]);
    }
}