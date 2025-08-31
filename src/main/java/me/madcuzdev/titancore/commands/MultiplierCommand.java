package me.madcuzdev.titancore.commands;

import me.madcuzdev.titancore.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MultiplierCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            String uuid = (((Player) sender).getUniqueId().toString());

            double tokenMulti = ConfigHandler.getPrestigesConfig().contains(uuid) ? (ConfigHandler.getPrestigesConfig().getInt(uuid)/10.0) + 1.0 : 1;
            double moneyMulti = ConfigHandler.getMasteryConfig().contains(uuid) && ConfigHandler.getMasteryConfig().getInt(uuid) > 0 ? ConfigHandler.getMasteryConfig().getInt(uuid) + 1 : 1;

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l--YOUR MULTIPLIERS--"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Token: " + tokenMulti));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Money: " + moneyMulti));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Must be a player to execute this command"));
        }
        return true;
    }
}
