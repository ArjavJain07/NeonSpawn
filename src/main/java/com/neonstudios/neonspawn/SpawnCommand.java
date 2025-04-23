package com.neonstudios.neonspawn;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.get("only-players"));
            return true;
        }

        Player player = (Player) sender;
        Location spawn = NeonSpawn.getInstance().getSpawnLocation();

        if (spawn == null) {
            player.sendMessage(Messages.get("spawn-not-set"));
            return true;
        }

        int delay = NeonSpawn.getInstance().getConfig().getInt("teleport-delay");
        TeleportTask.start(player, spawn, delay);
        return true;
    }
}
