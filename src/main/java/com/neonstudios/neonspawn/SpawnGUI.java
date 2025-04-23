package com.neonstudios.neonspawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnGUI {

    public static void open(Player player) {
        // Create inventory with translated title
        Inventory inv = Bukkit.createInventory(
                null,
                27,
                ChatColor.translateAlternateColorCodes('&', Messages.get("menu-title"))
        );

        // Create teleport to spawn item
        ItemStack teleportItem = createMenuItem(
                Material.ENDER_PEARL,
                ChatColor.GREEN + "Teleport to Spawn"
        );
        inv.setItem(11, teleportItem);

        // Create set spawn item if player has permission
        if (player.hasPermission("neonspawn.setspawn")) {
            ItemStack setSpawnItem = createMenuItem(
                    Material.COMPASS,
                    ChatColor.YELLOW + "Set Spawn Location"
            );
            inv.setItem(15, setSpawnItem);
        }

        player.openInventory(inv);
    }

    private static ItemStack createMenuItem(Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }

    public static void handleClick(InventoryClickEvent e) {
        // Check if this is our inventory
        if (!e.getView().getTitle().equals(
                ChatColor.translateAlternateColorCodes('&', Messages.get("menu-title")))
        ) {
            return;
        }

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        int slot = e.getSlot();

        // Handle slot clicks
        switch (slot) {
            case 11: // Teleport to spawn
                player.closeInventory();
                Bukkit.dispatchCommand(player, "spawn");
                break;

            case 15: // Set spawn
                if (player.hasPermission("neonspawn.setspawn")) {
                    player.closeInventory();
                    Bukkit.dispatchCommand(player, "setspawn");
                }
                break;
        }
    }
}