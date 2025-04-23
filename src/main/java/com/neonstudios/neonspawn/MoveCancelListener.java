package com.neonstudios.neonspawn;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MoveCancelListener implements Listener {
    private final Set<UUID> teleporting = new HashSet<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!teleporting.contains(e.getPlayer().getUniqueId())) return;

        if (!e.getFrom().getBlock().equals(e.getTo().getBlock())) {
            teleporting.remove(e.getPlayer().getUniqueId());
        }
    }

    public void addPlayer(UUID uuid) {
        teleporting.add(uuid);
    }

    public void removePlayer(UUID uuid) {
        teleporting.remove(uuid);
    }
}
