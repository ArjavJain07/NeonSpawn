package com.neonstudios.neonspawn;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportTask {

    public static void start(Player player, Location target, int delay) {
        player.sendMessage(Messages.get("teleport-start").replace("%time%", String.valueOf(delay)));

        new BukkitRunnable() {
            final Location startLoc = player.getLocation().getBlock().getLocation();
            int timeLeft = delay;

            public void run() {
                if (!player.getLocation().getBlock().getLocation().equals(startLoc)) {
                    player.sendMessage(Messages.get("teleport-cancelled"));
                    if (NeonSpawn.getInstance().getConfig().getBoolean("cancel-sound"))
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
                    cancel();
                    return;
                }

                if (timeLeft <= 0) {
                    player.teleport(target);
                    player.sendTitle(ChatColor.GREEN + "Teleported!", ChatColor.YELLOW + "Welcome to Spawn", 10, 40, 10);
                    player.sendMessage(Messages.get("teleport-success"));
                    player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                            new net.md_5.bungee.api.chat.TextComponent(ChatColor.AQUA + "✨ Whoosh! You've arrived."));
                    player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 50, 1, 1, 1);
                    cancel();
                    return;
                }

                if (NeonSpawn.getInstance().getConfig().getBoolean("countdown-sound"))
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);

                timeLeft--;
            }
        }.runTaskTimer(NeonSpawn.getInstance(), 0, 20);
    }
}
