package com.neonstudios.neonspawn;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.get("only-players"));
            return true;
        }

        Location spawn = NeonSpawn.getInstance().getSpawnLocation();

        if (spawn == null) {
            player.sendMessage(Messages.get("spawn-not-set"));
            return true;
        }

        int delay = NeonSpawn.getInstance().getConfig().getInt("teleport-delay", 0); // Default to 0
        TeleportTask.start(player, spawn, delay);
        return true;
    }
}
