package com.neonstudios.neonspawn;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class NeonSpawnCommand implements CommandExecutor {

    private final ReloadCommand reloadCommand = new ReloadCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Messages.get("prefix") + " &eUsage: /neonspawn reload");
            return true;
        }

        if ("reload".equalsIgnoreCase(args[0])) {
            return reloadCommand.onCommand(sender, command, label, args);
        }

        sender.sendMessage(Messages.get("prefix") + " &cUnknown subcommand.");
        return true;
    }
}
