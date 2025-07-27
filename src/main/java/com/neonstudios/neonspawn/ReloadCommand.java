package com.neonstudios.neonspawn;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission check (optional)
        if (!sender.hasPermission("neonspawn.reload")) {
            sender.sendMessage(Messages.get("no-permission"));
            return true;
        }

        NeonSpawn plugin = NeonSpawn.getInstance();

        // Reload config
        plugin.reloadConfig();

        // Reload messages.yml via Messages class
        Messages.reload();

        sender.sendMessage(Messages.get("reload-success"));
        return true;
    }
}
