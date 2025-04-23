package com.neonstudios.neonspawn;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Messages.get("only-players"));
            return true;
        }

        if (!player.hasPermission("neonspawn.setspawn")) {
            player.sendMessage(Messages.get("no-permission"));
            return true;
        }

        NeonSpawn.getInstance().setSpawnLocation(player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Spawn location set!");
        return true;
    }
}
