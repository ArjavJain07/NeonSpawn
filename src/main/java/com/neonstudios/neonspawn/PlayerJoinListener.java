package com.neonstudios.neonspawn;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final NeonSpawn plugin;

    public PlayerJoinListener(NeonSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            Location spawn = plugin.getSpawnLocation();
            if (spawn != null) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.teleport(spawn);

                    FileConfiguration config = plugin.getConfig();

                    // Message
                    String type = config.getString("first-join.message-type", "chat").toLowerCase();
                    String message = Messages.get("join-msg").replace("%player%", player.getName());
                    message = ChatColor.translateAlternateColorCodes('&', message);

                    if (type.equals("title")) {
                        player.sendTitle(message, "", 10, 60, 20);
                    } else {
                        player.sendMessage(message);
                    }

                    // Sound
                    try {
                        String soundName = config.getString("first-join.sound", "ENTITY_PLAYER_LEVELUP");
                        float volume = (float) config.getDouble("first-join.sound-volume", 1.2);
                        float pitch = (float) config.getDouble("first-join.sound-pitch", 1.0);
                        Sound sound = Sound.valueOf(soundName.toUpperCase());
                        player.playSound(player.getLocation(), sound, volume, pitch);
                    } catch (Exception ignored) {}

                    // Particle
                    try {
                        String particleName = config.getString("first-join.particle", "HAPPY_VILLAGER");
                        int amount = config.getInt("first-join.particle-amount", 40);
                        double offset = config.getDouble("first-join.particle-offset", 0.5);
                        Particle particle = Particle.valueOf(particleName.toUpperCase());
                        player.getWorld().spawnParticle(particle, player.getLocation().add(0, 1, 0), amount, offset, offset, offset, 0.1);
                    } catch (Exception ignored) {}

                }, 1L);
            }
        }
    }
}
