package me.madcuzdev.titancore.commands;

import me.madcuzdev.titancore.PriceHandler;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PickCommand implements CommandExecutor {
    private PriceHandler priceHandler = new PriceHandler();
    private ItemStack pickaxeItem = new ItemStack(Material.DIAMOND_PICKAXE); {
        pickaxeItem.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
        pickaxeItem.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 5);
        ItemMeta itemMeta = pickaxeItem.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        itemMeta.spigot().setUnbreakable(true);
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lPICKAXE"));
        itemMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&7Efficiency 10"), ChatColor.translateAlternateColorCodes('&', "&7Fortune 5")));
        pickaxeItem.setItemMeta(itemMeta);
    }

    public void givePickaxe(Player player) {
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(pickaxeItem);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(pickaxeItem);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lYou have been given a pickaxe"));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You do not have an empty inventory slot"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be a player to execute this command"));
        }
        return true;
    }
}
