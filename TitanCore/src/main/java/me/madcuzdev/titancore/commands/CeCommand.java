package me.madcuzdev.titancore.commands;

import me.madcuzdev.titancore.EnchantHandler;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CeCommand implements CommandExecutor {
    private EnchantHandler enchantHandler = new EnchantHandler();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (sender.hasPermission("titancore.ce")) {
                if (args.length > 0) {
                    switch (args[0].toLowerCase()) {
                        case "list":
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lValid Enchants:"));
                            for (Enchantment enchantment : enchantHandler.getEnchants()) {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l- " + enchantment.getName().toUpperCase()));
                            }
                            break;
                        case "add":
                            ArrayList<String> lore = new ArrayList<>();
                            if (args.length > 2) {
                                if (player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR)) {
                                    lore.clear();
                                    if (player.getItemInHand().getItemMeta().getLore() != null) {
                                        lore.addAll(player.getItemInHand().getItemMeta().getLore());
                                    }
                                    int lvl;
                                    if (NumberUtils.isNumber(args[2])) {
                                        lvl = Integer.parseInt(args[2]);
                                    } else {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: '" + args[2] + "' is not a valid level"));
                                        return true;
                                    }
                                    String ench = "";
                                    boolean hasEnch = false;

                                    String requestedEnchant = args[1];

                                    for (Enchantment enchantment : enchantHandler.getEnchants()) {
                                        if (requestedEnchant.equalsIgnoreCase(enchantment.getName())) {
                                            ench = enchantment.getName();
                                            hasEnch = true;
                                            player.getItemInHand().addEnchantment(enchantment, lvl);
                                        }
                                    }

                                    if (hasEnch) {
                                        ItemMeta im = player.getItemInHand().getItemMeta();
                                        for (int i = 0; lore.size() > i; i++) {
                                            if (lore.get(i).startsWith(ChatColor.translateAlternateColorCodes('&', "&7" + ench))) {
                                                lore.remove(i);
                                            }
                                        }

                                        lore.add(ChatColor.translateAlternateColorCodes('&', "&7" + ench + " " + lvl));
                                        im.setLore(lore);
                                        player.getItemInHand().setItemMeta(im);
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lApplied enchant '" + args[1] + " " + args[2] + "' successfully"));
                                    } else {
                                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Couldn't find enchant '" + args[1] + "'"));
                                    }
                                } else {
                                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Cannot enchant this item"));
                                }
                            } else {
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lFormat: /ce add (enchant) (level)"));
                            }
                            break;
                        case "remove":
                            break;
                        case "help":
                        default:
                            break;
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lFormat: /ce (command) {arg2} {arg3}"));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You do not have permission to execute this command!"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be executed as a Player"));
        }
        return true;
    }
}
