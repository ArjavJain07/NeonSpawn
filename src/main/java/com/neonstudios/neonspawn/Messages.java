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
    private static String prefix = "";

    public static void setup(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
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
        prefix = ChatColor.translateAlternateColorCodes('&',
                messages.getString("prefix", "&b&lNeonSpawn &7»"));
        save();
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

        // Replace placeholders
        message = message.replace("%prefix%", prefix);
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(String.valueOf(replacements[i]), String.valueOf(replacements[i + 1]));
            }
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getWithPrefix(String key, Object... replacements) {
        String message = get(key, replacements);
        return message.startsWith(prefix) ? message : prefix + message;
    }

    public static String getActionbar(String phase, Object... replacements) {
        return get("actionbar." + phase, replacements);
    }

    public static String getBossbar(String phase, Object... replacements) {
        return get("bossbar." + phase, replacements);
    }

    public static String getTitle(String key, Object... replacements) {
        return get("title.main", replacements);
    }

    public static String getSubtitle(String key, Object... replacements) {
        return get("title.sub", replacements);
    }

    public static String getRaw(String key, Object... replacements) {
        return get(key, replacements);
    }
}
