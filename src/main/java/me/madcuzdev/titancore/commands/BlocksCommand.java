package me.madcuzdev.titancore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class BlocksCommand implements Listener, CommandExecutor {

    private Inventory blocksGUI = Bukkit.createInventory(null, 45, ChatColor.translateAlternateColorCodes('&', "&6&lBlocks"));

    {
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.GRAY.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.BLACK.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.CYAN.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.MAGENTA.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.ORANGE.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.BLUE.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.BROWN.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.LIGHT_BLUE.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.RED.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.ORANGE.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.LIME.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.PURPLE.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.WHITE.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.SILVER.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.GREEN.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.YELLOW.getData()));
        blocksGUI.addItem(new ItemStack(Material.INK_SACK, 64, (short) DyeColor.PINK.getData()));
        blocksGUI.addItem(new ItemStack(Material.LOG, 64));
        blocksGUI.addItem(new ItemStack(Material.COBBLESTONE, 64));
        blocksGUI.addItem(new ItemStack(Material.STONE, 64));
        blocksGUI.addItem(new ItemStack(Material.DIRT, 64));
        blocksGUI.addItem(new ItemStack(Material.GRASS, 64));
        blocksGUI.addItem(new ItemStack(Material.SAND, 64));
        blocksGUI.addItem(new ItemStack(Material.GLASS, 64));
        blocksGUI.addItem(new ItemStack(Material.WOOL, 64));
        blocksGUI.addItem(new ItemStack(Material.QUARTZ_BLOCK, 64));
        blocksGUI.addItem(new ItemStack(Material.BRICK, 64));
        blocksGUI.addItem(new ItemStack(Material.CLAY, 64));
        blocksGUI.addItem(new ItemStack(Material.SMOOTH_BRICK, 64));
        blocksGUI.addItem(new ItemStack(Material.SANDSTONE, 64));
        blocksGUI.addItem(new ItemStack(Material.IRON_BLOCK, 64));
        blocksGUI.addItem(new ItemStack(Material.GOLD_BLOCK, 64));
        blocksGUI.addItem(new ItemStack(Material.REDSTONE_BLOCK, 64));
        blocksGUI.addItem(new ItemStack(Material.DIAMOND_BLOCK, 64));
        blocksGUI.addItem(new ItemStack(Material.TORCH, 64));
        blocksGUI.addItem(new ItemStack(Material.GLOWSTONE, 64));
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(blocksGUI.getTitle())) {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                event.setCancelled(true);
                if (event.getRawSlot() < blocksGUI.getSize()) {
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(event.getCurrentItem());
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You do not have any empty inventory space"));
                    }
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.openInventory(blocksGUI);
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be executed as a Player"));
        }
        return true;
    }
}
