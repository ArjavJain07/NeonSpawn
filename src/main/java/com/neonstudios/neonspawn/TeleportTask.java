package com.neonstudios.neonspawn;

import org.bukkit.*;
import org.bukkit.boss.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportTask {

    public static void start(Player player, Location target, int delay) {
        final Location startLoc = player.getLocation().getBlock().getLocation();
        final FileConfiguration config = NeonSpawn.getInstance().getConfig();

        // Load global toggles once
        final boolean useBossBar = config.getBoolean("enable-bossbar", true);
        final boolean useActionBar = config.getBoolean("enable-actionbar", true);
        final boolean useTitle = config.getBoolean("enable-title", true);
        final boolean useParticles = config.getBoolean("enable-particles", true);
        final boolean useSounds = config.getBoolean("enable-sounds", true);

        // Load effect booleans once
        final boolean bossbarEffectStart = useBossBar && config.getBoolean("effects.bossbar.teleport-start", true);
        final boolean actionbarEffectStart = useActionBar && config.getBoolean("effects.actionbar.teleport-start", true);
        final boolean actionbarEffectCancelled = useActionBar && config.getBoolean("effects.actionbar.teleport-cancelled", true);
        final boolean actionbarEffectSuccess = useActionBar && config.getBoolean("effects.actionbar.teleport-success", true);
        final boolean titleEffectSuccess = useTitle && config.getBoolean("effects.title.teleport-success", true);

        // Load particle/sound types with fallbacks
        final Particle particleStart = getParticle(config, "teleport-start", Particle.PORTAL);
        final Particle particleCancel = getParticle(config, "teleport-cancelled", Particle.ANGRY_VILLAGER);
        final Particle particleSuccess = getParticle(config, "teleport-success", Particle.END_ROD);

        final Sound soundStart = getSound(config, "teleport-start", Sound.BLOCK_NOTE_BLOCK_HAT);
        final Sound soundCancel = getSound(config, "teleport-cancelled", Sound.ENTITY_ITEM_BREAK);
        final Sound soundSuccess = getSound(config, "teleport-success", Sound.ENTITY_ENDERMAN_TELEPORT);

        // Setup BossBar if enabled for start effect
        BossBar bossBar = null;
        if (bossbarEffectStart) {
            BarColor color;
            BarStyle style;
            try {
                color = BarColor.valueOf(config.getString("effects.bossbar.color", "BLUE").toUpperCase());
            } catch (IllegalArgumentException e) {
                color = BarColor.BLUE; // fallback
            }
            try {
                style = BarStyle.valueOf(config.getString("effects.bossbar.style", "SOLID").toUpperCase());
            } catch (IllegalArgumentException e) {
                style = BarStyle.SOLID; // fallback
            }

            bossBar = Bukkit.createBossBar(
                    Messages.getBossbar("teleport.start", "%time%", delay),
                    color,
                    style
            );
            bossBar.setProgress(1.0);
            bossBar.addPlayer(player);
        }

        final BossBar activeBossBar = bossBar;

        new BukkitRunnable() {
            int timeLeft = delay;

            @Override
            public void run() {
                // Cancel teleport if player changes block location
                if (!player.getLocation().getBlock().getLocation().equals(startLoc)) {
                    if (activeBossBar != null) {
                        activeBossBar.setColor(BarColor.RED);
                        activeBossBar.setTitle(Messages.getBossbar("teleport.cancelled"));
                        activeBossBar.setProgress(1.0);

                        new BukkitRunnable() {
                            double progress = 1.0;

                            @Override
                            public void run() {
                                progress -= 0.05;
                                if (progress <= 0) {
                                    activeBossBar.removeAll();
                                    cancel();
                                } else {
                                    activeBossBar.setProgress(progress);
                                }
                            }
                        }.runTaskTimer(NeonSpawn.getInstance(), 0L, 2L);
                    }

                    if (actionbarEffectCancelled) {
                        player.spigot().sendMessage(
                                net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                                new net.md_5.bungee.api.chat.TextComponent(Messages.getActionbar("teleport.cancelled"))
                        );
                    }

                    if (useParticles && player.getWorld() != null) {
                        player.getWorld().spawnParticle(particleCancel, player.getLocation().add(0, 1, 0), 30, 0.5, 0.5, 0.5, 0);
                    }

                    if (useSounds && player.getWorld() != null) {
                        player.playSound(player.getLocation(), soundCancel, 1f, 1f);
                    }

                    cancel(); // Stop the task
                    return;
                }

                // Show particle effects during countdown
                if (useParticles && player.getWorld() != null) {
                    drawParticleCircle(player.getLocation().add(0, 0.5, 0), particleStart, 1.2, 12);
                }

                // Update boss bar during countdown
                if (activeBossBar != null && bossbarEffectStart) {
                    activeBossBar.setTitle(Messages.getBossbar("teleport.start", "%time%", timeLeft));
                    activeBossBar.setProgress((double) timeLeft / delay);
                }

                // Send actionbar countdown
                if (actionbarEffectStart) {
                    player.spigot().sendMessage(
                            net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                            new net.md_5.bungee.api.chat.TextComponent(Messages.getActionbar("teleport.start", "%time%", timeLeft))
                    );
                }

                // Play start sound each tick (consider playing only once if needed)
                if (useSounds && player.getWorld() != null) {
                    player.playSound(player.getLocation(), soundStart, 1f, 1f);
                }

                if (timeLeft <= 0) {
                    if (activeBossBar != null) activeBossBar.removeAll();
                    player.teleport(target);

                    if (titleEffectSuccess) {
                        int fadeIn = config.getInt("effects.title.teleport-success.fadeIn", 10);
                        int stay = config.getInt("effects.title.teleport-success.stay", 40);
                        int fadeOut = config.getInt("effects.title.teleport-success.fadeOut", 10);
                        player.sendTitle(
                                Messages.getTitle("teleport.success"),
                                Messages.getSubtitle("teleport.success"),
                                fadeIn, stay, fadeOut
                        );
                    }

                    if (actionbarEffectSuccess) {
                        player.spigot().sendMessage(
                                net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                                new net.md_5.bungee.api.chat.TextComponent(Messages.getActionbar("teleport.success"))
                        );
                    }

                    if (useParticles && player.getWorld() != null) {
                        player.getWorld().spawnParticle(particleSuccess, player.getLocation(), 50, 1, 1, 1, 0);
                    }

                    if (useSounds && player.getWorld() != null) {
                        player.playSound(player.getLocation(), soundSuccess, 1f, 1f);
                    }

                    cancel();
                    return;
                }

                timeLeft--;
            }
        }.runTaskTimer(NeonSpawn.getInstance(), 0L, 20L);
    }

    private static void drawParticleCircle(Location center, Particle particle, double radius, int points) {
        World world = center.getWorld();
        if (world == null) return;

        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;
            world.spawnParticle(particle, center.clone().add(x, 0, z), 1, 0, 0, 0, 0);
        }
    }

    private static Particle getParticle(FileConfiguration config, String key, Particle fallback) {
        try {
            String name = config.getString("effects.particles." + key);
            return (name != null) ? Particle.valueOf(name.toUpperCase()) : fallback;
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("[NeonSpawn] Invalid particle in config: " + key);
            return fallback;
        }
    }

    private static Sound getSound(FileConfiguration config, String key, Sound fallback) {
        try {
            String name = config.getString("effects.sounds." + key);
            return (name != null) ? Sound.valueOf(name.toUpperCase()) : fallback;
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("[NeonSpawn] Invalid sound in config: " + key);
            return fallback;
        }
    }
}
