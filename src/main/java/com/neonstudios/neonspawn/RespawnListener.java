package com.neonstudios.neonspawn;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Location spawnLocation = NeonSpawn.getInstance().getSpawnLocation();
        if (spawnLocation != null) {
            event.setRespawnLocation(spawnLocation);
        }
    }
}