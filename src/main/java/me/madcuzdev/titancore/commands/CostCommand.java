package me.madcuzdev.titancore.commands;

import me.madcuzdev.titancore.PriceHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CostCommand implements CommandExecutor {
    private PriceHandler priceHandler = new PriceHandler();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            try {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l" + PriceHandler.formatNumber((double) priceHandler.getPrestigePrice(Integer.parseInt(args[0])))));
            } catch (NumberFormatException exc) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Specifiy number between 1 and 2,147,483,647"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lFormat: /cost (prestige)"));
        }
        return true;
    }
}
