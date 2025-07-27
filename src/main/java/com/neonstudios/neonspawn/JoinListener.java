package com.neonstudios.neonspawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Location spawn = NeonSpawn.getInstance().getSpawnLocation();
        if (spawn != null) {
            player.teleport(spawn);
            String msg = Messages.get("join-msg");
            if (!msg.isEmpty()) {
                player.sendMessage(msg);
            }
        }
    }
}
