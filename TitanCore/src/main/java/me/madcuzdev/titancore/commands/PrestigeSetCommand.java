package me.madcuzdev.titancore.commands;

import me.madcuzdev.titancore.ConfigHandler;
import me.madcuzdev.titancore.PriceHandler;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PrestigeSetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("titancore.prestigeset")) {
            if (args.length > 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                String uuid = offlinePlayer.getUniqueId().toString();
                if (ConfigHandler.getPrestigesConfig().contains(uuid)) {
                    try {
                        int prestige = Integer.parseInt(args[1]);
                        ConfigHandler.getPrestigesConfig().set(uuid, prestige);
                        ConfigHandler.reloadPrestigesConfig();
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l" + offlinePlayer.getName() + "'s prestige set to " + prestige));
                    } catch (NumberFormatException exc) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Please specify a number between 1 - 2,147,483,647"));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: Could not find " + offlinePlayer.getName() + " in storage"));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lFormat: /prestigeset (player) (prestige)"));
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lError: You do not have permission to execute this command"));
        }
        return true;
    }
}
