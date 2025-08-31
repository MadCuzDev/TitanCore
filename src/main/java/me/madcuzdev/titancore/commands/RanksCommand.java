package me.madcuzdev.titancore.commands;

import me.madcuzdev.titancore.PriceHandler;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RanksCommand implements CommandExecutor {
    private PriceHandler priceHandler = new PriceHandler();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l--RANK COSTS--"));
        double totalPrice = 0;
        for (String key : priceHandler.getRankPrices().keySet().toArray(new String[0])) {
            if (!key.equals("default"))
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + key.toUpperCase() + " - $" + PriceHandler.formatNumber(Double.valueOf(priceHandler.getRankPrices().get(key)))));
            totalPrice+=priceHandler.getRankPrices().get(key);
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + "A - Z TOTAL: $" + PriceHandler.formatNumber(totalPrice)));
        return true;
    }
}
